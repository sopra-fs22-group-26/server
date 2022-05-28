package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;

import java.util.Date;

public class TaskPostDTO {

    private Long taskId;
    private Long creatorId;
    private java.util.Date dueDate;
    private String title;
    private String description;
    private int estimate;
    private TaskPriority priority;
    private String location;
    private String geoLocation;
    private TaskStatus status;
    private int score;
    private long assignee;
    private long reporter;
    private boolean privateFlag;

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

    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Date getDueDate() { return dueDate; }

    public void setTitle(String title) { this.title = title; }

    public String getTitle() { return title; }

    public void setDescription(String description) { this.description = description; }

    public String getDescription() { return description; }

    public void setEstimate(Integer estimate) { this.estimate = estimate; }

    public Integer getEstimate() { return estimate; }

    public void setPriority(String priority) {
        switch (priority) {
            case "NONE":
                this.priority = TaskPriority.NONE;
                break;
            case "LOW":
                this.priority = TaskPriority.LOW;
                break;
            case "MEDIUM":
                this.priority = TaskPriority.MEDIUM;
                break;
            case "HIGH":
                this.priority = TaskPriority.HIGH;
                break;
            default:
                this.priority = TaskPriority.NONE;
        }
    }

    public TaskPriority getPriority() { return priority; }

    public void setLocation(String location) { this.location = location; }

    public String getLocation() { return location; }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setStatus(String status) {
        switch (status) {
            case "ACTIVE":
                this.status = TaskStatus.ACTIVE;
                break;
            case "ARCHIVED":
                this.status = TaskStatus.ARCHIVED;
                break;
            case "REPORTED":
                this.status = TaskStatus.REPORTED;
                break;
            case "COMPLETED":
                this.status = TaskStatus.COMPLETED;
                break;
            default:
                this.status = TaskStatus.ACTIVE;
        }
    }

    public TaskStatus getStatus() { return status; }

    public void setScore(int score) { this.score = score; }

    public int getScore() { return score; }

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
}