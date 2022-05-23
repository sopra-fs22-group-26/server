package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.constant.PollMeetingStatus;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.*;
import ch.uzh.ifi.group26.scrumblebee.repository.PollMeetingRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.PollParticipantRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.TaskRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.PollMeetingGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.PollMeetingPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.PollMeetingPutDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import ch.uzh.ifi.group26.scrumblebee.service.PollMeetingService;
import ch.uzh.ifi.group26.scrumblebee.service.TaskService;
import ch.uzh.ifi.group26.scrumblebee.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest
public class PollMeetingControllerIntegrationTest {

    private MockMvc mockMvc;

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

    @Autowired
    WebApplicationContext context;

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    PollMeeting testPollMeeting = new PollMeeting();
    Task testTask = new Task();
    User testUser = new User();
    User user1 = new User();
    User user2 = new User();

    @BeforeEach
    @Transactional
    public void setup() throws Exception {
        userRepository.deleteAll();
        taskRepository.deleteAll();
        pollParticipantRepository.deleteAll();
        pollMeetingRepository.deleteAll();
        assertTrue(userRepository.findAll().isEmpty() &&
                        taskRepository.findAll().isEmpty() &&
                        pollMeetingRepository.findAll().isEmpty() &&
                        pollMeetingRepository.findAll().isEmpty()
                );
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
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
        pollMeetingService.addParticipant(testPollMeeting, testUser);
        pollMeetingService.addInvitee(testPollMeeting, user1);
        pollMeetingService.addInvitee(testPollMeeting, user2);
    }

