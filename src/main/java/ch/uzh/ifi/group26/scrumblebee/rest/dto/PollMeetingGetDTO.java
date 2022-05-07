package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.PollParticipant;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PollMeetingGetDTO {

    private Long meetingId;
    private Long creatorId;
    private Long taskId;
    private Integer estimateThreshold;
    private Integer averageEstimate;
    private PollMeetingStatus status;
    private List<PollParticipantGetDTO> participants;

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setAverageEstimate(Integer averageEstimate) {
        this.averageEstimate = averageEstimate;
    }

    public Integer getAverageEstimate() {
        return averageEstimate;
    }

    public void setEstimateThreshold(Integer estimateThreshold) {
        this.estimateThreshold = estimateThreshold;
    }

    public Integer getEstimateThreshold() {
        return estimateThreshold;
    }

    public void setStatus(PollMeetingStatus status) {
        this.status = status;
    }

    public PollMeetingStatus getStatus() {
        return status;
    }

    public void setParticipants(Set<PollParticipant> participants) {
        List<PollParticipantGetDTO> participantGetDTOs = new ArrayList<>();
        for (PollParticipant participant : participants) {
            participantGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPollParticipantGetDTO(participant));
        }
        this.participants = participantGetDTOs;
    }
    public List<PollParticipantGetDTO> getParticipants() {
        return participants;
    }


}
