package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.PollMeeting;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.repository.PollMeetingRepository;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class PollMeetingService {

    private final Logger log = LoggerFactory.getLogger(PollMeetingService.class);

    private final PollMeetingRepository pollMeetingRepository;

    @Autowired
    public PollMeetingService(@Qualifier("pollMeetingRepository") PollMeetingRepository pollMeetingRepository) {
        this.pollMeetingRepository = pollMeetingRepository;
    }

    // Get all tasks, completed or not
    public List<PollMeeting> getPollMeetings() {
        return this.pollMeetingRepository.findAll();
    }

    // Return only either active or completed (= not active) tasks
    public List<PollMeeting> getPollMeetings(String status) {
        List<PollMeeting> allPollMeetings = getPollMeetings();
        switch (status) {
            case "active":
                allPollMeetings = allPollMeetings.stream().filter(pollMeeting -> pollMeeting.getStatus() == PollMeetingStatus.OPEN).collect(Collectors.toList());
                break;
            case "completed":
                allPollMeetings = allPollMeetings.stream().filter(pollMeeting -> pollMeeting.getStatus() != PollMeetingStatus.OPEN).collect(Collectors.toList());
        }
        return allPollMeetings;
    }

    public PollMeeting getPollMeeting(long meetingId) {return this.pollMeetingRepository.findByMeetingId(meetingId);}
    /**
     * Used by: POST /tasks
     * @param newPollMeeting
     * @return the created task
     */
    public PollMeeting createPollMeeting(PollMeeting newPollMeeting) {

        newPollMeeting.setStatus(PollMeetingStatus.OPEN);

        log.debug(newPollMeeting.getCreatorId().toString());
        log.debug(newPollMeeting.getEstimateThreshold().toString());
        log.debug(newPollMeeting.getInvitees().toString());
        log.debug(newPollMeeting.getStatus().toString());

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newPollMeeting = pollMeetingRepository.save(newPollMeeting);
        pollMeetingRepository.flush();

        log.debug("Created Information for Task: {}", newPollMeeting);
        return newPollMeeting;
    }
//
//    /**
//     * Used by: PUT /tasks/{taskId}
//     * @param meetingId
//     * @param changesPollMeeting
//     * @return the created user
//     */
//    public PollMeeting updatePollMeeting(long meetingId, PollMeeting changesPollMeeting, String updateStatus) {
//        PollMeeting pollMeetingById = checkIfTaskIdExist(meetingId);
//
//        // Update status if one was provided in the query
//        if(updateStatus != null) {
//            switch (updateStatus) {
//                case "active":
//                    pollMeetingById.setStatus(TaskStatus.ACTIVE);
//                    break;
//                case "completed":
//                    pollMeetingById.setStatus(TaskStatus.COMPLETED);
//                    break;
//                case "reported":
//                    pollMeetingById.setStatus(TaskStatus.REPORTED);
//                    break;
//                case "archived":
//                    pollMeetingById.setStatus(TaskStatus.ARCHIVED);
//            }
//        }
//        else {
//
//            if (changesPollMeeting.getTitle() != null) {
//                pollMeetingById.setTitle(changesTask.getTitle());
//            }
//            if (changesPollMeeting.getDescription() != null) {
//                pollMeetingById.setDescription(changesTask.getDescription());
//            }
//            if (changesPollMeeting.getDueDate() != null) {
//                pollMeetingById.setDueDate(changesTask.getDueDate());
//            }
//            if (changesPollMeeting.getPriority() != null) {
//                pollMeetingById.setPriority(changesTask.getPriority());
//            }
//            if (changesPollMeeting.getEstimate() != null) {
//                pollMeetingById.setEstimate(changesTask.getEstimate());
//            }
//            if (changesPollMeeting.getLocation() != null) {
//                pollMeetingById.setLocation(changesTask.getLocation());
//            }
//
//            pollMeetingById.setAssignee(changesTask.getAssignee());
//            pollMeetingById.setReporter(changesTask.getReporter());
//        }
//
//        // saves the given entity but data is only persisted in the database once
//        // flush() is called
//        pollMeetingById = pollMeetingRepository.save(pollMeetingById);
//        pollMeetingRepository.flush();
//
//        log.debug("Edited Information for Task: {}", pollMeetingById);
//        return pollMeetingById;
//    }


    /**
     * Used by: DELETE /poll-meetings/{meetingId}
     * @param meetingId
     * @return the created user
     */
    public void deletePollMeeting(long meetingId) {
        PollMeeting pollMeetingById = checkIfTaskIdExist(meetingId);

        pollMeetingRepository.delete(pollMeetingById);
    }

    //check is task exist by id
    private PollMeeting checkIfTaskIdExist(long meetingId) {
        PollMeeting pollMeetingById = pollMeetingRepository.findByMeetingId(meetingId);

        String baseErrorMessage = "The user with id: %s not found!";
        if (pollMeetingById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, meetingId));
        }
        return pollMeetingById;
    }


}