    /**
     * type: GET
     * url: /poll-meetings
     * INPUT: valid, returns 0 meetings
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getAllPollMeetings_zeroMeetings_success() throws Exception {
        // PREPARATION
        pollParticipantRepository.deleteAll();
        pollMeetingRepository.deleteAll();
        assertTrue(
                pollMeetingRepository.findAll().isEmpty() &&
                pollMeetingRepository.findAll().isEmpty()
        );

        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/poll-meetings")
                .contentType(MediaType.APPLICATION_JSON);

        // PERFORM AND ASSERT
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    /**
     * type: GET
     * url: /poll-meetings
     * INPUT: valid, returns 1 meeting
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getAllPollMeetings_oneMeeting_success() throws Exception {
        // PRE-REQUISITES
        assertEquals(1, pollMeetingRepository.findAll().size());
        assertEquals(testPollMeeting.getMeetingId(), pollMeetingRepository.findAll().get(0).getMeetingId());
        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/poll-meetings")
                .contentType(MediaType.APPLICATION_JSON);
        // PERFORM AND ASSERT
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].meetingId").value(testPollMeeting.getMeetingId().intValue()))
                .andExpect(jsonPath("$[0].creatorId").value(testPollMeeting.getCreatorId().intValue()))
                .andExpect(jsonPath("$[0].creatorName").value(testPollMeeting.getCreatorName()))
                .andExpect(jsonPath("$[0].task.taskId").value(testPollMeeting.getTask().getTaskId()))
                .andExpect(jsonPath("$[0].task.creatorId").value(testPollMeeting.getTask().getCreatorId().intValue()))
                .andExpect(jsonPath("$[0].task.dueDate").value(dateFormat.format(testPollMeeting.getTask().getDueDate())))
                .andExpect(jsonPath("$[0].task.title").value(testPollMeeting.getTask().getTitle()))
                .andExpect(jsonPath("$[0].task.description").value(testPollMeeting.getTask().getDescription()))
                .andExpect(jsonPath("$[0].task.estimate").value(testPollMeeting.getTask().getEstimate()))
                .andExpect(jsonPath("$[0].task.priority").value(testPollMeeting.getTask().getPriority().toString()))
                .andExpect(jsonPath("$[0].task.location").value(testPollMeeting.getTask().getLocation()))
                .andExpect(jsonPath("$[0].task.status").value(testPollMeeting.getTask().getStatus().toString()))
                .andExpect(jsonPath("$[0].task.score").value(testPollMeeting.getTask().getScore()))
                .andExpect(jsonPath("$[0].task.assignee").value(testPollMeeting.getTask().getAssignee()))
                .andExpect(jsonPath("$[0].task.reporter").value(testPollMeeting.getTask().getReporter()))
                .andExpect(jsonPath("$[0].task.privateFlag").value(testPollMeeting.getTask().getPrivateFlag()))
                .andExpect(jsonPath("$[0].task.nofComments").value(0))
                .andExpect(jsonPath("$[0].estimateThreshold").value(testPollMeeting.getEstimateThreshold()))
                .andExpect(jsonPath("$[0].averageEstimate").value(testPollMeeting.getAverageEstimate()));
    }

    /**
     * type: GET
     * url: /poll-meetings
     * INPUT: valid, returns creator name
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getAllPollMeetings_CreatorName_useCreatorName_success() throws Exception {
        // PRE-REQUISITES
        assertEquals(1, pollMeetingRepository.findAll().size());
        assertEquals(testPollMeeting.getMeetingId(), pollMeetingRepository.findAll().get(0).getMeetingId());
        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/poll-meetings")
                .contentType(MediaType.APPLICATION_JSON);
        // PERFORM AND ASSERT
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].creatorName").value(testUser.getName()));
    }

    /**
     * type: GET
     * url: /poll-meetings
     * INPUT: valid, returns creator username
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getAllPollMeetings_noCreatorName_useCreatorUsername_success() throws Exception {
        // PRE-REQUISITES
        assertEquals(1, pollMeetingRepository.findAll().size());
        assertEquals(testPollMeeting.getMeetingId(), pollMeetingRepository.findAll().get(0).getMeetingId());
        // REMOVE THE NAME OF THE CREATOR ==> USERNAME SHOULD BE USED
        testUser.setName(null);
        userRepository.save(testUser);
        userRepository.flush();
        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/poll-meetings")
                .contentType(MediaType.APPLICATION_JSON);
        // PERFORM AND ASSERT
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].creatorName").value(testUser.getUsername()));
    }

    /**
     * type: GET
     * url: /poll-meetings/{meetingId}
     * INPUT: valid, returns creator username
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getPollMeeting_success() throws Exception {
        // PRE-REQUISITES
        assertEquals(1, pollMeetingRepository.findAll().size());
        assertEquals(testPollMeeting.getMeetingId(), pollMeetingRepository.findAll().get(0).getMeetingId());
        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders.get("/poll-meetings/{meetingId}", testPollMeeting.getMeetingId().intValue())
                .contentType(MediaType.APPLICATION_JSON);
        // PERFORM AND ASSERT
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.meetingId").value(testPollMeeting.getMeetingId().intValue()));
    }

    /**
     * type: GET
     * url: /poll-meetings/{meetingId}
     * INPUT: invalid, returns creator username
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getPollMeeting_fail() throws Exception {
        // PRE-REQUISITES
        assertEquals(1, pollMeetingRepository.findAll().size());
        assertEquals(testPollMeeting.getMeetingId(), pollMeetingRepository.findAll().get(0).getMeetingId());
        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders.get("/poll-meetings/{meetingId}",
                                testPollMeeting.getMeetingId().intValue() + 1)
                        .contentType(MediaType.APPLICATION_JSON);
        // PERFORM AND ASSERT
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    /**
     * type: GET
     * url: /poll-meetings/{meetingId}
     * INPUT: valid, returns creator name
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getPollMeeting_CreatorName_useCreatorName_success() throws Exception {
        // PRE-REQUISITES
        assertEquals(1, pollMeetingRepository.findAll().size());
        assertEquals(testPollMeeting.getMeetingId(), pollMeetingRepository.findAll().get(0).getMeetingId());
        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders.get("/poll-meetings/{meetingId}",
                        testPollMeeting.getMeetingId().intValue())
                                    .contentType(MediaType.APPLICATION_JSON);
        // PERFORM AND ASSERT
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.creatorName").value(testUser.getName()));
    }

    /**
     * type: GET
     * url: /poll-meetings/{meetingId}
     * INPUT: valid, returns creator username
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getPollMeeting_noCreatorName_useCreatorUsername_success() throws Exception {
        // PRE-REQUISITES
        assertEquals(1, pollMeetingRepository.findAll().size());
        assertEquals(testPollMeeting.getMeetingId(), pollMeetingRepository.findAll().get(0).getMeetingId());
        // REMOVE THE NAME OF THE CREATOR ==> USERNAME SHOULD BE USED
        testUser.setName(null);
        userRepository.save(testUser);
        userRepository.flush();
        // BUILD REQUEST
        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders.get("/poll-meetings/{meetingId}",
                                testPollMeeting.getMeetingId().intValue())
                                    .contentType(MediaType.APPLICATION_JSON);
        // PERFORM AND ASSERT
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.creatorName").value(testUser.getUsername()));
    }


    /**
     * type: POST
     * url: /poll-meetings
     * INPUT: valid, returns created poll-meeting
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void createPollMeeting_success() throws Exception {
        // PRE-REQUISITES
        assertTrue(userRepository.findById(testUser.getId()).isPresent());
        assertTrue(userRepository.findById(user1.getId()).isPresent());
        assertTrue(userRepository.findById(user2.getId()).isPresent());
        assertTrue(taskRepository.findByTaskId(testTask.getTaskId()).isPresent());
        pollMeetingRepository.deleteAll();
        assertTrue(pollMeetingRepository.findAll().isEmpty());
        pollParticipantRepository.deleteAll();
        assertTrue(pollMeetingRepository.findAll().isEmpty());
        // BUILD THE REQUEST
        PollMeetingPostDTO requestBody = new PollMeetingPostDTO();
        requestBody.setCreatorId(testUser.getId());
        requestBody.setTaskId(testTask.getTaskId());
        requestBody.setEstimateThreshold(testPollMeeting.getEstimateThreshold());
        List<Long> invitees = new ArrayList<>();
        invitees.add(user1.getId());
        invitees.add(user2.getId());
        requestBody.setInvitees(invitees);
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/poll-meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody));
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());
        // ASSERTIONS
        assertEquals(1, pollMeetingRepository.findAll().size());
        PollMeeting createdMeeting = pollMeetingRepository.findAll().get(0);
        assertEquals(testTask.getTaskId(), createdMeeting.getTask().getTaskId());
        assertEquals(testUser.getId(), createdMeeting.getCreatorId());
        assertEquals(1, pollMeetingRepository.findAll().size());
        assertEquals(createdMeeting.getMeetingId(), pollMeetingRepository.findAll().get(0).getMeetingId());
        assertEquals(testUser.getId(), pollParticipantRepository.findAll().get(0).getUser().getId());

    }

    /**
     * type: POST
     * url: /poll-meetings
     * INPUT: invalid, returns 404 because creator id does not exist
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void createPollMeeting_creatorDoesNotExist_fail() throws Exception {
        // PRE-REQUISITES
        assertTrue(userRepository.findById(testUser.getId()).isPresent());
        assertTrue(userRepository.findById(user1.getId()).isPresent());
        assertTrue(userRepository.findById(user2.getId()).isPresent());
        assertTrue(taskRepository.findByTaskId(testTask.getTaskId()).isPresent());
        pollMeetingRepository.deleteAll();
        assertTrue(pollMeetingRepository.findAll().isEmpty());
        pollParticipantRepository.deleteAll();
        assertTrue(pollMeetingRepository.findAll().isEmpty());
        // BUILD THE REQUEST
        PollMeetingPostDTO requestBody = new PollMeetingPostDTO();
        requestBody.setCreatorId(99999L);
        requestBody.setTaskId(testTask.getTaskId());
        requestBody.setEstimateThreshold(testPollMeeting.getEstimateThreshold());
        List<Long> invitees = new ArrayList<>();
        invitees.add(user1.getId());
        invitees.add(user2.getId());
        requestBody.setInvitees(invitees);
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/poll-meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody));
        mockMvc.perform(postRequest).andExpect(status().isNotFound());
        // ASSERTIONS
        assertTrue(pollMeetingRepository.findAll().isEmpty());
        assertTrue(pollParticipantRepository.findAll().isEmpty());

    }

    /**
     * type: POST
     * url: /poll-meetings
     * INPUT: invalid, returns 404 because task id is not found
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void createPollMeeting_taskDoesNotExist_fail() throws Exception {
        // PRE-REQUISITES
        assertTrue(userRepository.findById(testUser.getId()).isPresent());
        assertTrue(userRepository.findById(user1.getId()).isPresent());
        assertTrue(userRepository.findById(user2.getId()).isPresent());
        assertTrue(taskRepository.findByTaskId(testTask.getTaskId()).isPresent());
        pollMeetingRepository.deleteAll();
        assertTrue(pollMeetingRepository.findAll().isEmpty());
        pollParticipantRepository.deleteAll();
        assertTrue(pollMeetingRepository.findAll().isEmpty());
        // BUILD THE REQUEST
        PollMeetingPostDTO requestBody = new PollMeetingPostDTO();
        requestBody.setCreatorId(testUser.getId());
        requestBody.setTaskId(99999L);
        requestBody.setEstimateThreshold(testPollMeeting.getEstimateThreshold());
        List<Long> invitees = new ArrayList<>();
        invitees.add(user1.getId());
        invitees.add(user2.getId());
        requestBody.setInvitees(invitees);
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/poll-meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody));
        mockMvc.perform(postRequest).andExpect(status().isNotFound());
        // ASSERTIONS
        assertTrue(pollMeetingRepository.findAll().isEmpty());
        assertTrue(pollParticipantRepository.findAll().isEmpty());

    }

    /**
     * type: POST
     * url: /poll-meetings
     * INPUT: valid, returns created poll-meeting
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void createPollMeeting_creatorWithName_success() throws Exception {
        // PRE-REQUISITES
        assertTrue(userRepository.findById(testUser.getId()).isPresent());
        assertTrue(userRepository.findById(user1.getId()).isPresent());
        assertTrue(userRepository.findById(user2.getId()).isPresent());
        assertTrue(taskRepository.findByTaskId(testTask.getTaskId()).isPresent());
        pollMeetingRepository.deleteAll();
        assertTrue(pollMeetingRepository.findAll().isEmpty());
        pollParticipantRepository.deleteAll();
        assertTrue(pollMeetingRepository.findAll().isEmpty());
        // BUILD THE REQUEST
        PollMeetingPostDTO requestBody = new PollMeetingPostDTO();
        requestBody.setCreatorId(testUser.getId());
        requestBody.setTaskId(testTask.getTaskId());
        requestBody.setEstimateThreshold(testPollMeeting.getEstimateThreshold());
        List<Long> invitees = new ArrayList<>();
        invitees.add(user1.getId());
        invitees.add(user2.getId());
        requestBody.setInvitees(invitees);
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/poll-meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody));
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.creatorName").value(testUser.getName()));

    }

    /**
     * type: POST
     * url: /poll-meetings
     * INPUT: valid, returns created poll-meeting
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void createPollMeeting_creatorWithUsername_success() throws Exception {
        // PRE-REQUISITES
        userRepository.deleteById(testUser.getId());
        pollMeetingRepository.deleteAll();
        assertTrue(pollMeetingRepository.findAll().isEmpty());
        pollParticipantRepository.deleteAll();
        assertTrue(pollMeetingRepository.findAll().isEmpty());

        User testUserNoName = new User();
        testUserNoName.setUsername("testUser");
        testUserNoName.setPassword("password");
        testUserNoName.setEmailAddress("mail@test.com");
        testUserNoName.setCreationDate(dateFormat.parse("1998-11-18"));
        testUserNoName.setLoggedIn(true);
        testUserNoName.setScore(0);
        testUserNoName = userRepository.save(testUserNoName);

        assertTrue(userRepository.findById(testUserNoName.getId()).isPresent());
        assertTrue(userRepository.findById(user1.getId()).isPresent());
        assertTrue(userRepository.findById(user2.getId()).isPresent());
        assertTrue(taskRepository.findByTaskId(testTask.getTaskId()).isPresent());

        // BUILD THE REQUEST
        PollMeetingPostDTO requestBody = new PollMeetingPostDTO();
        requestBody.setCreatorId(testUserNoName.getId());
        requestBody.setTaskId(testTask.getTaskId());
        requestBody.setEstimateThreshold(testPollMeeting.getEstimateThreshold());
        List<Long> invitees = new ArrayList<>();
        invitees.add(user1.getId());
        invitees.add(user2.getId());
        requestBody.setInvitees(invitees);
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/poll-meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestBody));
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.creatorName").value(testUserNoName.getUsername()));

    }


    /**
     * type: PUT
     * url: /poll-meetings/{meetingId}
     * URL-Parameter: action [join, decline, vote]
     * Body: userId, vote, estimateThreshold, status
     * INPUT: invalid, vote in meeting fail
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void updatePollMeeting_wrongAction_fail() throws Exception {
        MockHttpServletRequestBuilder putRequest =
                MockMvcRequestBuilders.put("/poll-meetings/{meetingId}", testPollMeeting.getMeetingId().intValue())
                        .contentType(MediaType.APPLICATION_JSON);
        // PERFORM
        mockMvc.perform(putRequest).andExpect(status().isBadRequest());
    }

//    /**
//     * type: PUT
//     * url: /poll-meetings/{meetingId}
//     * URL-Parameter: action [join, decline, vote]
//     * Body: userId, vote, estimateThreshold, status
//     * INPUT: invalid, vote in meeting fail
//     * @throws Exception
//     */
//    @Test
//    @WithMockUser
//    public void updatePollMeeting_joinMeeting_success() throws Exception {
//
//        PollMeetingPutDTO putDTO = new PollMeetingPutDTO();
//        putDTO.setUserId(user1.getId());
//
//        MockHttpServletRequestBuilder putRequest =
//                MockMvcRequestBuilders.put("/poll-meetings/{meetingId}?action={action}", testPollMeeting.getMeetingId().intValue(), "join")
//                        .contentType(MediaType.APPLICATION_JSON)
//                                .content(asJsonString(putDTO));
//        // PERFORM
//        mockMvc.perform(putRequest).andExpect(status().isOk());
//    }


