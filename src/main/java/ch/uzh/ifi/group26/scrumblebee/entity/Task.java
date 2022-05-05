package ch.uzh.ifi.group26.scrumblebee.entity;

import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Internal Task representation
 * This class composes the internal representation of a task and defines
 * how a task is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes
 * the primary key.
 */
@Entity
@Table(name = "sb_task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long taskId;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer estimate;

    @Column(nullable = false)
    private TaskPriority priority;

    @Column
    private String location;

    @Column(nullable = false)
    private TaskStatus status;

    @Column
    private int score;

    @Column
    private long assignee;

    @Column
    private long reporter;

    //Comment entity not implemented yet
    //@Column
    //private list<Comment> comments;

    /**
     * Getter & setter methods 
     */

    public Long getTaskId() { return taskId; }

    public void setTaskId(Long taskId) { this.taskId = taskId; }

    /***/

    public Date getDueDate() { return dueDate; }

    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    /***/

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    /***/

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    /***/

    public Integer getEstimate() { return estimate; }

    public void setEstimate(Integer estimate) { this.estimate = estimate; }

    /***/

    public TaskPriority getPriority() { return priority; }

    public void setPriority(TaskPriority priority) { this.priority = priority; }

     /***/

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

     /***/
    public TaskStatus getStatus() { return status; }

    public void setStatus(TaskStatus status) { this.status = status; }

    /***/

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    /***/

    public long getAssignee() {
        return assignee;
    }

    public void setAssignee(long assignee) {
        this.assignee = assignee;
    }

    /***/
    public long getReporter() {
        return reporter;
    }

    public void setReporter(long reporter) {
        this.reporter = reporter;
    }

    /**
     * Add Getter and setter for comment list here.
    */

}