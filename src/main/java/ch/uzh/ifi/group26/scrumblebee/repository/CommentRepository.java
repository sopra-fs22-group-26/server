package ch.uzh.ifi.group26.scrumblebee.repository;

import ch.uzh.ifi.group26.scrumblebee.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("commentRepository")
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findCommentByCommentId(long commentId);
}
