package ch.uzh.ifi.group26.scrumblebee.rest.dto;

public class PollMeetingPutDTO {
    private Integer estimateThreshold;
    private Long userId;
    private int vote;

    public Integer getEstimateThreshold() {
        return estimateThreshold;
    }

    public void setEstimateThreshold(Integer estimateThreshold) {
        this.estimateThreshold = estimateThreshold;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
