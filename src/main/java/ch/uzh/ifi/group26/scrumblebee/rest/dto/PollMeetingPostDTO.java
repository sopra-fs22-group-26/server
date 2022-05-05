package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import ch.uzh.ifi.group26.scrumblebee.entity.User;

import java.util.List;

public class PollMeetingPostDTO {

    private Long meetingId;
    private long creatorId;
    private Integer estimateThreshold;
    private Integer averageEstimate;
    private List<Integer> votes;
    private List<Long> invitees;
    private List<User> participants;

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getAverageEstimate() {
        return averageEstimate;
    }

    public void setAverageEstimate(Integer averageEstimate) {
        this.averageEstimate = averageEstimate;
    }

    public Integer getEstimateThreshold() {
        return estimateThreshold;
    }

    public void setEstimateThreshold(Integer estimateThreshold) {
        this.estimateThreshold = estimateThreshold;
    }

    public List<Long> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<Long> invitees) {
        this.invitees = invitees;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public List<Integer> getVotes() {
        return votes;
    }

    public void setVotes(List<Integer> votes) {
        this.votes = votes;
    }

}
