package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import ch.uzh.ifi.group26.scrumblebee.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Task Controller
 * This class is responsible for handling all REST request that are related to
 * the task.
 * The controller will receive the request and delegate the execution to the
 * TaskService and finally return the result.
 */
@RestController
public class TaskController {

    private final TaskService taskService;

    TaskController(TaskService taskService) { this.taskService = taskService; }



    /*------------------------------------- GET requests -----------------------------------------------------------*/

    /**
     * Type: GET
     * URL: /tasks
     * Body: none
     * @return list<Task>
     */
    @GetMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TaskGetDTO> getAllTasks() {
        List<Task> tasks = taskService.getTasks();
        List<TaskGetDTO> taskGetDTOs = new ArrayList<>();
        for (Task task : tasks) {
            taskGetDTOs.add(DTOMapper.INSTANCE.convertEntityToTaskGetDTO(task));
        }
        return taskGetDTOs;
    }


    /*------------------------------------- POST requests ----------------------------------------------------------*/

    /**
     * Type: POST
     * URL: /tasks
     * Body: dueDate, title, description, estimate, priority, status
     * Protection: check if request is coming from the client (check for special token)
     * @return Task
     */
    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public TaskGetDTO createTask(@RequestBody TaskPostDTO taskPostDTO){
        // convert API task to internal representation
        Task input = DTOMapper.INSTANCE.convertTaskPostDTOtoEntity(taskPostDTO);
        // create task
        Task createdTask = taskService.createTask(input);
        //convert internal representation of task back to API
        return DTOMapper.INSTANCE.convertEntityToTaskGetDTO(createdTask);
    }



    /*------------------------------------- PUT requests -----------------------------------------------------------*/

    /**
     * Type: PUT
     * URL: /tasks/{taskId}
     * Body: username, name*, email, password
     * Protection: check if request is coming from the client (check for special token)
     * @return User
     */
    @PutMapping("/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public TaskGetDTO updateTask(@RequestBody TaskPostDTO taskPostDTO, @PathVariable long taskId) {
        Task changesTask = DTOMapper.INSTANCE.convertTaskPostDTOtoEntity(taskPostDTO);
        Task updatedTask = taskService.updateTask(taskId, changesTask);
        return DTOMapper.INSTANCE.convertEntityToTaskGetDTO(updatedTask);

    }
    /*------------------------------------- DELETE requests --------------------------------------------------------*/

    /**
     * Type: DELETE
     * URL: /tasks/{taskId}
     * Body: username, name*, email, password
     * Protection: check if request is coming from the client (check for special token)
     * @return User
     */
    @DeleteMapping("/tasks/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Long> deleteTask(@PathVariable long taskId) {
        taskService.deleteTask(taskId);

        return new ResponseEntity<>(taskId, HttpStatus.OK);
    }
}
