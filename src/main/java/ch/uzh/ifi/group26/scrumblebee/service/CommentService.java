package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.Comment;
import ch.uzh.ifi.group26.scrumblebee.repository.CommentRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.TaskRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


/**
 * Comment Service
 * This class is the "worker" and responsible for all functionality related to
 * the comment
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class CommentService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Return a list of all comments.
     * @return List<Comment>
     */
    public List<Comment> getComments(Long id) {
        return this.commentRepository.findAllByBelongingTask(id);
    }


    /**
     * Return a single comment by ID if this comment exists, null otherwise.
     * @param commentID
     * @return
     */
    public Comment getComment(long commentID) {
        String baseErrorMessage = "Error: No user found with userID %d!";
        return commentRepository.findById(commentID).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, commentID)));
    }

    /**
     * Used by: POST /comments
     * @param newComment
     * @return the created Comment
     */
    public Comment createComment(Comment newComment) {

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newComment = commentRepository.save(newComment);
        commentRepository.flush();

        log.debug("Created Information for Task: {}", newComment);
        return newComment;
    }

}