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
        Comment createdComment = commentService.createComment(comment1);

        // ASSERTIONS
        verify(commentRepository, times(1)).save(any());
        assertEquals(comment1, createdComment);

    }

}
