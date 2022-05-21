package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;

public class PollMeetingPutDTO {
    private Integer estimateThreshold;
    private Long userId;
    private int vote;
    private PollMeetingStatus status;

    public void setEstimateThreshold(Integer estimateThreshold) {
        this.estimateThreshold = estimateThreshold;
    }

    public Integer getEstimateThreshold() {
        return estimateThreshold;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getVote() {
        return vote;
    }

    public void setStatus(String status) {
        switch (status) {
            case "OPEN":
                this.status = PollMeetingStatus.OPEN;
                break;
            case "VOTING":
                this.status = PollMeetingStatus.VOTING;
                break;
            case "ENDED":
                this.status = PollMeetingStatus.ENDED;
                break;
            default:
                this.status = PollMeetingStatus.OPEN;
        }
    }

    public PollMeetingStatus getStatus() {
        return status;
    }
}
