package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import ch.uzh.ifi.group26.scrumblebee.constant.PollParticipantStatus;

public class PollParticipantGetDTO {
    private UserGetDTO user;
    private Integer vote;
    private PollParticipantStatus status;

    public void setUser(UserGetDTO user) {
        this.user = user;
    }

    public UserGetDTO getUser() {
        return user;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public Integer getVote() {
        return vote;
    }

    public void setStatus(PollParticipantStatus status) {
        this.status = status;
    }

    public PollParticipantStatus getStatus() {
        return status;
    }
}