    /**
     * type: DELETE
     * url: /poll-meetings/{meetingId}
     * URL-Parameter: action [join, decline, vote]
     * INPUT: valid, delete the poll-meeting
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void deletePollMeeting_success() throws Exception {
        // PRE-REQUISITES
        assertNotNull(pollMeetingRepository.findByMeetingId(testPollMeeting.getMeetingId()));
        // BUILD REQUEST
        MockHttpServletRequestBuilder deleteRequest =
                MockMvcRequestBuilders.delete("/poll-meetings/{meetingId}", testPollMeeting.getMeetingId().intValue())
                        .contentType(MediaType.APPLICATION_JSON);
        // PERFORM
        mockMvc.perform(deleteRequest).andExpect(status().isOk());
        assertNull(pollMeetingRepository.findByMeetingId(testPollMeeting.getMeetingId()));
    }

    /**
     * type: DELETE
     * url: /poll-meetings/{meetingId}
     * URL-Parameter: action [join, decline, vote]
     * INPUT: invalid, return 404 because poll-meeting was not found
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void deletePollMeeting_fail() throws Exception {
        // PRE-REQUISITES
        assertNotNull(pollMeetingRepository.findByMeetingId(testPollMeeting.getMeetingId()));
        // BUILD REQUEST
        MockHttpServletRequestBuilder deleteRequest =
                MockMvcRequestBuilders.delete("/poll-meetings/{meetingId}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON);
        // PERFORM
        mockMvc.perform(deleteRequest).andExpect(status().isNotFound());
    }


    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }


//    @AfterEach
//    @Transactional
//    public void cleanUp() {
//        userRepository.deleteAll();
//        taskRepository.deleteAll();
//        pollParticipantRepository.deleteAll();
//        pollMeetingRepository.deleteAll();
//        assertTrue(userRepository.findAll().isEmpty() &&
//                taskRepository.findAll().isEmpty() &&
//                pollMeetingRepository.findAll().isEmpty() &&
//                pollMeetingRepository.findAll().isEmpty()
//        );
//    }

}
