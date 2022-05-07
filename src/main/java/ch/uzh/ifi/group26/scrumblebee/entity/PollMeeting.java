package ch.uzh.ifi.group26.scrumblebee.entity;

import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;

import javax.persistence.*;
import java.io.Serializable;
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
    private Long taskId;

    @Column(nullable = false)
    private Integer estimateThreshold;

    @Column
    private Integer averageEstimate;

    @Column(nullable = false)
    private PollMeetingStatus status;

    // Relation for participants of a poll meeting, together with their votes
    @OneToMany(mappedBy = "pollMeeting",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<PollParticipant> participants = new HashSet<>();

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


    /**
     * Getter & setter methods
     */

    public void setMeetingId(Long meetingId) { this.meetingId = meetingId; }

    public Long getMeetingId() { return meetingId; }

    /***/

    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

    public Long getCreatorId() { return creatorId; }

    /***/

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    /***/

    public void setAverageEstimate(Integer averageEstimate) { this.averageEstimate = averageEstimate; }

    public Integer getAverageEstimate() { return averageEstimate; }

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

}