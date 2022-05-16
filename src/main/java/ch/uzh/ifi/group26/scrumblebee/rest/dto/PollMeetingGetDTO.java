package ch.uzh.ifi.group26.scrumblebee.rest.dto;

import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.PollParticipant;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PollMeetingGetDTO {

    private Long meetingId;
    private Long creatorId;
    private TaskGetDTO task;
    private Integer estimateThreshold;
    private Integer averageEstimate;
    private PollMeetingStatus status;
    private LocalDateTime createDateTime;
    private List<PollParticipantGetDTO> participants;

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    /***/

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    /***/

    public void setTask(Task task) {
        this.task = DTOMapper.INSTANCE.convertEntityToTaskGetDTO(task);
    }

    public TaskGetDTO getTask() {
        return task;
    }

    /***/

    public void setAverageEstimate(Integer averageEstimate) {
        this.averageEstimate = averageEstimate;
    }

    public Integer getAverageEstimate() {
        return averageEstimate;
    }

    /***/

    public void setEstimateThreshold(Integer estimateThreshold) {
        this.estimateThreshold = estimateThreshold;
    }

    public Integer getEstimateThreshold() {
        return estimateThreshold;
    }

    /***/

    public void setStatus(PollMeetingStatus status) {
        this.status = status;
    }

    public PollMeetingStatus getStatus() {
        return status;
    }

    /***/

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    /***/

    public void setParticipants(Set<PollParticipant> participants) {
        // Sort participants by the time they joined the session
        List<PollParticipant> sortedParticipants = participants.stream()
                .sorted(Comparator.comparing(PollParticipant::getCreateDateTime))
                .collect(Collectors.toList());
        // Convert to a GetDTO
        List<PollParticipantGetDTO> participantGetDTOs = new ArrayList<>();
        for (PollParticipant participant : sortedParticipants) {
            participantGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPollParticipantGetDTO(participant));
        }
        this.participants = participantGetDTOs;
    }

    public List<PollParticipantGetDTO> getParticipants() {
        return participants;
    }

}
