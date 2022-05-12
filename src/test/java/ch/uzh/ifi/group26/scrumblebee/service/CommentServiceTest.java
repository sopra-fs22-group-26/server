package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.Comment;
import ch.uzh.ifi.group26.scrumblebee.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    Comment comment1 = new Comment();
    Comment comment2 = new Comment();
    Comment comment3 = new Comment();

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);

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
     * METHOD TESTED: getComments()
     * INPUT: valid
     * EXPECTED RESULT: no comments should be returned
     */
    @Test
    public void getComments_zeroComments_success() {

        // STUBBING
        List<Comment> allComments = new ArrayList<>();
        when(commentRepository.findAllByBelongingTask(anyLong())).thenReturn(allComments);

        // EXERCISE
        List<Comment> foundComments = commentService.getComments(anyLong());

        // ASSERTIONS
        verify(commentRepository, times(1)).findAllByBelongingTask(anyLong());
        assertTrue(foundComments.isEmpty());

    }

    /**
     * METHOD TESTED: getComments()
     * INPUT: valid
     * EXPECTED RESULT: one comment should be returned
     */
    @Test
    public void getComments_oneComment_success() {

        // STUBBING
        List<Comment> allComments = new ArrayList<>();
        allComments.add(comment1);
        when(commentRepository.findAllByBelongingTask(anyLong())).thenReturn(allComments);

        // EXERCISE
        List<Comment> foundComments = commentService.getComments(anyLong());

        // ASSERTIONS
        verify(commentRepository, times(1)).findAllByBelongingTask(anyLong());
        assertEquals(1, foundComments.size());
        assertEquals(allComments, foundComments);

    }

    /**
     * METHOD TESTED: getComments()
     * INPUT: valid
     * EXPECTED RESULT: multiple comments should be returned
     */
    @Test
    public void getComments_multipleComments_success() {

        // STUBBING
        List<Comment> allComments = new ArrayList<>();
        allComments.add(comment1);
        allComments.add(comment2);
        allComments.add(comment3);
        when(commentRepository.findAllByBelongingTask(anyLong())).thenReturn(allComments);

        // EXERCISE
        List<Comment> foundComments = commentService.getComments(anyLong());

        // ASSERTIONS
        verify(commentRepository, times(1)).findAllByBelongingTask(anyLong());
        assertEquals(allComments, foundComments);

    }

    /**
     * METHOD TESTED: getComment()
     * INPUT: valid
     * EXPECTED RESULT: return the comment
     */
    @Test
    public void getComment_success() {

        // STUBBING
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment1));

        // EXERCISE
        Comment foundComment = commentService.getComment(anyLong());

        // ASSERTIONS
        verify(commentRepository, times(1)).findById(anyLong());
        assertEquals(comment1, foundComment);

    }

    /**
     * METHOD TESTED: getComment()
     * INPUT: invalid
     * EXPECTED RESULT: throw a ResponseStatusException
     */
    @Test
    public void getComment_fail() {

        // STUBBING
        when(commentRepository.findById(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // ASSERTIONS
        assertThrows(ResponseStatusException.class, ()->{
           // EXERCISE
           commentService.getComment(anyLong());
        });
        verify(commentRepository, times(1)).findById(anyLong());

    }

    /**
     * METHOD TESTED: createComment()
     * INPUT: valid
     * EXPECTED RESULT: return the created comment, commentRepository.save() must only be invoked once
     */
    @Test
    public void createComment_succes() {

        // STUBBING
        when(commentRepository.save(any())).thenReturn(comment1);

        // EXERCISE
        Comment createdComment = commentService.createComment(any());

        // ASSERTIONS
        verify(commentRepository, times(1)).save(any());
        assertEquals(comment1, createdComment);

    }

}
