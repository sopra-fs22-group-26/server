package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.entity.PollMeeting;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.PollMeetingPutDTO;
import ch.uzh.ifi.group26.scrumblebee.service.UserService;
import ch.uzh.ifi.group26.scrumblebee.service.PollMeetingService;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.PollMeetingGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.PollMeetingPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Poll Meeting Controller
 * This class is responsible for handling all REST request that are related to
 * the estimate-poll-meetings.
 * The controller will receive the request and delegate the execution to the
 * PollMeetingService and finally return the result.
 */
@RestController
public class PollMeetingController {

    private final PollMeetingService pollMeetingService;

    private final UserService userService;


    PollMeetingController(PollMeetingService pollMeetingService, UserService userService) {
        this.pollMeetingService = pollMeetingService;
        this.userService = userService;
    }



    /*------------------------------------- GET requests -----------------------------------------------------------*/

    /**
     * Type: GET
     * URL: /poll-meetings
     * Body: none
     * @return list<PollMeetings>
     */
    @GetMapping("/poll-meetings")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PollMeetingGetDTO> getAllPollMeetings() {
        List<PollMeeting> pollMeetings = pollMeetingService.getPollMeetings();
        List<PollMeetingGetDTO> pollMeetingGetDTOs = new ArrayList<>();
        for (PollMeeting pollMeeting : pollMeetings) {
            pollMeetingGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPollMeetingGetDTO(pollMeeting));
        }
        return pollMeetingGetDTOs;
    }

    /**
     * Type: GET
     * URL: /poll-meetings/{meetingId}
     * Body: none
     * @return PollMeeting
     */
    @GetMapping("/poll-meetings/{meetingId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PollMeetingGetDTO getPollMeeting(@PathVariable long meetingId) {
        PollMeeting pollMeeting = pollMeetingService.getPollMeeting(meetingId);
        return DTOMapper.INSTANCE.convertEntityToPollMeetingGetDTO(pollMeeting);
    }

    /*------------------------------------- POST requests ----------------------------------------------------------*/

    /**
     * Type: POST
     * URL: /poll-meetings
     * Body: dueDate, title, description, estimate, priority, status
     * Protection: check if request is coming from the client (check for special token)
     * @return PollMeeting
     */
    @PostMapping("/poll-meetings")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PollMeetingGetDTO createPollMeeting(@RequestBody PollMeetingPostDTO pollMeetingPostDTO){
        // convert API task to internal representation
        PollMeeting input = DTOMapper.INSTANCE.convertPollMeetingPostDTOtoEntity(pollMeetingPostDTO);

        // create task
        PollMeeting createdPollMeeting = pollMeetingService.createPollMeeting(input);

        // Add creator of the session to the participants
        User creator = userService.getUser(createdPollMeeting.getCreatorId());
        pollMeetingService.addParticipant(createdPollMeeting, creator);

        // add invitees to meeting
        for (long userId : pollMeetingPostDTO.getInvitees()) {
            User invitee = userService.getUser(userId);
            pollMeetingService.addInvitee(createdPollMeeting, invitee);
        }

        //convert internal representation of task back to API
        return DTOMapper.INSTANCE.convertEntityToPollMeetingGetDTO(createdPollMeeting);
    }


    /*------------------------------------- PUT requests -----------------------------------------------------------*/

    /**
     * Type: PUT
     * URL: /poll-meetings/{meetingId}
     * URL-Parameter: action [join, decline, vote]
     * Body: userId, vote, estimateThreshold, status
     * @return PollMeeting
     */
    @PutMapping("/poll-meetings/{meetingId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PollMeetingGetDTO updatePollMeeting(@RequestBody PollMeetingPutDTO pollMeetingPutDTO,
                                               @PathVariable long meetingId,
                                               @RequestParam(required = false) String action){
        // Get meeting with provided meetingId from repository
        PollMeeting pollMeeting = pollMeetingService.getPollMeeting(meetingId);

        // Set PollMeeting status if one was provided in request body
        if (pollMeetingPutDTO.getStatus() != null) {
            pollMeetingService.changeStatus(pollMeeting, pollMeetingPutDTO.getStatus());
        }

        // Perform action if response url contained an "action" parameter
        if (action != null) {
            User participant;

            switch (action) {
                case "join":
                    // A user joins the session.
                    // Get user with provided userID from repository (getUser throws an exception for invalid userId)
                    participant = userService.getUser(pollMeetingPutDTO.getUserId());
                    pollMeetingService.addParticipant(pollMeeting, participant);
                    break;

                case "decline":
                    // A user declines the invitation
                    // Get user with provided userID from repository (getUser throws an exception for invalid userId)
                    participant = userService.getUser(pollMeetingPutDTO.getUserId());
                    pollMeetingService.declineInvitation(pollMeeting, participant);
                    break;

                case "vote":
                    // A user casts a vote.
                    // Get user with provided userID from repository (getUser throws an exception for invalid userId)
                    participant = userService.getUser(pollMeetingPutDTO.getUserId());
                    pollMeetingService.castVote(pollMeeting, participant, pollMeetingPutDTO.getVote());
                    break;

                default:
                    // Do nothing
            }
        }

        return DTOMapper.INSTANCE.convertEntityToPollMeetingGetDTO(pollMeeting);
    }


    /*------------------------------------- DELETE requests --------------------------------------------------------*/

    /**
     * Type: DELETE
     * URL: /poll-meetings/{meetingId}
     * Body: username, name*, email, password
     * Protection: check if request is coming from the client (check for special token)
     * @return User
     */
    @DeleteMapping("/poll-meetings/{meetingId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Long> deletePollMeeting(@PathVariable long meetingId) {
        pollMeetingService.deletePollMeeting(meetingId);

        return new ResponseEntity<>(meetingId, HttpStatus.OK);
    }
}
