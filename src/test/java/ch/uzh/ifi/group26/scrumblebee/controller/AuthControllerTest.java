package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.*;
import ch.uzh.ifi.group26.scrumblebee.repository.RefreshTokenRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.RefreshPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserPostDTO;
import ch.uzh.ifi.group26.scrumblebee.security.utils.JwtUtils;
import ch.uzh.ifi.group26.scrumblebee.service.AuthService;
import ch.uzh.ifi.group26.scrumblebee.service.RefreshTokenService;
import ch.uzh.ifi.group26.scrumblebee.service.SecurityUserDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest
public class AuthControllerTest {

    private MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @MockBean
    SecurityUserDetailsService securityUserDetailsService;

    @MockBean
    RefreshTokenService refreshTokenService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    PasswordEncoder encoder;

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    User user1 = new User();

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

    @Autowired
    JwtUtils jwtUtils;


    /**
     * Type: POST
     * URL: /auth/logout
     * INPUT: valid
     * @throws Exception
     */
    @Test
    public void logoutSuccess_thenReturnJsonArray() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username1");

        given(authService.logoutUser(user1)).willReturn(user1);

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isOk());

    }

    /**
     * Type: POST
     * URL: /auth/logout
     * INPUT: invalid
     * @throws Exception
     */
    @Test
    public void logoutFail_return404() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username2");

        given(authService.logoutUser(any(User.class))).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isNotFound());

    }

    /**
     * Type: POST
     * URL: /auth/login
     * INPUT: valid, returns 0 users
     * @throws Exception
     */
    @Test
    public void loginSuccess_thenReturnJsonArray() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username1");
        userPostDTO.setPassword(encoder.encode("Password123"));

        UserDetails userDetails = SecurityUserDetails.build(user1);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user1);
        refreshToken.setExpiryDate(Instant.now().plusMillis(20000));
        refreshToken.setToken(UUID.randomUUID().toString());

        when(authService.verifyUser(any(User.class))).thenReturn(user1);
        when(securityUserDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(refreshTokenService.createRefreshToken(anyLong())).thenReturn(refreshToken);

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId().intValue()))
                .andExpect(jsonPath("$.username").value(user1.getUsername()))
                .andExpect(jsonPath("$.name").value(user1.getName()))
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.refreshToken").isString());

    }


    /**
     * Handles the logout process. User is identified by their username.
     * Type: POST
     * URL: /auth/login
     * INPUT: valid, returns 0 users
     * @throws Exception
     */
    @Test
    public void loginFail_return404() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username2");
        userPostDTO.setPassword(encoder.encode("wrong"));

        given(authService.verifyUser(any(User.class))).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isNotFound());

    }

    /**
     * Type: POST
     * URL: /auth/refreshtoken
     * INPUT: valid, returns 0 users
     * @throws Exception
     */
    @Test
    public void refreshTokenSuccess_thenReturnJsonArray() throws Exception {

        String token = UUID.randomUUID().toString();
        RefreshPostDTO refreshPostDTO = new RefreshPostDTO();
        refreshPostDTO.setRefreshToken(token);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user1);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(Instant.now().plusMillis(200000));

        given(refreshTokenService.findByToken(anyString())).willReturn(Optional.of(refreshToken));
        given(refreshTokenService.verifyExpiration(any(RefreshToken.class))).willReturn(refreshToken);

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/auth/refreshtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(refreshPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId().intValue()))
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.refreshToken").isString());

    }


    /**
     * Type: POST
     * URL: /auth/refreshtoken
     * INPUT: invalid, returns 0 users
     * @throws Exception
     */
    @Test
    public void refreshTokenFail_return401() throws Exception {

        String token = UUID.randomUUID().toString();
        RefreshPostDTO refreshPostDTO = new RefreshPostDTO();
        refreshPostDTO.setRefreshToken(token);

        given(refreshTokenService.findByToken(anyString())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/auth/refreshtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(refreshPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isUnauthorized());

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
