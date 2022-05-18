package ch.uzh.ifi.group26.scrumblebee.entity;

import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @Column(nullable = false)
    private boolean privateFlag;


    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("creationDate")
    private Set<Comment> comments = new HashSet<>();


    // Relation to poll-meeting, if task is affected by it
    @OneToOne(mappedBy = "task", cascade = CascadeType.REMOVE)
    private PollMeeting pollMeeting;

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

    /***/

    public void setPollMeeting(PollMeeting pollMeeting) {
        this.pollMeeting = pollMeeting;
    }

    public PollMeeting getPollMeeting() {
        return pollMeeting;
    }

    /***/

    public void setPrivateFlag(boolean privateFlag) {
        this.privateFlag = privateFlag;
    }

    public boolean getPrivateFlag() {
        return privateFlag;
    }


    /**
     * Add Getter and setter for comment list here.
    */
    public void setComments(Set<Comment> comments) {this.comments = comments;}

    public Set<Comment> getComments(){return comments;}

    public void addComment(Comment aComment){
        comments.add(aComment);
    }

    public void removeComment(Comment aComment){
        comments.remove(aComment);
    }
}