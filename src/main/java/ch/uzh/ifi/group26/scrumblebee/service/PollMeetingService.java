package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;
import ch.uzh.ifi.group26.scrumblebee.constant.PollParticipantStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.*;
import ch.uzh.ifi.group26.scrumblebee.repository.PollMeetingRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.PollParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * PollMeeting Service
 * This class is the "worker" and responsible for all functionality related to
 * the Poll-meetings
 */
@Service
@Transactional
public class PollMeetingService {

    private final Logger log = LoggerFactory.getLogger(PollMeetingService.class);

    @Autowired
    PollMeetingRepository pollMeetingRepository;

    @Autowired
    PollParticipantRepository pollParticipantRepository;


    // Get all meetings, sorted by creation date
    public List<PollMeeting> getPollMeetings() {
        return this.pollMeetingRepository.findAll(Sort.by(Sort.Direction.ASC, "createDateTime"));
    }

    public PollMeeting getPollMeeting(long meetingId) {
        return this.pollMeetingRepository.findByMeetingId(meetingId);
    }

    /**
     * Used by: POST /poll-meetings
     * Checks if task has not already been assigned to another meeting.
     * If everyhting is ok => store meeting to database.
     * @param newPollMeeting, task
     * @return the created meeting
     */
    public PollMeeting createPollMeeting(PollMeeting newPollMeeting, Task task) {

        // Check if task is already assigned => throws error in that case
        checkIfTaskIsAlreadyAssigned(task);
        // Assign task to pollMeeting
        assignTask(newPollMeeting, task);

        newPollMeeting.setStatus(PollMeetingStatus.OPEN);

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newPollMeeting = pollMeetingRepository.save(newPollMeeting);
        pollMeetingRepository.flush();

        return newPollMeeting;
    }

    /**
     * Used by: POST /poll-meetings
     * Assign task to PollMeeting.
     * @param pollMeeting, task
     */
    public void assignTask(PollMeeting pollMeeting, Task task) {
        pollMeeting.setTask(task);
    }

    /**
     * Used by: POST /poll-meetings
     * Add invitee to set of participants with status "INVITED" and update repository.
     * @param pollMeeting, user
     */
    public void addInvitee(PollMeeting pollMeeting, User user) {
        PollParticipantKey idToCheck = new PollParticipantKey(pollMeeting.getMeetingId(), user.getId());
        if (!pollParticipantRepository.findById(idToCheck).isPresent()) {
            PollParticipant pollParticipant = pollMeeting.addParticipant(user);
            pollParticipant.setStatus(PollParticipantStatus.INVITED);
        }
    }

    /**
     * Change meeting status
     */
    public void changeStatus(PollMeeting pollMeeting, PollMeetingStatus status) {
        pollMeeting.setStatus(status);
    }


    /**
     * Participant functions
     */

    // Add participant - if they are not already participating - and update repository (implicitly).
    // This function is also called if an invited user joins the session
    public void addParticipant(PollMeeting pollMeeting, User user) {
        PollParticipantKey idToCheck = new PollParticipantKey(pollMeeting.getMeetingId(), user.getId());
        Optional<PollParticipant> pollParticipant = pollParticipantRepository.findById(idToCheck);
        if (!pollParticipant.isPresent()) {
            // User is not in session => add them
            pollParticipant = Optional.of(pollMeeting.addParticipant(user));
        }
        pollParticipant.get().setStatus(PollParticipantStatus.JOINED);

    }

    // User declines an invitation
    public void declineInvitation(PollMeeting pollMeeting, User user) {
        PollParticipantKey idToCheck = new PollParticipantKey(pollMeeting.getMeetingId(), user.getId());
        Optional<PollParticipant> pollParticipant = pollParticipantRepository.findById(idToCheck);
        if (pollParticipant.isPresent() && pollParticipant.get().getStatus() == PollParticipantStatus.INVITED) {
            pollParticipant.get().setStatus(PollParticipantStatus.DECLINED);
        }
    }

    // Remove participant if they exist and update repository (implicitly)
    public void removeParticipant(PollMeeting pollMeeting, User user) {
        PollParticipantKey idToCheck = new PollParticipantKey(pollMeeting.getMeetingId(), user.getId());
        Optional<PollParticipant> pollParticipant = pollParticipantRepository.findById(idToCheck);
        if (pollParticipant.isPresent()) {
            pollMeeting.removeParticipant(pollParticipant.get());
        }
    }

    // Update a vote for a user in this meeting (if present)
    public void castVote(PollMeeting pollMeeting, User user, int vote) {
        PollParticipantKey idToCheck = new PollParticipantKey(pollMeeting.getMeetingId(), user.getId());
        Optional<PollParticipant> pollParticipant = pollParticipantRepository.findById(idToCheck);
        if (pollParticipant.isPresent()) {
            pollParticipant.get().setVote(vote);
        }
    }


    /**
     * Used by: DELETE /poll-meetings/{meetingId}
     * @param meetingId of meeting to delete
     */
    public void deletePollMeeting(long meetingId) {
        PollMeeting pollMeetingById = checkIfTaskIdExist(meetingId);

        pollMeetingRepository.delete(pollMeetingById);
    }

    //check if pollMeeting exists by id
    private PollMeeting checkIfTaskIdExist(long meetingId) {
        PollMeeting pollMeetingById = pollMeetingRepository.findByMeetingId(meetingId);

        String baseErrorMessage = "The pollMeeting with id: %s not found!";
        if (pollMeetingById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, meetingId));
        }
        return pollMeetingById;
    }

    // Check if task is already assigned to a pollMeeting
    private void checkIfTaskIsAlreadyAssigned(Task task) {
        String baseErrorMessage = "The task with id %s is already assigned to a Poll-Session!";
        if (task.getPollMeeting() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, task.getTaskId()));
        }
    }
}
