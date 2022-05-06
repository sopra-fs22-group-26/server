package ch.uzh.ifi.group26.scrumblebee.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Column;

/**
 * Defines the primary key for the participant relation of Users to PollMeetings.
 * Each tuple will then have a voting value.
 */

@Embeddable
public class PollParticipantKey implements Serializable {

    @Column(name = "pollMeeting_id")
    private Long pollMeetingId;

    @Column(name = "user_id")
    private Long userId;

    /**
     * Constructors
     */

    public PollParticipantKey() {}

    public PollParticipantKey(Long pollMeetingId, Long userId) {
        this.pollMeetingId = pollMeetingId;
        this.userId = userId;
    }

    /**
     * Getter & setter methods
     */

    public void setPollMeetingId(Long pollMeetingId) {
        this.pollMeetingId = pollMeetingId;
    }

    public Long getPollMeetingId() {
        return pollMeetingId;
    }

    /***/

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    /**
     * equals and hashCode methodes are required
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PollParticipantKey that = (PollParticipantKey) o;
        return userId.equals(that.userId) && pollMeetingId.equals(that.pollMeetingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, pollMeetingId);
    }
}
