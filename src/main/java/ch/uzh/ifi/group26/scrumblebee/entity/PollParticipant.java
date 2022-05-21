package ch.uzh.ifi.group26.scrumblebee.entity;

import ch.uzh.ifi.group26.scrumblebee.constant.PollParticipantStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sb_poll_participant")
public class PollParticipant {

    @EmbeddedId
    private PollParticipantKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pollMeetingId")
    @JoinColumn(name = "pollMeeting_id")
    private PollMeeting pollMeeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    // Vote of user for poll-session
    private int vote;

    // Status of participation [invited, joined, declined]
    private PollParticipantStatus status;

    // Save creation timestamp to keep order of participants
    @CreationTimestamp
    private LocalDateTime createDateTime;

    /**
     * Constructors
     */

    public PollParticipant() {}

    public PollParticipant(PollMeeting pollMeeting, User user) {
        this.id = new PollParticipantKey(pollMeeting.getMeetingId(), user.getId());
        this.pollMeeting = pollMeeting;
        this.user = user;
    }

    /**
     * Getter & setter methods
     */

    public void setId(PollParticipantKey id) {
        this.id = id;
    }

    public PollParticipantKey getId() {
        return id;
    }

    /***/

    public void setPollMeeting(PollMeeting pollMeeting) {
        this.pollMeeting = pollMeeting;
    }

    public PollMeeting getPollMeeting() {
        return pollMeeting;
    }

    /***/

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    /***/

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getVote() {
        return vote;
    }

    /***/

    public void setStatus(PollParticipantStatus status) {
        this.status = status;
    }

    public PollParticipantStatus getStatus() {
        return status;
    }

    /***/

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

}
