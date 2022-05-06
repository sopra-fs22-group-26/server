package ch.uzh.ifi.group26.scrumblebee.entity;

import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "meeting_invitees",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> invitees = new HashSet<>();

    // Relation for participants of a poll meeting, together with their votes
    @OneToMany(mappedBy = "pollMeeting",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<PollParticipant> participants = new HashSet<>();

    /**
     * Add and remove participants
     */
    public void addParticipant(User user) {
        if (findPollParticipant(user) == null) {
            PollParticipant pollParticipant = new PollParticipant(this, user);
            participants.add(pollParticipant);
            user.getPollMeetings().add(pollParticipant);
        }
    }

    public void removeParticipant(User user) {
        PollParticipant pollParticipant = findPollParticipant(user);
        if (pollParticipant != null) {
            participants.remove(pollParticipant);
            pollParticipant.getUser().getPollMeetings().remove(pollParticipant);
            pollParticipant.setPollMeeting(null);
            pollParticipant.setUser(null);
        }

        /*
        for (Iterator<PollParticipant> iterator = participants.iterator(); iterator.hasNext(); ) {
            PollParticipant pollParticipant = iterator.next();
            if (pollParticipant.getPollMeeting().equals(this) && pollParticipant.getUser().equals(user)) {
                iterator.remove();
                pollParticipant.getUser().getPollMeetings().remove(pollParticipant);
                pollParticipant.setPollMeeting(null);
                pollParticipant.setUser(null);
            }
         */
    }

    // Update a user's vote if the user is in the session. Ignore otherwise.
    public void updateVote(User user, int vote) {
        PollParticipant pollParticipant = findPollParticipant(user);
        if (pollParticipant != null) {
            pollParticipant.setVote(vote);
        }
    }

    /**
     * Helper function for participants
     */
    // Check if user is in the participants set.
    // Return found PollParticipant of null if user is not in set.
    private PollParticipant findPollParticipant(User user) {
        for (Iterator<PollParticipant> iterator = participants.iterator(); iterator.hasNext(); ) {
            PollParticipant pollParticipant = iterator.next();
            if (pollParticipant.getPollMeeting().equals(this) && pollParticipant.getUser().equals(user)) {
                return pollParticipant;
            }
        }
        // Not found in participants
        return null;
    }

    /**
     * Add and remove invitees
     */
    public void addInvitee(User user) {
        invitees.add(user);
        user.getMeeting_invitations().add(this);
    }
    public void removeInvitee(User user) {
        invitees.remove(user);
        user.getMeeting_invitations().remove(this);
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

    public void setInvitees(Set<User> invitees) {
        this.invitees = invitees;
    }

    public Set<User> getInvitees() {
        return invitees;
    }

    /***/

    public void setParticipants(Set<PollParticipant> participants) {
        this.participants = participants;
    }

    public Set<PollParticipant> getParticipants() {
        return participants;
    }

}