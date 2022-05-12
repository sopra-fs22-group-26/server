package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.Comment;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.CommentPostDTO;
import ch.uzh.ifi.group26.scrumblebee.service.CommentService;
import ch.uzh.ifi.group26.scrumblebee.service.TaskService;
import ch.uzh.ifi.group26.scrumblebee.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest
public class CommentControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    CommentService commentService;

    @MockBean
    TaskService taskService;

    @MockBean
    UserService userService;

    Comment comment1 = new Comment();
    Comment comment2 = new Comment();
    Comment comment3 = new Comment();

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        comment1.setCommentId(1L);
        comment1.setContent("Anything written here, which could be interesting for a task");
        comment1.setAuthorId(2L);
        comment1.setBelongingTask(1L);

        comment2.setCommentId(2L);
        comment2.setContent("Bliblablub and I wrote a comment to this task.");
        comment2.setAuthorId(1L);
        comment2.setBelongingTask(1L);

        comment3.setCommentId(3L);
        comment3.setContent("I am trying to contribute interesting information to this task");
        comment3.setAuthorId(3L);
        comment3.setBelongingTask(1L);

    }

    /**
     * Type: GET
     * URL: /comments/:taskId
     * INPUT: valid
     */
    @Test
    public void getAllCommentsToATask_zeroComments_success() throws Exception {

        // STUBBING
        List<Comment> allCommentsPerTask = new ArrayList<>();
        when(commentService.getComments(anyLong())).thenReturn(allCommentsPerTask);
        Task foundTask = new Task();
        foundTask.setTaskId(1L);
        when(taskService.getTask(anyLong())).thenReturn(Optional.of(foundTask));

        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/comments/{taskId}", anyInt())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

    }

    /**
     * Type: GET
     * URL: /comments/:taskId
     * INPUT: valid
     */
    @Test
    public void getAllCommentsToATask_oneComment_success() throws Exception {

        // STUBBING
        List<Comment> allCommentsPerTask = new ArrayList<>();
        allCommentsPerTask.add(comment1);
        Assertions.assertEquals(1, allCommentsPerTask.size());
        when(commentService.getComments(anyLong())).thenReturn(allCommentsPerTask);
        Task foundTask = new Task();
        foundTask.setTaskId(1L);
        when(taskService.getTask(anyLong())).thenReturn(Optional.of(foundTask));

        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/comments/{taskId}", anyInt())
                .contentType(MediaType.APPLICATION_JSON);

        // PERFORM REQUEST AND ASSERT
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].commentId")
                        .value(allCommentsPerTask.get(0).getCommentId().intValue()))
                .andExpect(jsonPath("$[0].content")
                        .value(allCommentsPerTask.get(0).getContent()))
                .andExpect(jsonPath("$.[0].authorId")
                        .value(allCommentsPerTask.get(0).getAuthorId().intValue()))
                .andExpect(jsonPath("$[0].belongingTask")
                        .value(allCommentsPerTask.get(0).getBelongingTask().intValue()));

    }

    /**
     * Type: GET
     * URL: /comments/:taskId
     * INPUT: valid
     */
    @Test
    public void getAllCommentsToATask_multipleComments_success() throws Exception {

        // STUBBING
        List<Comment> allCommentsPerTask = new ArrayList<>();
        allCommentsPerTask.add(comment1);
        allCommentsPerTask.add(comment2);
        allCommentsPerTask.add(comment3);
        Assertions.assertEquals(3, allCommentsPerTask.size());
        when(commentService.getComments(anyLong())).thenReturn(allCommentsPerTask);
        Task foundTask = new Task();
        foundTask.setTaskId(1L);
        when(taskService.getTask(anyLong())).thenReturn(Optional.of(foundTask));

        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/comments/{taskId}", anyInt())
                .contentType(MediaType.APPLICATION_JSON);

        // PERFORM REQUEST AND ASSERT
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].commentId")
                        .value(allCommentsPerTask.get(0).getCommentId().intValue()))
                .andExpect(jsonPath("$[0].content")
                        .value(allCommentsPerTask.get(0).getContent()))
                .andExpect(jsonPath("$.[0].authorId")
                        .value(allCommentsPerTask.get(0).getAuthorId().intValue()))
                .andExpect(jsonPath("$[0].belongingTask")
                        .value(allCommentsPerTask.get(0).getBelongingTask().intValue()))
                .andExpect(jsonPath("$[1].commentId")
                        .value(allCommentsPerTask.get(1).getCommentId().intValue()))
                .andExpect(jsonPath("$[1].content")
                        .value(allCommentsPerTask.get(1).getContent()))
                .andExpect(jsonPath("$.[1].authorId")
                        .value(allCommentsPerTask.get(1).getAuthorId().intValue()))
                .andExpect(jsonPath("$[1].belongingTask")
                        .value(allCommentsPerTask.get(1).getBelongingTask().intValue()))
                .andExpect(jsonPath("$[2].commentId")
                        .value(allCommentsPerTask.get(2).getCommentId().intValue()))
                .andExpect(jsonPath("$[2].content")
                        .value(allCommentsPerTask.get(2).getContent()))
                .andExpect(jsonPath("$.[2].authorId")
                        .value(allCommentsPerTask.get(2).getAuthorId().intValue()))
                .andExpect(jsonPath("$[2].belongingTask")
                        .value(allCommentsPerTask.get(2).getBelongingTask().intValue()));

    }

    /**
     * Type: GET
     * URL: /comments/:taskId
     * INPUT: invalid
     */
    @Test
    public void getAllCommentsToATask_taskDoesNotExist_fail() throws Exception {

        // STUBBING
        when(taskService.getTask(anyLong())).thenReturn(Optional.empty());

        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/comments/{taskId}", anyInt())
                .contentType(MediaType.APPLICATION_JSON);

        // PERFORM AND ASSERT
        mockMvc.perform(getRequest).andExpect(status().isNotFound());

    }

    /**
     * Type: POST
     * URL: /comments/
     * INPUT: valid
     */
    @Test
    public void createComment_success() throws Exception {

        // STUBBING
        CommentPostDTO input = new CommentPostDTO();
        input.setAuthorId(comment1.getAuthorId());
        input.setCommentId(comment1.getCommentId());
        input.setContent(comment1.getContent());
        input.setBelongingTask(comment1.getBelongingTask());

        when(commentService.createComment(Mockito.any(Comment.class))).thenReturn(comment1);
        Task foundTask = new Task();
        foundTask.setTaskId(1L);
        User foundUser = new User();
        foundUser.setId(1L);
        when(taskService.getTask(anyLong())).thenReturn(Optional.of(foundTask));
        when(userService.getUser(anyLong())).thenReturn(foundUser);

        // BUILD REQUEST
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(input));

        // PERFORM AND ASSERT
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.authorId").value(input.getAuthorId().intValue()))
                .andExpect(jsonPath("$.belongingTask").value(input.getBelongingTask().intValue()))
                .andExpect(jsonPath("$.commentId").value(input.getCommentId().intValue()))
                .andExpect(jsonPath("$.content").value(input.getContent()));

    }

    /**
     * Type: POST
     * URL: /comments/
     * INPUT: invalid
     */
    @Test
    public void createComment_wrongAuthorId_fail() throws Exception {

        // STUBBING
        CommentPostDTO input = new CommentPostDTO();
        input.setAuthorId(9999L);
        input.setCommentId(comment1.getCommentId());
        input.setContent(comment1.getContent());
        input.setBelongingTask(comment1.getBelongingTask());

        when(commentService.createComment(Mockito.any(Comment.class))).thenReturn(comment1);
        Task foundTask = new Task();
        foundTask.setTaskId(9999L);
        when(taskService.getTask(anyLong())).thenReturn(Optional.of(foundTask));
        when(userService.getUser(anyLong())).thenReturn(null);

        // BUILD REQUEST
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(input));

        // PERFORM AND ASSERT
        mockMvc.perform(postRequest).andExpect(status().isNotFound());

    }

    /**
     * Type: POST
     * URL: /comments/
     * INPUT: invalid
     */
    @Test
    public void createComment_wrongBelongingTaskId_fail() throws Exception {

        // STUBBING
        CommentPostDTO input = new CommentPostDTO();
        input.setAuthorId(comment1.getAuthorId());
        input.setCommentId(comment1.getCommentId());
        input.setContent(comment1.getContent());
        input.setBelongingTask(9999L);

        when(commentService.createComment(Mockito.any(Comment.class))).thenReturn(comment1);
        User foundUser = new User();
        foundUser.setId(9999L);
        when(taskService.getTask(anyLong())).thenReturn(Optional.empty());
        when(userService.getUser(anyLong())).thenReturn(foundUser);

        // BUILD REQUEST
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(input));

        // PERFORM AND ASSERT
        mockMvc.perform(postRequest).andExpect(status().isNotFound());

    }

    /**
     * Type: DELETE
     * URL: /comments/:commentId
     * INPUT: valid
     */
    @Test
    public void deleteComment_success() throws Exception {

        // STUBBING
        when(commentService.getComment(Mockito.anyLong())).thenReturn(comment1);

        // BUILD REQUEST
        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders.delete("/comments/{commentId}", comment1.getCommentId().intValue())
                .contentType(MediaType.APPLICATION_JSON);

        // PERFORM AND ASSERT
        mockMvc.perform(postRequest).andExpect(status().isNoContent());

    }

    /**
     * Type: DELETE
     * URL: /comments/:commentId
     * INPUT: invalid
     */
    @Test
    public void deleteComment_fail() throws Exception {

        // STUBBING
        when(commentService.getComment(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // BUILD REQUEST
        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders.delete("/comments/{commentId}", comment1.getCommentId().intValue())
                        .contentType(MediaType.APPLICATION_JSON);

        // PERFORM AND ASSERT
        mockMvc.perform(postRequest).andExpect(status().isNotFound());

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
