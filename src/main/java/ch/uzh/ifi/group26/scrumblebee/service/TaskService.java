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
import java.util.UUID;

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

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(@Qualifier("taskRepository") TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return this.taskRepository.findAll();
    }

     /**
     * Used by: POST /tasks
     * @param newTask
     * @return the created task
     */
    public Task createTask(Task newTask) {

        newTask.setTaskStatus(TaskStatus.ACTIVE);

        checkIfTaskIdExists(newTask);

        log.debug(newTask.getDueDate());
        log.debug(newTask.getTitle());
        log.debug(newTask.getDescription());
        log.debug(newTask.getEstimate());
        log.debug(newTask.getTaskPriority());
        log.debug(newTask.getTaskStatus());

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
        Task taskById = checkIfTaskIdExist(taskId);

        if(changesTask.getTitle()!=null){
            taskById.setTitle(changesTask.getTitle());
        }
        if(changesTask.getDescription()!=null){
            taskById.setDescription(changesTask.getDescription());
        }
        if(changesTask.getDueDate()!=null){
            taskById.setDueDate(changesTask.getDueDate());
        }
        if(changesTask.getTaskPriority()!=null){
            taskById.setTaskPriority(changesTask.getTaskPriority());
        }
        if(changesTask.getEstimate()!=null){
            taskById.setEstimate(changesTask.getEstimate());
        }
        if(changesTask.getLocation()!=null){
            taskById.setLocation(changesTask.getLocation());
        }

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
    public void deleteTask(long taskId) {
        Task taskById = checkIfTaskIdExist(taskId);

        taskRepository.delete(taskById);
    }

    //check is task exist by id
    private Task checkIfTaskIdExist(long taskId) {
        Task taskById = taskRepository.findByTaskId(taskId);

        String baseErrorMessage = "The user with id: %s not found!";
        if (taskById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, taskId));
        }
        return taskById;
    }


}
