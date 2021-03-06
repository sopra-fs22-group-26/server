package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.Comment;
import ch.uzh.ifi.group26.scrumblebee.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


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

    @Autowired
    private CommentRepository commentRepository;


    /**
     * Return a single comment by ID if this comment exists, null otherwise.
     * @param commentID
     * @return
     */
    public Comment getComment(long commentID) {
        String baseErrorMessage = "Error: No comment found with commentID %d!";
        return commentRepository.findById(commentID).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, commentID)));
    }

    /**
     * Used by: POST /comments
     * @param newComment
     * @return the created Comment
     */
    public Comment createComment(Comment newComment) {

        java.util.Date creationDate = java.util.Calendar.getInstance().getTime();
        newComment.setCreationDate(creationDate);

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newComment = commentRepository.save(newComment);
        commentRepository.flush();

        return newComment;
    }

}