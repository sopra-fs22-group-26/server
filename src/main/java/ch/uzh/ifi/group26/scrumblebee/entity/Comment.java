package ch.uzh.ifi.group26.scrumblebee.entity;


import javax.persistence.*;
import java.io.Serializable;


/**
 * Internal Comment representation
 * This class composes the internal representation of a comment and defines
 * how a comment is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes
 * the primary key.
 */
@Entity
@Table(name = "sb_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false)
    private Long belongingTask;


    /**
     * Getter & setter methods
     */

    public void setCommentId(Long commentId) {this.commentId = commentId;}

    public Long getCommentId() { return commentId; }

    public void setContent(String content) { this.content = content;  }

    public String getContent() { return content; }

    public void setAuthorId(Long authorId) {this.authorId = authorId; }

    public Long getAuthorId() {return authorId; }

    public void setBelongingTask(Long belongingTask) {this.belongingTask = belongingTask; }

    public Long getBelongingTask() {return belongingTask;}

}