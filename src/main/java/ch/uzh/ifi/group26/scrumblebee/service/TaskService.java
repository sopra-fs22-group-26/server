package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.Comment;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.repository.TaskRepository;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
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

    // Get all tasks, completed or not
    public List<Task> getTasks() {
        return this.taskRepository.findAll();
    }

    // NEW VERSION (private tasks)
    // Get all tasks, completed or not
    public List<Task> getTasks(Long creatorId) {
        return this.taskRepository.findAll().stream().
                filter(task -> !task.getPrivateFlag() || Objects.equals(task.getCreatorId(), creatorId)).
                collect(Collectors.toList());
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

    // NEW VERSION (private tasks)
    // Return only either active or completed (= not active) tasks
    public List<Task> getTasks(String status, Long creatorId) {
        List<Task> allTasks = getTasks(creatorId);
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
        List<Task> allTasks = getTasks(userId);
        return allTasks.stream().filter(task -> (task.getStatus() == TaskStatus.ACTIVE) && (task.getAssignee() == userId)).collect(Collectors.toList());
    }


    /**
     * Used by: GET /tasks/reporter/{userId}
     * @param userId
     * @return all tasks (active and completed) for which user with userId is the reporter
     */
    public List<Task> getTasksToReportForUser(long userId) {
        List<Task> allTasks = getTasks(userId);
        return allTasks.stream().filter(task -> (task.getReporter() == userId)).collect(Collectors.toList());
    }


    public Optional<Task> getTask(long taskId) {
        String baseErrorMessage = "Error: No task found with id %d!";
        return Optional.ofNullable(taskRepository.findById(taskId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, taskId))));
    }

    // NEW VERSION (private tasks)
    public Optional<Task> getTask(long taskId, Long creatorId) {
        String baseErrorMessage = "Error: No task found with id %d!";
        Task task = taskRepository.findById(taskId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, taskId)));
        return Optional.ofNullable(!task.getPrivateFlag() || task.getCreatorId() == creatorId ? task : null);
    }


    /**
     * Used by: POST /tasks
     * @param newTask
     * @return the created task
     */
    public Task createTask(Task newTask) {

        newTask.setStatus(TaskStatus.ACTIVE);
        if (newTask.getCreatorId() == null) {
            newTask.setCreatorId(0L);
        }

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
    /**
     * Used by: DELETE /comments/{commentId}
     * @param aComment
     * @return Task
     */
    public Task deleteComment(Comment aComment){

        Optional<Task> aTask = taskRepository.findByTaskId(aComment.getBelongingTask());
        String baseErrorMessage = "The task with id: %s not found!";
        if (aTask.isPresent()){
            Task task = aTask.get();
            task.removeComment(aComment);
            taskRepository.save(task);
            taskRepository.flush();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, aComment.getBelongingTask()));
        }
        return aTask.get();
    }

    /**
     * Used by: POST /comments
     * @param aComment
     * @return Task
     */
    public Task assignCommentToTask(Comment aComment){

        Optional<Task> aTask = taskRepository.findByTaskId(aComment.getBelongingTask());
        String baseErrorMessage = "The task with id: %s not found!";
        if (aTask.isPresent()){
            Task task = aTask.get();
            task.addComment(aComment);
            taskRepository.save(task);
            taskRepository.flush();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, aComment.getBelongingTask()));
        }
        return aTask.get();
    }

}
