package ch.uzh.ifi.group26.scrumblebee.entity;


import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;
import ch.uzh.ifi.group26.scrumblebee.service.UserService;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Internal PollMeeting representation
 * This class composes the internal representation of a task and defines
 * how a task is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes
 * the primary key.
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

    @Column
    private Integer averageEstimate;

    @Column(nullable = false)
    private PollMeetingStatus status;

    @ElementCollection
    private List<Integer> votes;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "meeting_invitees",
    joinColumns = @JoinColumn(name = "meeting_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> invitees = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "meeting_participants",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id"))
    private Set<User> participants = new HashSet<>();

    /**
     * Add and remove invitees and participants
     */
    public void addInvitee(User user) {
        invitees.add(user);
        user.getMeeting_invitations().add(this);
    }
    public void removeInvitee(User user) {
        invitees.remove(user);
        user.getMeeting_invitations().remove(this);
    }
    public void addParticipant(User user) {
        participants.add(user);
        user.getMeeting_participations().add(this);
    }
    public void removeParticipant(User user) {
        participants.remove(user);
        user.getMeeting_participations().remove(this);
    }

    /**
     * Getter & setter methods
     */

    public Long getMeetingId() { return meetingId; }

    public void setMeetingId(Long meetingId) { this.meetingId = meetingId; }

    /***/

    public Long getCreatorId() { return creatorId; }

    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

    /***/

    public Integer getAverageEstimate() { return averageEstimate; }

    public void setAverageEstimate(Integer averageEstimate) { this.averageEstimate = averageEstimate; }

    /***/

    public Integer getEstimateThreshold() { return estimateThreshold; }

    public void setEstimateThreshold(Integer estimateThreshold) { this.estimateThreshold = estimateThreshold; }

    /***/

    public void setInvitees(Set<User> invitees) {
        this.invitees = invitees;
    }

    public Set<User> getInvitees() {
        return invitees;
    }

    /***/

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    /***/

    public List<Integer> getVotes() {return votes;}

    public void setVotes(List<Integer> votes) {
        this.votes = votes;
    }

    /***/

    public PollMeetingStatus getStatus() {return status;}

    public void setStatus(PollMeetingStatus status) {
        this.status = status;
    }
}