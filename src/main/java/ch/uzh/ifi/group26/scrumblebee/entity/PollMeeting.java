package ch.uzh.ifi.group26.scrumblebee.entity;

import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Internal PollMeeting representation
 */

@Entity
@Table(name = "sb_pollMeeting")
public class PollMeeting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long meetingId;

    @Column(nullable = false)
    private Long creatorId;

    @Column(nullable = false)
    private Integer estimateThreshold;

    @Column(nullable = false)
    private PollMeetingStatus status;

    // Save creation timestamp to keep sorting of all sessions consistent
    @CreationTimestamp
    private LocalDateTime createDateTime;

    // Relation for participants of a poll meeting, together with their votes
    @OneToMany(mappedBy = "pollMeeting",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<PollParticipant> participants = new HashSet<>();

    // Relation for affected task
    @OneToOne
    @JoinColumn(name = "pm_task")
    private Task task;

    // Additional information for REST requests
    private String creatorName;

    /**
     * Manipulate participants
     */
    // A user (invited or uninvited) joins the session
    public PollParticipant addParticipant(User user) {
        PollParticipant pollParticipant = new PollParticipant(this, user);
        participants.add(pollParticipant);
        user.getPollMeetings().add(pollParticipant);
        return pollParticipant;
    }

    public void removeParticipant(PollParticipant pollParticipant) {
        participants.remove(pollParticipant);
        pollParticipant.getUser().getPollMeetings().remove(pollParticipant);
        pollParticipant.setPollMeeting(null);
        pollParticipant.setUser(null);
    }

    /**************
     * Special functions for information output:
     * - Calculate average of all voted estimates
     * - Collect information about affected task (title, description)
     **************/

    /**
     * Calculate the average estimate each time
     * @return averageEstimate [integer]
     */
    public int getAverageEstimate() {
        int voters = 0;
        int voteSum = 0;
        int averageEstimate = 0;

        // Collect votes
        for (PollParticipant pollParticipant : participants) {
            if (pollParticipant.getVote() > 0) {
                voters++;
                voteSum += pollParticipant.getVote();
            }
        }

        // Calculate average
        // Attention: We want the result to be rounded-up to the next integer, and we are dividing integers
        if (voters > 0) {
            averageEstimate = voteSum / voters + ((voteSum % voters == 0) ? 0 : 1);
        }
        return averageEstimate;
    }


    /**************
     * Standard getter & setter methods
     **************/

    public void setMeetingId(Long meetingId) { this.meetingId = meetingId; }

    public Long getMeetingId() { return meetingId; }

    /***/

    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

    public Long getCreatorId() { return creatorId; }

    /***/

    public void setEstimateThreshold(Integer estimateThreshold) { this.estimateThreshold = estimateThreshold; }

    public Integer getEstimateThreshold() { return estimateThreshold; }

    /***/

    public void setStatus(PollMeetingStatus status) {
        this.status = status;
    }

    public PollMeetingStatus getStatus() {return status;}

    /***/

    public void setParticipants(Set<PollParticipant> participants) {
        this.participants = participants;
    }

    public Set<PollParticipant> getParticipants() {
        return participants;
    }

    /***/

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    /***/

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) { this.createDateTime = createDateTime; }

    /***/

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorName() {
        return creatorName;
    }
}