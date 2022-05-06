package ch.uzh.ifi.group26.scrumblebee.rest.dto;

public class PollParticipantGetDTO {
    private UserGetDTO user;
    private Integer vote;

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
}
