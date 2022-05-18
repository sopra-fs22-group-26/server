package ch.uzh.ifi.group26.scrumblebee.rest.dto;

public class CommentPostDTO {

    private Long commentId;
    private String content;
    private Long authorId;
    private Long belongingTask;

    public void setCommentId(Long commentId) {this.commentId = commentId;}

    public Long getCommentId() { return commentId; }

    public void setContent(String content) { this.content = content;  }

    public String getContent() { return content; }

    public void setAuthorId(Long authorId) {this.authorId = authorId; }

    public Long getAuthorId() {return authorId; }

    public void setBelongingTask(Long belongingTask) {this.belongingTask = belongingTask; }

    public Long getBelongingTask() {return belongingTask;}

}
