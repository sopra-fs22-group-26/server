package ch.uzh.ifi.group26.scrumblebee.entity;


import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@Table(name = "sb_task")
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
    private Integer averageEstimate;

    @Column(nullable = false)
    private PollMeetingStatus status;

    @ElementCollection
    private List<Integer> votes;

    @OneToMany
    private List<User> invitees;

    @OneToMany
    private List<User> participants;


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
    public List<User> getInvitees() { return invitees; }

    public void setInvitees(List<User> invitees) { this.invitees = invitees; }

    /***/

    public List<User> getParticipants() { return participants; }

    public void setParticipants(List<User> participants) { this.participants = participants; }

    /***/

    public List<Integer> getVotes() {return votes;}

    public void setVotes(List<Integer> votes) {
        this.votes = votes;
    }


}