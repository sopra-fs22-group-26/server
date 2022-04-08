package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import org.mapstruct.Mapping;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskGetDTO {

    private Long task_id;
    private java.util.Date dueDate;
    private String title;
    private String description;
    private int estimate;
    private TaskPriority priority;
    private String location;
    private TaskStatus status;
    private int score;

    private static final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

    public Long getTask_id() {
        return task_id;
    }

    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Date getDueDate() { return dueDate; }

    public void setTitle(String title) { this.title = title; }

    public String getTitle() { return title; }

    public void setDescription(String description) { this.description = description; }

    public String getDescription() { return description; }

    public void setEstimate(Integer estimate) { this.estimate = estimate; }

    public Integer getEstimate() { return estimate; }

    public void setPriority(TaskPriority priority) { this.priority = priority; }

    public TaskPriority getPriority() { return priority; }

    public void setLocation(String location) { this.location = location; }

    public String getLocation() { return location; }

    public void setStatus(TaskStatus status) { this.status = status; }

    public String getStatus() {
        String returnStatus = "";
        switch (status) {
            case ACTIVE -> returnStatus = "ACTIVE";
            case ARCHIVED -> returnStatus = "ARCHIVED";
            case REPORTED -> returnStatus = "REPORTED";
            case COMPLETED -> returnStatus = "COMPLETED";
        }
        return returnStatus;
    }

    public void setScore(int score) { this.score = score; }

    public int getScore() { return score; }

}
