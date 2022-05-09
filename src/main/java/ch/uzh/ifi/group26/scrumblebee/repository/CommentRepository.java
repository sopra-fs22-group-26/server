package ch.uzh.ifi.group26.scrumblebee.repository;

import ch.uzh.ifi.group26.scrumblebee.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("commentRepository")
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findCommentByCommentId(long commentId);
    List<Comment> findAllByBelongingTask(Long aLong);
    void deleteByCommentId(Long aLong);
}
