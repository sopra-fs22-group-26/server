package ch.uzh.ifi.group26.scrumblebee.service;


import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;
import ch.uzh.ifi.group26.scrumblebee.constant.PollParticipantStatus;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.PollMeeting;
import ch.uzh.ifi.group26.scrumblebee.entity.PollParticipant;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.PollMeetingRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.PollParticipantRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.TaskRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class PollMeetingServiceIntegrationTest {

    @Autowired
    PollMeetingService pollMeetingService;

    @Autowired
    PollMeetingRepository pollMeetingRepository;

    @Autowired
    PollParticipantRepository pollParticipantRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    PollMeeting testPollMeeting = new PollMeeting();
    Task testTask = new Task();
    User testUser = new User();
    User user1 = new User();
    User user2 = new User();

    @BeforeEach
    @Transactional
    public void setup() throws ParseException {
        userRepository.deleteAll();
        taskRepository.deleteAll();
        pollParticipantRepository.deleteAll();
        pollMeetingRepository.deleteAll();
        assertTrue(userRepository.findAll().isEmpty() &&
                taskRepository.findAll().isEmpty() &&
                pollMeetingRepository.findAll().isEmpty() &&
                pollMeetingRepository.findAll().isEmpty()
        );

        // SETTING UP THE USER THAT CREATES THE MEETING
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        testUser.setEmailAddress("mail@test.com");
        testUser.setName("Test User");
        testUser.setCreationDate(dateFormat.parse("1998-11-18"));
        testUser.setLoggedIn(true);
        testUser.setScore(0);
        testUser = userRepository.save(testUser);
        // SETTING UP THE USER WHICH PARTICIPATE IN THE MEETING
        user1.setUsername("user1");
        user1.setName("User 1");
        user1.setPassword("password");
        user1.setEmailAddress("mail@test.com");
        user1.setCreationDate(dateFormat.parse("1998-11-17"));
        user1.setLoggedIn(true);
        user1.setScore(0);
        user1 = userRepository.save(user1);
        user2.setUsername("user2");
        user2.setName("User 2");
        user2.setPassword("password");
        user2.setEmailAddress("mail@test.com");
        user2.setCreationDate(dateFormat.parse("1998-11-11"));
        user2.setLoggedIn(true);
        user2.setScore(0);
        user2 = userRepository.save(user2);
        userRepository.flush();

        // SETTING UP THE TASK
        testTask.setCreatorId(testUser.getId());
        testTask.setDueDate(dateFormat.parse("2020-11-11"));
        testTask.setTitle("TestTask");
        testTask.setDescription("balalalallalalalla");
        testTask.setEstimate(0);
        testTask.setPriority(TaskPriority.LOW);
        testTask.setLocation("Zurich");
        testTask.setStatus(TaskStatus.ACTIVE);
        testTask.setAssignee(4L);
        testTask.setReporter(5L);
        testTask.setPrivateFlag(false);
        testTask.setComments(new HashSet<>());
        testTask = taskRepository.save(testTask);
        taskRepository.flush();

        // SETTING UP THE MEETING ITSELF
        testPollMeeting.setCreatorId(testUser.getId());
        testPollMeeting.setCreatorName(testUser.getName());
        testPollMeeting.setEstimateThreshold(4);
        testPollMeeting = pollMeetingService.createPollMeeting(testPollMeeting, testTask);
        pollMeetingService.addInvitee(testPollMeeting, testUser);
    }

    @Test
    @Transactional
    public void changeStatus_success() {
        // PRE-CONDITION
        testPollMeeting.setStatus(PollMeetingStatus.OPEN);
        assertEquals(PollMeetingStatus.OPEN, testPollMeeting.getStatus());
        // EXECUTE METHOD
        pollMeetingService.changeStatus(testPollMeeting, PollMeetingStatus.ENDED);
        // ASSERT POST-CONDITION
        assertEquals(PollMeetingStatus.ENDED, testPollMeeting.getStatus());
    }

    @Test
    @Transactional
    public void declineInvitation_success() {
        // PRE-CONDITION
        Set<PollParticipant> participants = testPollMeeting.getParticipants();
        List<PollParticipant> participantList = new ArrayList<>(participants);
        assertEquals(testUser.getId(), participantList.get(0).getUser().getId());
        assertEquals(PollParticipantStatus.INVITED, participantList.get(0).getStatus());
        assertFalse(pollParticipantRepository.findAll().isEmpty());
        // EXECUTE METHOD
        pollMeetingService.declineInvitation(testPollMeeting, testUser);
        // ASSERT POST-CONDITION
        participants = testPollMeeting.getParticipants();
        List<PollParticipant> participantListChanged = new ArrayList<>(participants);
        assertEquals(PollParticipantStatus.DECLINED, participantListChanged.get(0).getStatus());
    }

    @Test
    @Transactional
    public void declineInvitation_fail() {
        // PRE-CONDITION
        Set<PollParticipant> participants = testPollMeeting.getParticipants();
        List<PollParticipant> participantList = new ArrayList<>(participants);
        assertEquals(testUser.getId(), participantList.get(0).getUser().getId());
        participantList.get(0).setStatus(PollParticipantStatus.JOINED);
        assertEquals(PollParticipantStatus.JOINED, participantList.get(0).getStatus());
        assertFalse(pollParticipantRepository.findAll().isEmpty());
        // EXECUTE METHOD
        pollMeetingService.declineInvitation(testPollMeeting, testUser);
        // ASSERT POST-CONDITION
        participants = testPollMeeting.getParticipants();
        List<PollParticipant> participantListChanged = new ArrayList<>(participants);
        assertEquals(PollParticipantStatus.JOINED, participantListChanged.get(0).getStatus());
    }

    @Test
    @Transactional
    public void removeParticipant_success() {
        // PRE-CONDITION
        Set<PollParticipant> participants = testPollMeeting.getParticipants();
        List<PollParticipant> participantList = new ArrayList<>(participants);
        assertEquals(1, participantList.size());
        assertEquals(testUser.getId(), participantList.get(0).getUser().getId());
        assertFalse(pollParticipantRepository.findAll().isEmpty());
        // EXECUTE METHOD
        pollMeetingService.removeParticipant(testPollMeeting, testUser);
        // ASSERT POST-CONDITION
        participants = testPollMeeting.getParticipants();
        List<PollParticipant> participantListChanged = new ArrayList<>(participants);
        assertEquals(0, participantListChanged.size());
    }

    @Test
    @Transactional
    public void castVote_success() {
        // PRE-CONDITION
        Set<PollParticipant> participants = testPollMeeting.getParticipants();
        List<PollParticipant> participantList = new ArrayList<>(participants);
        assertEquals(testUser.getId(), participantList.get(0).getUser().getId());
        assertFalse(pollParticipantRepository.findAll().isEmpty());
        // EXECUTE METHOD
        int vote = 4;
        pollMeetingService.castVote(testPollMeeting, testUser, vote);
        // ASSERT POST-CONDITION
        participants = testPollMeeting.getParticipants();
        List<PollParticipant> participantListChanged = new ArrayList<>(participants);
        assertEquals(vote, participantListChanged.get(0).getVote());
    }

}
