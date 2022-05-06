package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.PollMeeting;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.PollMeetingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * PollMeeting Service
 * This class is the "worker" and responsible for all functionality related to
 * the Poll-meetings
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

    // Get all meetings
    public List<PollMeeting> getPollMeetings() {
        return this.pollMeetingRepository.findAll();
    }

    public PollMeeting getPollMeeting(long meetingId) {
        return this.pollMeetingRepository.findByMeetingId(meetingId);
    }

    /**
     * Used by: POST /poll-meetings
     * @param newPollMeeting
     * @return the created meeting
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

        return newPollMeeting;
    }

    // Add invitee to set and update repository
    public PollMeeting addInvitee(PollMeeting pollMeeting, User user) {
        pollMeeting.addInvitee(user);
        pollMeeting = pollMeetingRepository.save(pollMeeting);
        pollMeetingRepository.flush();
        return pollMeeting;
    }

    // Add participant to set and update repository (implicitly)
    public void addParticipant(PollMeeting pollMeeting, User user) {
        pollMeeting.addParticipant(user);
    }

    // Update a vote for a user in this meeting (if present)
    public void castVote(PollMeeting pollMeeting, User user, int vote) {
        pollMeeting.updateVote(user, vote);
    }


    /**
     * Used by: DELETE /poll-meetings/{meetingId}
     * @param meetingId
     * @return the created user
     */
    public void deletePollMeeting(long meetingId) {
        PollMeeting pollMeetingById = checkIfTaskIdExist(meetingId);

        pollMeetingRepository.delete(pollMeetingById);
    }

    //check if task exists by id
    private PollMeeting checkIfTaskIdExist(long meetingId) {
        PollMeeting pollMeetingById = pollMeetingRepository.findByMeetingId(meetingId);

        String baseErrorMessage = "The task with id: %s not found!";
        if (pollMeetingById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, meetingId));
        }
        return pollMeetingById;
    }
}
