package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.TaskMapper;
import ch.uzh.ifi.group26.scrumblebee.service.TaskService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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



    /*------------------------------------- POST requests ----------------------------------------------------------*/


    /*------------------------------------- PUT requests -----------------------------------------------------------*/

    /**
     * Type: PUT
     * URL: /
     * Body: username, name*, email, password
     * Protection: check if request is coming from the client (check for special token)
     * @return User
     */
    @PutMapping("/tasks/{taskId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public TaskGetDTO updateTask(@RequestBody TaskPostDTO taskPostDTO, @PathVariable long taskId) {
        Task changesTask = TaskMapper.INSTANCE.convertTaskPostDTOtoEntity(taskPostDTO);
        Task updatedTask = taskService.updateTask(taskId, changesTask);
        return TaskMapper.INSTANCE.convertEntityToTaskGetDTO(updatedTask);

    }
    /*------------------------------------- DELETE requests --------------------------------------------------------*/


}
