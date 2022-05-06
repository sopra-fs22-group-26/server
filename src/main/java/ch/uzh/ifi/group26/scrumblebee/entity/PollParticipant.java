package ch.uzh.ifi.group26.scrumblebee.entity;

import javax.persistence.*;

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
}
