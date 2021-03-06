package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskPostDTO;
import ch.uzh.ifi.group26.scrumblebee.security.utils.JwtUtils;
import ch.uzh.ifi.group26.scrumblebee.service.TaskService;
import ch.uzh.ifi.group26.scrumblebee.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest
class TaskControllerTest {

    private MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    @MockBean
    UserService userService;

    @MockBean
    JwtUtils jwtUtils;

    @Autowired
    private WebApplicationContext context;

    private static final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    Task task1 = new Task();
    Task task2 = new Task();
    User testUser = new User();

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        // init first task
        task1.setTaskId(1L);
        task1.setDueDate(dateFormat.parse("1992-11-19"));
        task1.setTitle("Some@task");
        task1.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam " +
                "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        task1.setEstimate(4);
        task1.setPriority(TaskPriority.HIGH);
        task1.setStatus(TaskStatus.ACTIVE);

        // init second task
        task2.setTaskId(2L);
        task2.setDueDate(dateFormat.parse("1992-11-20"));
        task2.setTitle("SomeSecond@task");
        task2.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus " +
                "est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam " +
                "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        task2.setEstimate(8);
        task2.setPriority(TaskPriority.MEDIUM);
        task2.setStatus(TaskStatus.ACTIVE);
    }

    /*--------------------------------------------- GET ----------------------------------------------------------*/

    /**
     * type: GET
     * url: /tasks
     * INPUT: valid, returns 0 users
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getAllTasks_zeroTasks_thenReturnJsonArray() throws Exception {

        List<Task> allTasks = new ArrayList<>();

        given(taskService.getTasks(9999L)).willReturn(allTasks);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    /**
     * type: GET
     * url: /tasks
     * INPUT: valid, returns 1 task
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getAllTasks_oneTasks_thenReturnJsonArray() throws Exception {

        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);

        given(jwtUtils.extractUsername(anyString())).willReturn("correctUsername");
        given(userService.getUserIdFromUsername(anyString())).willReturn(0L);
        given(taskService.getTasks(0L)).willReturn(allTasks);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/tasks")
                .header("Authorization", "my-token")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].taskId").value(task1.getTaskId()))
                .andExpect(jsonPath("$[0].dueDate").value(dateFormat.format(task1.getDueDate())))
                .andExpect(jsonPath("$[0].title").value(task1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(task1.getDescription()))
                .andExpect(jsonPath("$[0].estimate").value(task1.getEstimate()))
                .andExpect(jsonPath("$[0].priority").value(task1.getPriority().toString()))
                .andExpect(jsonPath("$[0].status").value(task1.getStatus().toString()));

    }

    /**
     * type: GET
     * url: /tasks
     * INPUT: valid, returns 2 task
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getAllTasks_multipleTasks_thenReturnJsonArray() throws Exception {

        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.add(task2);

        given(jwtUtils.extractUsername(anyString())).willReturn("correctUsername");
        given(userService.getUserIdFromUsername(anyString())).willReturn(0L);
        given(taskService.getTasks(0L)).willReturn(allTasks);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/tasks")
                .header("Authorization", "my-token")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].taskId").value(task1.getTaskId()))
                .andExpect(jsonPath("$[0].dueDate").value(dateFormat.format(task1.getDueDate())))
                .andExpect(jsonPath("$[0].title").value(task1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(task1.getDescription()))
                .andExpect(jsonPath("$[0].estimate").value(task1.getEstimate()))
                .andExpect(jsonPath("$[0].priority").value(task1.getPriority().toString()))
                .andExpect(jsonPath("$[0].status").value(task1.getStatus().toString()))
                .andExpect(jsonPath("$[1].taskId").value(task2.getTaskId()))
                .andExpect(jsonPath("$[1].dueDate").value(dateFormat.format(task2.getDueDate())))
                .andExpect(jsonPath("$[1].title").value(task2.getTitle()))
                .andExpect(jsonPath("$[1].description").value(task2.getDescription()))
                .andExpect(jsonPath("$[1].estimate").value(task2.getEstimate()))
                .andExpect(jsonPath("$[1].priority").value(task2.getPriority().toString()))
                .andExpect(jsonPath("$[1].status").value(task2.getStatus().toString()));
    }

    /**
     * type: GET
     * url: /tasks
     * INPUT: valid, returns 0 users
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getActiveTasks_zeroTasks_thenReturnJsonArray() throws Exception {

        List<Task> allTasks = new ArrayList<>();

        given(taskService.getTasks("active", 9999L)).willReturn(allTasks);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users?show=active")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    /**
     * type: GET
     * url: /tasks
     * INPUT: valid, returns 1 task
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getActiveTasks_oneTasks_thenReturnJsonArray() throws Exception {

        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);

        given(jwtUtils.extractUsername(anyString())).willReturn("correctUsername");
        given(userService.getUserIdFromUsername(anyString())).willReturn(0L);
        given(taskService.getTasks("active", 0L)).willReturn(allTasks);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/tasks?show=active")
                .header("Authorization", "my-token")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].taskId").value(task1.getTaskId()))
                .andExpect(jsonPath("$[0].dueDate").value(dateFormat.format(task1.getDueDate())))
                .andExpect(jsonPath("$[0].title").value(task1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(task1.getDescription()))
                .andExpect(jsonPath("$[0].estimate").value(task1.getEstimate()))
                .andExpect(jsonPath("$[0].priority").value(task1.getPriority().toString()))
                .andExpect(jsonPath("$[0].status").value(task1.getStatus().toString()));

    }

    /**
     * type: GET
     * url: /tasks
     * INPUT: valid, returns 2 task
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getActiveTasks_multipleTasks_thenReturnJsonArray() throws Exception {

        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.add(task2);

        given(jwtUtils.extractUsername(anyString())).willReturn("correctUsername");
        given(userService.getUserIdFromUsername(anyString())).willReturn(0L);
        given(taskService.getTasks("active", 0L)).willReturn(allTasks);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/tasks?show=active")
                .header("Authorization", "my-token")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].taskId").value(task1.getTaskId()))
                .andExpect(jsonPath("$[0].dueDate").value(dateFormat.format(task1.getDueDate())))
                .andExpect(jsonPath("$[0].title").value(task1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(task1.getDescription()))
                .andExpect(jsonPath("$[0].estimate").value(task1.getEstimate()))
                .andExpect(jsonPath("$[0].priority").value(task1.getPriority().toString()))
                .andExpect(jsonPath("$[0].status").value(task1.getStatus().toString()))
                .andExpect(jsonPath("$[1].taskId").value(task2.getTaskId()))
                .andExpect(jsonPath("$[1].dueDate").value(dateFormat.format(task2.getDueDate())))
                .andExpect(jsonPath("$[1].title").value(task2.getTitle()))
                .andExpect(jsonPath("$[1].description").value(task2.getDescription()))
                .andExpect(jsonPath("$[1].estimate").value(task2.getEstimate()))
                .andExpect(jsonPath("$[1].priority").value(task2.getPriority().toString()))
                .andExpect(jsonPath("$[1].status").value(task2.getStatus().toString()));
    }

    /**
     * type: GET
     * url: /tasks
     * INPUT: valid, returns 0 users
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getCompletedTasks_zeroTasks_thenReturnJsonArray() throws Exception {

        List<Task> allTasks = new ArrayList<>();

        given(taskService.getTasks("completed", 9999L)).willReturn(allTasks);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users?show=completed")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    /**
     * type: GET
     * url: /tasks
     * INPUT: valid, returns 1 task
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getCompletedTasks_oneTasks_thenReturnJsonArray() throws Exception {

        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);

        given(jwtUtils.extractUsername(anyString())).willReturn("correctUsername");
        given(userService.getUserIdFromUsername(anyString())).willReturn(0L);
        given(taskService.getTasks("completed", 0L)).willReturn(allTasks);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/tasks?show=completed")
                .header("Authorization", "my-token")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].taskId").value(task1.getTaskId()))
                .andExpect(jsonPath("$[0].dueDate").value(dateFormat.format(task1.getDueDate())))
                .andExpect(jsonPath("$[0].title").value(task1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(task1.getDescription()))
                .andExpect(jsonPath("$[0].estimate").value(task1.getEstimate()))
                .andExpect(jsonPath("$[0].priority").value(task1.getPriority().toString()))
                .andExpect(jsonPath("$[0].status").value(task1.getStatus().toString()));

    }

    /**
     * type: GET
     * url: /tasks
     * INPUT: valid, returns 2 task
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getCompletedTasks_multipleTasks_thenReturnJsonArray() throws Exception {

        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.add(task2);

        given(jwtUtils.extractUsername(anyString())).willReturn("correctUsername");
        given(userService.getUserIdFromUsername(anyString())).willReturn(0L);
        given(taskService.getTasks("completed", 0L)).willReturn(allTasks);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/tasks?show=completed")
                .header("Authorization", "my-token")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].taskId").value(task1.getTaskId()))
                .andExpect(jsonPath("$[0].dueDate").value(dateFormat.format(task1.getDueDate())))
                .andExpect(jsonPath("$[0].title").value(task1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(task1.getDescription()))
                .andExpect(jsonPath("$[0].estimate").value(task1.getEstimate()))
                .andExpect(jsonPath("$[0].priority").value(task1.getPriority().toString()))
                .andExpect(jsonPath("$[0].status").value(task1.getStatus().toString()))
                .andExpect(jsonPath("$[1].taskId").value(task2.getTaskId()))
                .andExpect(jsonPath("$[1].dueDate").value(dateFormat.format(task2.getDueDate())))
                .andExpect(jsonPath("$[1].title").value(task2.getTitle()))
                .andExpect(jsonPath("$[1].description").value(task2.getDescription()))
                .andExpect(jsonPath("$[1].estimate").value(task2.getEstimate()))
                .andExpect(jsonPath("$[1].priority").value(task2.getPriority().toString()))
                .andExpect(jsonPath("$[1].status").value(task2.getStatus().toString()));
    }




    /**
     * type: GET
     * URL: /tasks/{taskId}
     * INPUT: valid
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getTaskById_validInput_returnUserWithId() throws Exception {

        given(jwtUtils.extractUsername(anyString())).willReturn("correctUsername");
        given(userService.getUserIdFromUsername(anyString())).willReturn(0L);
        given(taskService.getTask(task1.getTaskId(),0L)).willReturn(Optional.ofNullable(task1));

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get(
                        "/tasks/{taskId}", task1.getTaskId())
                .header("Authorization", "my-token")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(task1.getTaskId()))
                .andExpect(jsonPath("$.dueDate").value(dateFormat.format(task1.getDueDate())))
                .andExpect(jsonPath("$.title").value(task1.getTitle()))
                .andExpect(jsonPath("$.description").value(task1.getDescription()))
                .andExpect(jsonPath("$.estimate").value(task1.getEstimate()))
                .andExpect(jsonPath("$.priority").value(task1.getPriority().toString()))
                .andExpect(jsonPath("$.status").value(task1.getStatus().toString()));

    }

    /**
     * type: GET
     * URL: /tasks/{taskId}
     * INPUT: invalid
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getTaskById_invalidInput_return404() throws Exception {

        given(jwtUtils.extractUsername(anyString())).willReturn("correctUsername");
        given(userService.getUserIdFromUsername(anyString())).willReturn(0L);
        given(taskService.getTask(anyLong(), anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get(
                        "/tasks/{taskId}", 999L)
                .header("Authorization", "my-token")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }


    /*--------------------------------------------- POST --------------------------------------------------------*/


    /**
     * type: POST
     * url: /tasks
     * INPUT: valid
     * @throws Exception
     */
    @Test
    @WithMockUser
    void createTask_validInput_taskCreated() throws Exception {

        TaskPostDTO taskPostDTO = new TaskPostDTO();
        taskPostDTO.setTaskId(1L);
        taskPostDTO.setDueDate(dateFormat.parse("1992-11-19"));
        taskPostDTO.setTitle("Some@task");
        taskPostDTO.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus " +
                "est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam " +
                "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        taskPostDTO.setEstimate(4);
        taskPostDTO.setPriority("HIGH");
        taskPostDTO.setStatus("ACTIVE");

        given(taskService.createTask(any())).willReturn(task1);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId").value(task1.getTaskId()))
                .andExpect(jsonPath("$.dueDate").value(dateFormat.format(task1.getDueDate())))
                .andExpect(jsonPath("$.title").value(task1.getTitle()))
                .andExpect(jsonPath("$.description").value(task1.getDescription()))
                .andExpect(jsonPath("$.estimate").value(task1.getEstimate()))
                .andExpect(jsonPath("$.priority").value(task1.getPriority().toString()))
                .andExpect(jsonPath("$.status").value(task1.getStatus().toString()));

    }


    /*----------------------------------------------- PUT --------------------------------------------------------*/


    /**
     * type: PUT
     * url: /tasks/{id}
     * INPUT: valid
     * @throws Exception
     */
    @Test
    @WithMockUser
    void updateTask_validInput_taskUpdated() throws Exception {

        TaskPostDTO taskPostDTO = new TaskPostDTO();
        taskPostDTO.setDueDate(dateFormat.parse("1980-11-19"));
        taskPostDTO.setTitle("SomeChanged@task");
        taskPostDTO.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam " +
                "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        taskPostDTO.setEstimate(8);
        taskPostDTO.setPriority("LOW");
        taskPostDTO.setScore(4);
        taskPostDTO.setStatus("REPORTED");

        Task updatedTask = new Task();
        updatedTask.setTaskId(1L);
        updatedTask.setDueDate(dateFormat.parse("1980-11-19"));
        updatedTask.setTitle("SomeChanged@task");
        updatedTask.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam " +
                "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        updatedTask.setEstimate(8);
        updatedTask.setPriority(TaskPriority.LOW);
        updatedTask.setScore(4);
        updatedTask.setStatus(TaskStatus.REPORTED);

        when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(updatedTask);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put(
                        "/tasks/{taskId}", task1.getTaskId().intValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskPostDTO));

        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(updatedTask.getTaskId()))
                .andExpect(jsonPath("$.dueDate").value(dateFormat.format(updatedTask.getDueDate())))
                .andExpect(jsonPath("$.title").value(updatedTask.getTitle()))
                .andExpect(jsonPath("$.description").value(updatedTask.getDescription()))
                .andExpect(jsonPath("$.estimate").value(updatedTask.getEstimate()))
                .andExpect(jsonPath("$.priority").value(updatedTask.getPriority().toString()))
                .andExpect(jsonPath("$.status").value(updatedTask.getStatus().toString()));

    }

    /**
     * type: PUT
     * url: /tasks/{id}
     * INPUT: invalid
     * @throws Exception
     */
    /*
    @Test
    public void updateTask_invalidInput_return404() throws Exception {
        TaskPostDTO taskPostDTO = new TaskPostDTO();
        taskPostDTO.setTitle("any@title");


        given(taskService.checkIfTaskIdExist(anyLong()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/tasks/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskPostDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }

     */


    /*----------------------------------------------- DELETE ------------------------------------------------------*/


    /**
     * type: DELETE
     * url: /tasks/{id}
     * INPUT: valid
     * @throws Exception
     */
    @Test
    @WithMockUser
    void deleteTask_validInput_returnDeletedTask() throws Exception {

        given(taskService.deleteTask(task1.getTaskId())).willReturn(task1);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/tasks/{taskId}", task1.getTaskId().intValue())
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(deleteRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(task1.getTaskId()))
                .andExpect(jsonPath("$.dueDate").value(dateFormat.format(task1.getDueDate())))
                .andExpect(jsonPath("$.title").value(task1.getTitle()))
                .andExpect(jsonPath("$.description").value(task1.getDescription()))
                .andExpect(jsonPath("$.estimate").value(task1.getEstimate()))
                .andExpect(jsonPath("$.priority").value(task1.getPriority().toString()))
                .andExpect(jsonPath("$.status").value(task1.getStatus().toString()));
    }


    /**
     * type: DELETE
     * url: /tasks/{id}
     * INPUT: valid
     * @throws Exception
     */
    @Test
    @WithMockUser
    void deleteTask_invalidInput_return404() throws Exception {

        when(taskService.deleteTask(anyLong()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/tasks/{taskId}", anyInt())
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(deleteRequest)
                .andExpect(status().isNotFound());
    }

    /**
     * type: GET
     * url: /tasks/assignee/{userId}
     * INPUT: valid
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getTasksForUser_success() throws Exception {

        when(userService.getUser(anyLong())).thenReturn(testUser);
        List<Task> tasksFound = new ArrayList<>();
        task1.setAssignee(1L);
        tasksFound.add(task1);
        task2.setAssignee(1L);
        tasksFound.add(task2);

        when(taskService.getTasksForUser(anyLong(), anyLong())).thenReturn(tasksFound);

        given(jwtUtils.extractUsername(anyString())).willReturn("correctUsername");
        given(userService.getUserIdFromUsername(anyString())).willReturn(0L);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders.get("/tasks/assignee/{userId}", anyInt())
                        .header("Authorization", "my-token")
                        .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assignee").value(1L))
                .andExpect(jsonPath("$[1].assignee").value(1L));
    }

    /**
     * type: GET
     * url: /tasks/assignee/{userId}
     * INPUT: invalid
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getTasksForUser_fail() throws Exception {

        when(userService.getUser(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        given(jwtUtils.extractUsername(anyString())).willReturn("correctUsername");
        given(userService.getUserIdFromUsername(anyString())).willReturn(0L);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders.get("/tasks/assignee/{userId}", anyInt())
                        .header("Authorization", "my-token")
                        .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    /**
     * type: GET
     * url: /tasks/reporter/{userId}
     * INPUT: valid
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getTasksToReportForUser_success() throws Exception {

        when(userService.getUser(anyLong())).thenReturn(testUser);
        List<Task> tasksFound = new ArrayList<>();
        task1.setReporter(1L);
        tasksFound.add(task1);
        task2.setReporter(1L);
        tasksFound.add(task2);
        when(taskService.getTasksToReportForUser(anyLong())).thenReturn(tasksFound);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders.get("/tasks/reporter/{userId}", anyInt())
                        .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reporter").value(1L))
                .andExpect(jsonPath("$[1].reporter").value(1L));
    }

    /**
     * type: GET
     * url: /tasks/reporter/{userId}
     * INPUT: invalid
     * @throws Exception
     */
    @Test
    @WithMockUser
    void getTasksToReportForUser_fail() throws Exception {

        when(userService.getUser(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders.get("/tasks/reporter/{userId}", anyInt())
                        .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }


    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
