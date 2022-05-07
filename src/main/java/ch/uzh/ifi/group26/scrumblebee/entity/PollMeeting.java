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

    /***/

    // This is a special case
    // We want to calculate the average estimate each time
    public int getAverageEstimate() {
        int voters = 0;
        int voteSum = 0;
        int estimate = 0;

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
            estimate = voteSum / voters + ((voteSum % voters == 0) ? 0 : 1);
        }
        return estimate;
    }

    /**
     * Standard getter & setter methods
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