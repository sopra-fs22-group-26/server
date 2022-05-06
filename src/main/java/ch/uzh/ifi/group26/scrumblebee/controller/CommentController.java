package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.Comment;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.*;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import ch.uzh.ifi.group26.scrumblebee.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private final CommentService commentService;

    CommentController(CommentService commentService) { this.commentService = commentService; }



    /*------------------------------------- GET requests -----------------------------------------------------------*/

    /**
     * Type: GET
     * URL: /tasks/:taskId
     * Query parameter: show [active|completed] (optional)
     * Body: none
     * @return list<Task>
     */
    @GetMapping("/comments/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<CommentGetDTO> getAllCommentsToATask(@PathVariable("taskId")Long taskId) {
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
     * Body: dueDate, title, description, estimate, priority, status
     * Protection: check if request is coming from the client (check for special token)
     * @return Task
     */
    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CommentGetDTO createComment(@RequestBody CommentPostDTO commentPostDTO) {
        Comment commentInput = DTOMapper.INSTANCE.convertCommentPostDTOtoEntity(commentPostDTO);
        Comment createdComment = commentService.createComment(commentInput);
        return DTOMapper.INSTANCE.convertEntityToCommentGetDTO(createdComment);
    }

    /*------------------------------------- PUT requests -----------------------------------------------------------*/

    /**
     * Type: PUT
     * URL: /tasks/{taskId}
     * Body: username, name*, email, password
     * Protection: check if request is coming from the client (check for special token)
     * @return User
     */


    /*------------------------------------- DELETE requests --------------------------------------------------------*/

    /**
     * Type: DELETE
     * URL: /tasks/{taskId}
     * Body: username, name*, email, password
     * Protection: check if request is coming from the client (check for special token)
     * @return User
     */
    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteComment(@PathVariable long commentId) {
        commentService.deleteComment(commentId);
    }

}
