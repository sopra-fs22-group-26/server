package ch.uzh.ifi.group26.scrumblebee.entity;

import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Internal Task representation
 * This class composes the internal representation of a task and deifnes
 * how a task is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes
 * the primary key.
 */
@Entity
@Table(name = "TASK")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long task_id;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
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

    //Comment entity not implemented yet
    //@Column
    //private list<Comment> comments;

    /**
     * Getter & setter methods 
     */

    public Long getTask_id() { return task_id; }

    public void setTask_id(Long task_id) { this.task_id = task_id; }

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

    public TaskPriority getTaskPriority() { return priority; }

    public void setTaskPriority(TaskPriority priority) { this.priority = priority; }

     /***/

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

     /***/
    public TaskStatus getTaskStatus() { return status; }

    public void setTaskStatus(TaskStatus status) { this.status = status; }

    /***/

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    /**
     * Add Getter and setter for comment list here.
    */

}