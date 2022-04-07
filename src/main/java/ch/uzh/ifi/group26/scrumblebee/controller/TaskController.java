package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.TaskMapper;
import ch.uzh.ifi.group26.scrumblebee.service.TaskService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
            taskGetDTOs.add(TaskMapper.INSTANCE.convertEntityToTaskGetDTO(task));
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
    public TaskPostDTO createTask(@RequestBody TaskPostDTO taskPostDTO){
        // convert API task to internal representation
        Task input = TaskMapper.INSTANCE.convertTaskPostDTOtoEntity(taskPostDTO);
        // create task
        Task createdTask = taskService.createTask(input);
        //convert internal representation of task back to API
        return TaskMapper.INSTANCE.convertEntityToTaskGetDTO(createdTask);
    }



    /*------------------------------------- PUT requests -----------------------------------------------------------*/

    /**
     * Type: PUT
     * URL: /
     * Body: username, name*, email, password
     * Protection: check if request is coming from the client (check for special token)
     * @return User
     */
    @PutMapping("/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public TaskGetDTO updateTask(@RequestBody TaskPostDTO taskPostDTO, @PathVariable long taskId) {
        Task changesTask = TaskMapper.INSTANCE.convertTaskPostDTOtoEntity(taskPostDTO);
        Task updatedTask = taskService.updateTask(taskId, changesTask);
        return TaskMapper.INSTANCE.convertEntityToTaskGetDTO(updatedTask);

    }
    /*------------------------------------- DELETE requests --------------------------------------------------------*/


}
