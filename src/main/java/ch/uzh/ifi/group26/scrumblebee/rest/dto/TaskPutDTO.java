package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskPutDTO {
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

    private static final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

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
            case "NONE" -> this.priority = TaskPriority.NONE;
            case "LOW"  -> this.priority = TaskPriority.LOW;
            case "MEDIUM"  -> this.priority = TaskPriority.MEDIUM;
            case "HIGH"  -> this.priority = TaskPriority.HIGH;
        }
    }

    public TaskPriority getPriority() { return priority; }

    public void setLocation(String location) { this.location = location; }

    public String getLocation() { return location; }

    public void setStatus(String status) {
        switch (status) {
            case "ACTIVE" -> this.status = TaskStatus.ACTIVE;
            case "ARCHIVED" -> this.status = TaskStatus.ARCHIVED;
            case "REPORTED" -> this.status = TaskStatus.REPORTED;
            case "COMPLETED" -> this.status = TaskStatus.COMPLETED;
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

}
