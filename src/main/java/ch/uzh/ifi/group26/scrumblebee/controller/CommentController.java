package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.Comment;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.*;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import ch.uzh.ifi.group26.scrumblebee.service.CommentService;
import ch.uzh.ifi.group26.scrumblebee.service.TaskService;
import ch.uzh.ifi.group26.scrumblebee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Comment Controller
 * This class is responsible for handling all REST request that are related to
 * the comment.
 * The controller will receive the request and delegate the execution to the
 * CommentService and finally return the result.
 */
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;


    /*------------------------------------- GET requests -----------------------------------------------------------*/

    /**
     * Type: GET
     * URL: /comments/:taskId
     * Body: commentId
     * @return list<Comment>
     */
    @GetMapping("/comments/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<CommentGetDTO> getAllCommentsToATask(@PathVariable("taskId")Long taskId) {
        Optional<Task> taskIdExistCheck = taskService.getTask(taskId);
        if (taskIdExistCheck.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task ID does not exist");
        }
        List<Comment> comments = commentService.getComments(taskId);
        List<CommentGetDTO> commentGetDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            commentGetDTOs.add(DTOMapper.INSTANCE.convertEntityToCommentGetDTO(comment));
        }
        return commentGetDTOs;
    }

    /*------------------------------------- POST requests ----------------------------------------------------------*/

    /**
     * Type: POST
     * URL: /comments
     * Body: authorId, content and belongingTask (id)
     * Protection: check if request is coming from the client (check for special token)
     * @return Comment
     */
    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CommentGetDTO createComment(@RequestBody CommentPostDTO commentPostDTO) {
        Comment commentInput = DTOMapper.INSTANCE.convertCommentPostDTOtoEntity(commentPostDTO);
        // check if the id's exist
        Optional<Task> foundTask = taskService.getTask(commentInput.getBelongingTask());
        Optional<User> foundUser = Optional.ofNullable(userService.getUser(commentInput.getAuthorId()));
        if ( foundTask.isEmpty() || foundUser.isEmpty() ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or Task ID do not exist.");
        }
        //create comment
        Comment createdComment = commentService.createComment(commentInput);
        //add comment to the comments List in the right task
        taskService.assignCommentToTask(commentInput);
        return DTOMapper.INSTANCE.convertEntityToCommentGetDTO(createdComment);
    }

    /*------------------------------------- PUT requests -----------------------------------------------------------*/


    /*------------------------------------- DELETE requests --------------------------------------------------------*/

    /**
     * Type: DELETE
     * URL: /comment/{commentId}
     * Body: commentId
     * Protection: check if request is coming from the client (check for special token)
     * @return void
     */
    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteComment(@PathVariable long commentId) {
        //get comment from ID
        Comment commentFromId = commentService.getComment(commentId);
        //delete comment by removing it from the comments-List in the task
        taskService.deleteComment(commentFromId);
    }

}
