package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class AuthServiceIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    UserPostDTO inputUser = new UserPostDTO();

    @BeforeEach
    public void setup() {

        userRepository.deleteAll();

        inputUser.setUsername("testUsername");
        inputUser.setName("testName");
        inputUser.setEmailAddress("test@amil.com");
        inputUser.setPassword("password123");

    }

    /**
     * METHOD TESTED: verifyUser()
     * INPUT: valid
     * EXPECTED RESULT: the successfully verified user is returned
     *
     */
    @Test
    public void verifyUser_success() {

        assertTrue(userRepository.findAll().isEmpty());
        User createdUser = userService.createUser(DTOMapper.INSTANCE.convertUserPostDTOtoEntity(inputUser));
        assertTrue(userRepository.findByUsername(createdUser.getUsername()).isPresent());

        UserPostDTO loginCredentials = new UserPostDTO();
        loginCredentials.setUsername(inputUser.getUsername());
        loginCredentials.setPassword(inputUser.getPassword());

        // EXECUTE METHOD
        User verifiedUser = authService.verifyUser(DTOMapper.INSTANCE.convertUserPostDTOtoEntity(loginCredentials));

        // ASSERTIONS
        assertEquals(createdUser.getId(), verifiedUser.getId());
        assertEquals(createdUser.getUsername(), verifiedUser.getUsername());
        assertEquals(createdUser.getPassword(), verifiedUser.getPassword());
        assertEquals(createdUser.getEmailAddress(), verifiedUser.getEmailAddress());
        assertEquals(createdUser.getName(), verifiedUser.getName());
        assertEquals(createdUser.getBirthDate(), verifiedUser.getBirthDate());
        assertEquals(createdUser.getCreationDate(), verifiedUser.getCreationDate());
        assertEquals(createdUser.getScore(), verifiedUser.getScore());
        // must be true for logged in
        Assertions.assertTrue(verifiedUser.getLoggedIn());

    }

    /**
     * METHOD TESTED: verifyUser()
     * INPUT: invalid
     * EXPECTED RESULT: wrong username, throws exception
     *
     */
    @Test
    public void verifyUser_WrongUsername_fail() {

        assertTrue(userRepository.findAll().isEmpty());
        User createdUser = userService.createUser(DTOMapper.INSTANCE.convertUserPostDTOtoEntity(inputUser));
        assertTrue(userRepository.findByUsername(createdUser.getUsername()).isPresent());

        UserPostDTO loginCredentials = new UserPostDTO();
        loginCredentials.setUsername("wrongUsername");
        loginCredentials.setPassword(inputUser.getPassword());

        // ASSERTIONS
        assertThrows(ResponseStatusException.class, ()->{
            // EXECUTE METHOD
            authService.verifyUser(DTOMapper.INSTANCE.convertUserPostDTOtoEntity(loginCredentials));
        });

    }

    /**
     * METHOD TESTED: verifyUser()
     * INPUT: invalid
     * EXPECTED RESULT: wrong username and password, throws exception
     *
     */
    @Test
    public void verifyUser_WrongPassword_fail() {

        assertTrue(userRepository.findAll().isEmpty());
        User createdUser = userService.createUser(DTOMapper.INSTANCE.convertUserPostDTOtoEntity(inputUser));
        assertTrue(userRepository.findByUsername(createdUser.getUsername()).isPresent());

        UserPostDTO loginCredentials = new UserPostDTO();
        loginCredentials.setUsername(inputUser.getUsername());
        loginCredentials.setPassword("wrongPassword");

        // ASSERTIONS
        assertThrows(ResponseStatusException.class, ()->{
            // EXECUTE METHOD
            authService.verifyUser(DTOMapper.INSTANCE.convertUserPostDTOtoEntity(loginCredentials));
        });

    }

    /**
     * METHOD TESTED: verifyUser()
     * INPUT: valid
     * EXPECTED RESULT: the successfully verified user is returned
     *
     */
    @Test
    public void logoutUser_success() {

        assertTrue(userRepository.findAll().isEmpty());
        User createdUser = userService.createUser(DTOMapper.INSTANCE.convertUserPostDTOtoEntity(inputUser));
        assertTrue(userRepository.findByUsername(createdUser.getUsername()).isPresent());

        UserPostDTO logoutUser = new UserPostDTO();
        logoutUser.setUsername(createdUser.getUsername());

        // EXECUTE METHOD
        User loggedOutUser = authService.logoutUser(DTOMapper.INSTANCE.convertUserPostDTOtoEntity(logoutUser));

        // ASSERTIONS
        assertEquals(createdUser.getId(), loggedOutUser.getId());
        assertEquals(createdUser.getUsername(), loggedOutUser.getUsername());
        assertEquals(createdUser.getPassword(), loggedOutUser.getPassword());
        assertEquals(createdUser.getEmailAddress(), loggedOutUser.getEmailAddress());
        assertEquals(createdUser.getName(), loggedOutUser.getName());
        assertEquals(createdUser.getBirthDate(), loggedOutUser.getBirthDate());
        assertEquals(createdUser.getCreationDate(), loggedOutUser.getCreationDate());
        assertEquals(createdUser.getScore(), loggedOutUser.getScore());
        // must be true for logged in
        assertFalse(loggedOutUser.getLoggedIn());

    }

    /**
     * METHOD TESTED: logoutUser()
     * INPUT: invalid
     * EXPECTED RESULT: wrong username, throws exception
     *
     */
    @Test
    public void logoutUser_WrongUsername_fail() {

        assertTrue(userRepository.findAll().isEmpty());

        UserPostDTO logoutUserDTO = new UserPostDTO();
        logoutUserDTO.setUsername("wrongUsername");

        User logoutUserEntity = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(logoutUserDTO);

        // ASSERTIONS
        assertThrows(ResponseStatusException.class, ()->{
            // EXECUTE METHOD
            User loggedOutUser = authService.logoutUser(logoutUserEntity);
        });

    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

}
