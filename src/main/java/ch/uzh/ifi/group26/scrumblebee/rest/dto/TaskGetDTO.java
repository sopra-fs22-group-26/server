package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;


public class TaskGetDTO {

    private Long taskId;
    private Long creatorId;
    private java.util.Date dueDate;
    private String title;
    private String description;
    private int estimate;
    private TaskPriority priority;
    private String location;
    private TaskStatus status;
    private int score;
    private long assignee;
    private long reporter;
    private boolean privateFlag;
    private int nofComments;

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dueDate);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setEstimate(Integer estimate) {
        this.estimate = estimate;
    }

    public Integer getEstimate() {
        return estimate;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public String getPriority() {
        String returnPriority = "";
        switch (priority) {
            case NONE:
                returnPriority = "NONE";
                break;
            case LOW:
                returnPriority = "LOW";
                break;
            case MEDIUM:
                returnPriority = "MEDIUM";
                break;
            case HIGH:
                returnPriority = "HIGH";
                break;
            default:
                returnPriority = "NONE";
        }
        return returnPriority;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getStatus() {
        String returnStatus = "";
        switch (status) {
            case ACTIVE:
                returnStatus = "ACTIVE";
                break;
            case ARCHIVED:
                returnStatus = "ARCHIVED";
                break;
            case REPORTED:
                returnStatus = "REPORTED";
                break;
            case COMPLETED:
                returnStatus = "COMPLETED";
                break;
            default:
                returnStatus = "ACTIVE";
        }
        return returnStatus;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setAssignee(long assignee) {
        this.assignee = assignee;
    }

    public long getAssignee() {
        return assignee;
    }

    public void setReporter(long reporter) {
        this.reporter = reporter;
    }

    public long getReporter() {
        return reporter;
    }

    public void setPrivateFlag(boolean privateFlag) {
        this.privateFlag = privateFlag;
    }

    public boolean getPrivateFlag() {
        return privateFlag;
    }

    public void setNofComments(Set<Comment> comments) {
        this.nofComments = comments != null ? comments.size() : 0;
    }

    public int getNofComments() {
        return nofComments;
    }
}
