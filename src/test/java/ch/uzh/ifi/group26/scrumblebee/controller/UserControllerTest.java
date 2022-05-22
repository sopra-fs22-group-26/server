package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.Role;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserPutDTO;
import ch.uzh.ifi.group26.scrumblebee.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebAppConfiguration
@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    PasswordEncoder encoder;

    private final Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    private User user1 = new User();

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        user1.setId(1L);
        user1.setName("name1");
        user1.setUsername("username1");
        user1.setEmailAddress("test@domain.com");
        user1.setPassword(encoder.encode("Password123"));
        user1.setBirthDate(dateFormat.parse("1998-11-18"));
        user1.setCreationDate(dateFormat.parse("2022-11-18"));
        user1.setLoggedIn(false);
        user1.setScore(0);

        Set<Role> roles = new HashSet<>();
        user1.setRoles(roles);

    }


    /* ------------------------------------------------- GET endpoints ------------------------------------------- */


    /**
     * type: GET
     * url: /users
     * INPUT: valid, returns 0 users
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getUsers_zeroUsers_thenReturnJsonArray() throws Exception {

        List<User> allUsers = new ArrayList<>();

        given(userService.getUsers()).willReturn(allUsers);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    /**
     * type: GET
     * url: /users
     * INPUT: valid, returns 1 user
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getUsers_oneUser_thenReturnJsonArray() throws Exception {

        List<User> allUsers = Collections.singletonList(user1);

        given(userService.getUsers()).willReturn(allUsers);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(user1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(user1.getName())))
                .andExpect(jsonPath("$[0].username", is(user1.getUsername())))
                .andExpect(jsonPath("$[0].emailAddress", is(user1.getEmailAddress())))
                .andExpect(jsonPath("$[0].birthDate", is(dateFormat.format(user1.getBirthDate()))))
                .andExpect(jsonPath("$[0].creationDate", is(dateFormat.format(user1.getCreationDate()))))
                .andExpect(jsonPath("$[0].loggedIn", is(user1.getLoggedIn())))
                .andExpect(jsonPath("$[0].score", is(user1.getScore())));

    }

    /**
     * type: GET
     * url: /users
     * INPUT: valid, returns 2 users
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getUsers_multipleUsers_thenReturnJsonArray() throws Exception {

        User user2 = new User();
        user2.setId(2L);
        user2.setName("name2");
        user2.setUsername("username2");
        user2.setEmailAddress("test@domain.com");
        user2.setPassword(encoder.encode("Password123"));
        user2.setBirthDate(dateFormat.parse("1998-11-18"));
        user2.setCreationDate(dateFormat.parse("2022-11-18"));
        user2.setLoggedIn(false);
        user2.setScore(0);

        Set<Role> roles = new HashSet<>();
        user2.setRoles(roles);

        List<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);


        given(userService.getUsers()).willReturn(allUsers);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(user1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(user1.getName())))
                .andExpect(jsonPath("$[0].username", is(user1.getUsername())))
                .andExpect(jsonPath("$[0].emailAddress", is(user1.getEmailAddress())))
                .andExpect(jsonPath("$[0].birthDate", is(dateFormat.format(user1.getBirthDate()))))
                .andExpect(jsonPath("$[0].creationDate", is(dateFormat.format(user1.getCreationDate()))))
                .andExpect(jsonPath("$[0].loggedIn", is(user1.getLoggedIn())))
                .andExpect(jsonPath("$[0].score", is(user1.getScore())))
                .andExpect(jsonPath("$[1].id", is(user2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(user2.getName())))
                .andExpect(jsonPath("$[1].username", is(user2.getUsername())))
                .andExpect(jsonPath("$[1].emailAddress", is(user2.getEmailAddress())))
                .andExpect(jsonPath("$[1].birthDate", is(dateFormat.format(user2.getBirthDate()))))
                .andExpect(jsonPath("$[1].creationDate", is(dateFormat.format(user2.getCreationDate()))))
                .andExpect(jsonPath("$[1].loggedIn", is(user2.getLoggedIn())))
                .andExpect(jsonPath("$[1].score", is(user2.getScore())));

    }


    /**
     * type: GET
     * URL: /users/{userId}
     * INPUT: valid
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getUserById_validInput_returnUserWithId() throws Exception {

        given(userService.getUser(user1.getId())).willReturn(user1);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get(
                "/users/{userId}", user1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId().intValue())))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.username", is(user1.getUsername())))
                .andExpect(jsonPath("$.emailAddress", is(user1.getEmailAddress())))
                .andExpect(jsonPath("$.birthDate", is(dateFormat.format(user1.getBirthDate()))))
                .andExpect(jsonPath("$.creationDate", is(dateFormat.format(user1.getCreationDate()))))
                .andExpect(jsonPath("$.loggedIn", is(user1.getLoggedIn())))
                .andExpect(jsonPath("$.score", is(user1.getScore())));

    }

    /**
     * type: GET
     * URL: /users/{userId}
     * INPUT: invalid
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void getUserById_invalidInput_return404() throws Exception {

        given(userService.getUser(999L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get(
                "/users/{userId}", 999L)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }


    /* ----------------------------------------------- POST endpoints ------------------------------------------- */

    /**
     * type: POST
     * url: /users
     * INPUT: valid
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void createUser_validInput_userCreated() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setEmailAddress("t@domain.com");
        userPostDTO.setPassword("Password123");

        given(userService.createUser(any())).willReturn(user1);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user1.getId().intValue())))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.username", is(user1.getUsername())))
                .andExpect(jsonPath("$.emailAddress", is(user1.getEmailAddress())))
                .andExpect(jsonPath("$.loggedIn", is(user1.getLoggedIn())))
                .andExpect(jsonPath("$.birthDate", is(dateFormat.format(user1.getBirthDate()))))
                .andExpect(jsonPath("$.creationDate", is(dateFormat.format(user1.getCreationDate()))))
                .andExpect(jsonPath("$.score", is(user1.getScore())));

    }


    /**
     * type: POST
     * url: /users
     * INPUT: invalid
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void createUser_invalidInput_return409() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("Password123");

        given(userService.createUser(any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }


    /* ----------------------------------------------- PUT endpoints ------------------------------------------- */


    /**
     * type: PUT
     * url: /users/{id}
     * INPUT: valid
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void updateUser_validInput_userUpdated() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("someNewUsername");
        userPutDTO.setName("newName");
        userPutDTO.setEmailAddress("new@domain.com");
        userPutDTO.setBirthDate("2004-11-18");

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = MockMvcRequestBuilders.put(
                "/users/{id}", user1.getId().intValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        mockMvc.perform(putRequest).andExpect(status().isNoContent());
    }

    /**
     * type: PUT
     * url: /users/{id}
     * INPUT: invalid
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void updateUser_invalidInput_return404() throws Exception {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("any@username");

        given(userService.getUser(anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = MockMvcRequestBuilders.put("/users/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().is(404));
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
}