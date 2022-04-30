package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskPutDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import ch.uzh.ifi.group26.scrumblebee.service.TaskService;
import ch.uzh.ifi.group26.scrumblebee.service.UserService;
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
    private final UserService userService;

    TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }



    /*------------------------------------- GET requests -----------------------------------------------------------*/

    /**
     * Type: GET
     * URL: /tasks
     * Query parameter: show [active|completed] (optional)
     * Body: none
     * @return list<Task>
     */
    @GetMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TaskGetDTO> getAllTasks(@RequestParam(required = false) String show) {
        // If parameter "show" was specified, get only active or completed tasks.
        // Otherwise get all tasks.
        List<Task> tasks;
        if (show != null) {
            tasks = taskService.getTasks(show);
        }
        else {
            tasks = taskService.getTasks();
        }
        List<TaskGetDTO> taskGetDTOs = new ArrayList<>();
        for (Task task : tasks) {
            taskGetDTOs.add(DTOMapper.INSTANCE.convertEntityToTaskGetDTO(task));
        }
        return taskGetDTOs;
    }

    /**
     * Type: GET
     * URL: /tasks/{taskId}
     * Body: none
     * @return Task
     */
    @GetMapping("/tasks/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TaskGetDTO getTask(@PathVariable long taskId) {
        Task task = taskService.getTask(taskId);
        return DTOMapper.INSTANCE.convertEntityToTaskGetDTO(task);
    }

    /**
     * Type: GET
     * URL: /tasks/assignee/{userId}
     * Body: none
     * @return Task
     */
    @GetMapping("/tasks/assignee/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TaskGetDTO> getTasksForUser(@PathVariable long userId) {
        // check if userId is valid (getUser throws an exception otherwise)
        User user = userService.getUser(userId);

        List<Task> tasks = taskService.getTasksForUser(userId);
        List<TaskGetDTO> taskGetDTOs = new ArrayList<>();
        for (Task task : tasks) {
            taskGetDTOs.add(DTOMapper.INSTANCE.convertEntityToTaskGetDTO(task));
        }
        return taskGetDTOs;
    }

    /**
     * Type: GET
     * URL: /tasks/reporter/{userId}
     * Body: none
     * @return Task
     */
    @GetMapping("/tasks/reporter/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TaskGetDTO> getTasksToReportForUser(@PathVariable long userId) {
        // Check if userId is valid (getUser throws an exception otherwise)
        User user = userService.getUser(userId);

        List<Task> tasks = taskService.getTasksToReportForUser(userId);
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
     * Body: task details
     * Protection: check if request is coming from the client (check for special token)
     * @return Task
     */
    @PutMapping("/tasks/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TaskGetDTO updateTask(@RequestBody TaskPutDTO taskPutDTO, @PathVariable long taskId) {
        Task changesTask = DTOMapper.INSTANCE.convertTaskPutDTOtoEntity(taskPutDTO);
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
