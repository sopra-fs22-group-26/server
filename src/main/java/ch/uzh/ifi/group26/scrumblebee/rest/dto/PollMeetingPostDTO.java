package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import java.util.List;

public class PollMeetingPostDTO {

    private Long meetingId;
    private long creatorId;
    private Integer estimateThreshold;
    private Integer averageEstimate;
    private List<Long> invitees;

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

}
