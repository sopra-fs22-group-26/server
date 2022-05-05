package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PollMeetingGetDTO {

    private Long meetingId;
    private long creatorId;
    private Integer estimateThreshold;
    private Integer averageEstimate;
    private PollMeetingStatus status;
    private List<Integer> votes;
    private List<UserGetDTO> invitees;
    private List<UserGetDTO> participants;

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

    public List<UserGetDTO> getInvitees() {
        return invitees;
    }

    public void setInvitees(Set<User> invitees) {
        List<UserGetDTO> userGetDTOs = new ArrayList<>();
        for (User user : invitees) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        this.invitees = userGetDTOs;
    }

    public List<UserGetDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        List<UserGetDTO> userGetDTOs = new ArrayList<>();
        for (User user : participants) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        this.participants = userGetDTOs;
    }

    public List<Integer> getVotes() {
        return votes;
    }

    public void setVotes(List<Integer> votes) {
        this.votes = votes;
    }

    public PollMeetingStatus getStatus() {
        return status;
    }

    public void setStatus(PollMeetingStatus status) {
        this.status = status;
    }

}
