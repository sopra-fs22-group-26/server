package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.Comment;
import ch.uzh.ifi.group26.scrumblebee.repository.CommentRepository;
import com.sun.istack.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@SpringBootTest
public class CommentServiceIntegrationTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    Comment testComment = new Comment();

    @BeforeEach
    public void setup() {
        commentRepository.deleteAll();
        testComment.setCreationDate(null);
        testComment.setContent("Anything written here, which could be interesting for a task");
        testComment.setAuthorId(2L);
        testComment.setBelongingTask(1L);
    }


    /**
     * METHOD TESTED: getComment()
     * INPUT: valid
     * EXPECTED RESULT: return the comment
     */
    @Test
    public void integrationTest_getComment_success() {

        assertTrue(commentRepository.findAll().isEmpty());
        testComment.setCreationDate(java.util.Calendar.getInstance().getTime());
        Comment savedComment = commentRepository.save(testComment);

        // EXERCISE
        Comment foundComment = commentService.getComment(savedComment.getCommentId());

        // ASSERTIONS
        checkCreatedCommentEquals(savedComment, foundComment);

    }

    /**
     * METHOD TESTED: getComment()
     * INPUT: invalid
     * EXPECTED RESULT: throw a ResponseStatusException
     */
    @Test
    public void integrationTest_getComment_fail() {

        assertTrue(commentRepository.findAll().isEmpty());

        // ASSERTIONS
        assertThrows(ResponseStatusException.class, ()->{
            // EXERCISE
            commentService.getComment(999L);
        });

    }

    /**
     * METHOD TESTED: createComment()
     * INPUT: valid
     * EXPECTED RESULT: return the created comment, commentRepository.save() must only be invoked once
     */
    @Test
    public void integrationTest_createComment_succes() {
        assertTrue(commentRepository.findAll().isEmpty());
        // EXERCISE
        Comment createdComment = commentService.createComment(testComment);
        // ASSERTIONS
        checkCreatedCommentEquals(testComment, createdComment);
    }

    /**
     * Helps to verify if a created comment is correct
     * @param expected
     * @param actual
     */
    private void checkCreatedCommentEquals(@NotNull Comment expected, @NotNull Comment actual) {
        assertNotNull(actual.getCommentId());
        assertNotNull(actual.getCreationDate());
        assertEquals(expected.getAuthorId(), actual.getAuthorId());
        assertEquals(expected.getBelongingTask(), actual.getBelongingTask());
        assertEquals(expected.getContent(), actual.getContent());
    }

}
