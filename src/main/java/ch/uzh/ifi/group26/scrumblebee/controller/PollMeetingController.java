package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.entity.PollMeeting;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.PollMeetingPutDTO;
import ch.uzh.ifi.group26.scrumblebee.service.TaskService;
import ch.uzh.ifi.group26.scrumblebee.service.UserService;
import ch.uzh.ifi.group26.scrumblebee.service.PollMeetingService;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.PollMeetingGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.PollMeetingPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final TaskService taskService;


    PollMeetingController(PollMeetingService pollMeetingService, UserService userService, TaskService taskService) {
        this.pollMeetingService = pollMeetingService;
        this.userService = userService;
        this.taskService = taskService;
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
            // Get creator's name
            User creator = userService.getUser(pollMeeting.getCreatorId());
            pollMeeting.setCreatorName(creator.getName() != null ? creator.getName() : creator.getUsername());

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
        // Get creator's name
        User creator = userService.getUser(pollMeeting.getCreatorId());
        pollMeeting.setCreatorName(creator.getName() != null ? creator.getName() : creator.getUsername());

        return DTOMapper.INSTANCE.convertEntityToPollMeetingGetDTO(pollMeeting);
    }

    /*------------------------------------- POST requests ----------------------------------------------------------*/

    /**
     * Type: POST
     * URL: /poll-meetings
     * Body: creatorId, taskId, estimateThreshold, invitees[]
     * @return PollMeeting
     */
    @PostMapping("/poll-meetings")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PollMeetingGetDTO createPollMeeting(@RequestBody PollMeetingPostDTO pollMeetingPostDTO){
        // convert request to internal representation
        PollMeeting input = DTOMapper.INSTANCE.convertPollMeetingPostDTOtoEntity(pollMeetingPostDTO);

        // Collect creator and task of the session
        // => Throws an error if one of both does not exist
        User creator = userService.getUser(pollMeetingPostDTO.getCreatorId());
        Task task = taskService.getTask(pollMeetingPostDTO.getTaskId()).get();

        // Create pollMeeting and assign task, if task is not already assigned.
        // createPollMeeting will throw an error otherwise.
        PollMeeting createdPollMeeting = pollMeetingService.createPollMeeting(input, task);

        // Add creator of the session to the participants
        pollMeetingService.addParticipant(createdPollMeeting, creator);

        // add invitees to meeting
        for (long userId : pollMeetingPostDTO.getInvitees()) {
            User invitee = userService.getUser(userId);
            pollMeetingService.addInvitee(createdPollMeeting, invitee);
        }

        // Add creator's name to return object
        createdPollMeeting.setCreatorName(creator.getName() != null ? creator.getName() : creator.getUsername());

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
            // Get user with provided userID from repository (getUser throws an exception for invalid userId)
            User participant = userService.getUser(pollMeetingPutDTO.getUserId());

            switch (action) {
                case "join" ->
                        // A user joins the session.
                        pollMeetingService.addParticipant(pollMeeting, participant);
                case "decline" ->
                        // A user declines the invitation
                        pollMeetingService.declineInvitation(pollMeeting, participant);
                case "vote" ->
                        // A user casts a vote.
                        pollMeetingService.castVote(pollMeeting, participant, pollMeetingPutDTO.getVote());
            }
        }

        // Get creator's name
        User creator = userService.getUser(pollMeeting.getCreatorId());
        pollMeeting.setCreatorName(creator.getName() != null ? creator.getName() : creator.getUsername());

        return DTOMapper.INSTANCE.convertEntityToPollMeetingGetDTO(pollMeeting);
    }


    /*------------------------------------- DELETE requests --------------------------------------------------------*/

    /**
     * Type: DELETE
     * URL: /poll-meetings/{meetingId}
     * Body: -
     * Protection: check if request is coming from the client (check for special token)
     * @return User
     */
    @DeleteMapping("/poll-meetings/{meetingId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PollMeetingGetDTO deletePollMeeting(@PathVariable long meetingId) {
        // Get meeting with provided meetingId from repository
        PollMeeting pollMeeting = pollMeetingService.getPollMeeting(meetingId);
        pollMeetingService.deletePollMeeting(meetingId);
        return DTOMapper.INSTANCE.convertEntityToPollMeetingGetDTO(pollMeeting);
    }
}
