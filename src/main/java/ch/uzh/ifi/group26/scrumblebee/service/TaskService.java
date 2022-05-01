package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.repository.TaskRepository;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    TaskRepository taskRepository;

    /*
    @Autowired
    public TaskService(@Qualifier("taskRepository") TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

     */

    // Get all tasks, completed or not
    public List<Task> getTasks() {
        return this.taskRepository.findAll();
    }

    // Return only either active or completed (= not active) tasks
    public List<Task> getTasks(String status) {
        List<Task> allTasks = getTasks();
        switch (status) {
            case "active":
                allTasks = allTasks.stream().filter(task -> task.getStatus() == TaskStatus.ACTIVE).collect(Collectors.toList());
                break;
            case "completed":
                allTasks = allTasks.stream().filter(task -> task.getStatus() != TaskStatus.ACTIVE).collect(Collectors.toList());
        }
        return allTasks;
    }

    /**
     * Used by: GET /tasks/assignee/{userId}
     * @param userId
     * @return all active tasks for which user with userId is assigned
     */
    public List<Task> getTasksForUser(long userId) {
        List<Task> allTasks = getTasks();
        return allTasks.stream().filter(task -> (task.getStatus() == TaskStatus.ACTIVE) && (task.getAssignee() == userId)).collect(Collectors.toList());
    }


    /**
     * Used by: GET /tasks/reporter/{userId}
     * @param userId
     * @return all tasks (active and completed) for which user with userId is the reporter
     */
    public List<Task> getTasksToReportForUser(long userId) {
        List<Task> allTasks = getTasks();
        return allTasks.stream().filter(task -> (task.getReporter() == userId)).collect(Collectors.toList());
    }


    public Optional<Task> getTask(long taskId) {
        return this.taskRepository.findByTaskId(taskId);
    }

    /**
     * Used by: POST /tasks
     * @param newTask
     * @return the created task
     */
    public Task createTask(Task newTask) {

        newTask.setStatus(TaskStatus.ACTIVE);

        log.debug(newTask.getDueDate().toString());
        log.debug(newTask.getTitle());
        log.debug(newTask.getDescription());
        log.debug(newTask.getEstimate().toString());
        log.debug(newTask.getPriority().toString());
        log.debug(newTask.getStatus().toString());

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newTask = taskRepository.save(newTask);
        taskRepository.flush();

        log.debug("Created Information for Task: {}", newTask);
        return newTask;
    }

    /**
     * Used by: PUT /tasks/{taskId}
     * @param taskId
     * @param changesTask
     * @return the created user
     */
    public Task updateTask(long taskId, Task changesTask) {
        // Check if taskId is valid (checkIfTaskIdExist throws an exception otherwise)
        Task taskById = checkIfTaskIdExist(taskId);

        if (changesTask.getTitle() != null) {
            taskById.setTitle(changesTask.getTitle());
        }
        if (changesTask.getDescription() != null) {
            taskById.setDescription(changesTask.getDescription());
        }
        if (changesTask.getDueDate() != null) {
            taskById.setDueDate(changesTask.getDueDate());
        }
        if (changesTask.getPriority() != null) {
            taskById.setPriority(changesTask.getPriority());
        }
        if (changesTask.getEstimate() != null) {
            taskById.setEstimate(changesTask.getEstimate());
        }
        if (changesTask.getLocation() != null) {
            taskById.setLocation(changesTask.getLocation());
        }
        if (changesTask.getStatus() != null) {
            taskById.setStatus(changesTask.getStatus());
        }
        if (changesTask.getScore() > 0) {
            taskById.setScore(changesTask.getScore());
        }

        taskById.setAssignee(changesTask.getAssignee());
        taskById.setReporter(changesTask.getReporter());

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        taskById = taskRepository.save(taskById);
        taskRepository.flush();

        log.debug("Edited Information for Task: {}", taskById);
        return taskById;
    }


    /**
     * Used by: DELETE /tasks/{taskId}
     * @param taskId
     * @return the created user
     */
    public Task deleteTask(long taskId) {
        Task taskById = checkIfTaskIdExist(taskId);

        taskRepository.delete(taskById);

        return taskById;
    }

    //check is task exist by id
    private Task checkIfTaskIdExist(long taskId) {
        Optional<Task> taskById = taskRepository.findByTaskId(taskId);

        String baseErrorMessage = "The user with id: %s not found!";
        if (!taskById.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, taskId));
        }
        return taskById.get();
    }


}
