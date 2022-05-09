package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.constant.RoleType;
import ch.uzh.ifi.group26.scrumblebee.entity.Role;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.RoleRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder encoder;

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setup() {
    userRepository.deleteAll();
  }


    /**
     * METHOD TESTED: createUser()
     * INPUT: valid
     * EXPECTED RESULT: user should be created
     */
    @Test
    public void createUser_validInputs_success() {

        // PRE_CONDITIONS
        assertTrue(userRepository.findByUsername("testUsername").isEmpty());

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setEmailAddress("test@mail.com");
        testUser.setPassword("password");

        // EXECUTE METHOD
        User createdUser = userService.createUser(testUser);

        // ASSERTIONS
        assertNotNull(createdUser.getId());
        assertEquals(testUser.getName(), createdUser.getName());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getEmailAddress(), createdUser.getEmailAddress());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertNotNull(createdUser.getCreationDate());
        assertNull(createdUser.getBirthDate());
        assertFalse(createdUser.getLoggedIn());
        assertEquals(0, createdUser.getScore());
        assertNotNull(createdUser.getRoles());

    }


    /**
     * METHOD TESTED: createUser()
     * INPUT: invalid
     * EXPECTED RESULT: user must not be created, username already exists
     */
    @Test
    public void createUser_duplicateUsername_throwsException() {
        // PRE_CONDITIONS
        assertTrue(userRepository.findByUsername("testUsername").isEmpty());

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setEmailAddress("test@mail.com");
        testUser.setPassword("password");
        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setName("testName2");
        testUser2.setUsername("testUsername");
        testUser.setEmailAddress("test@mail.com");
        testUser.setPassword("password");

        // ASSERTIONS
        assertThrows(ResponseStatusException.class, () -> {
            // EXECUTE METHOD
            userService.createUser(testUser2);
        });

      }

    /**
     * METHOD TESTED: createUser()
     * INPUT: invalid
     * EXPECTED RESULT: user must not be created, email address already exists
     */
    @Test
    public void createUser_duplicateEmailAddress_throwsException() {
        // PRE_CONDITIONS
        assertTrue(userRepository.findByEmailAddress("test@mail.com").isEmpty());

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setEmailAddress("test@mail.com");
        testUser.setPassword("password");
        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setName("testName2");
        testUser2.setUsername("testUsername2");
        testUser2.setEmailAddress("test@mail.com");
        testUser2.setPassword("password");

        // ASSERTIONS
        assertThrows(ResponseStatusException.class, () -> {
            // EXECUTE METHOD
            userService.createUser(testUser2);
        });

    }

    /**
     * METHOD TESTED: createUser()
     * INPUT: invalid
     * EXPECTED RESULT: user must not be created, email address already exists
     */
    @Test
    public void createUser_duplicateUsernameAndEmailAddress_throwsException() {
        // PRE_CONDITIONS
        assertTrue(userRepository.findByUsername("testUsername").isEmpty());
        assertTrue(userRepository.findByEmailAddress("test@mail.com").isEmpty());

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setEmailAddress("test@mail.com");
        testUser.setPassword("password");
        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setName("testName2");
        testUser2.setUsername("testUsername");
        testUser2.setEmailAddress("test@mail.com");
        testUser2.setPassword("password");

        // ASSERTIONS
        assertThrows(ResponseStatusException.class, () -> {
            // EXECUTE METHOD
            userService.createUser(testUser2);
        });
    }

    /**
     * METHOD TESTED: updateUser()
     * INPUT: valid
     * EXPECTED RESULT: as the old password is valid, the new password is set
     */
    @Test
    public void updateUser_validInputs_success() throws ParseException {

        // PRE-CONDITION
        assertTrue(userRepository.findByUsername("testUsername").isEmpty());
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setEmailAddress("test@mail.com");
        testUser.setPassword("password");
        userService.createUser(testUser);

        User inputUser = new User();
        inputUser.setPassword("newPassword");
        inputUser.setBirthDate(dateFormat.parse("1920-1-1"));
        inputUser.setName("NewName");
        inputUser.setUsername("NewUsername");
        inputUser.setEmailAddress("newMail@something.com");
        inputUser.setScore(10);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("password");
        User userCredentials = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // EXECUTE METHOD: userToUpdate == createdUser, userInput == inputUser, userCredentials == testUser
        Optional<User> createdUser = userRepository.findByUsername(testUser.getUsername());
        assertTrue(createdUser.isPresent());
        User updatedUser = userService.updateUser(createdUser.get(), inputUser, userCredentials);

        // should stay the same
        assertEquals(testUser.getId(), updatedUser.getId());
        assertEquals(testUser.getCreationDate(), updatedUser.getCreationDate());
        assertEquals(testUser.getLoggedIn(), updatedUser.getLoggedIn());
        // should have changed
        assertEquals(inputUser.getEmailAddress(), updatedUser.getEmailAddress());
        assertEquals(inputUser.getName(), updatedUser.getName());
        assertEquals(inputUser.getUsername(), updatedUser.getUsername());
        assertTrue(encoder.matches(inputUser.getPassword(), updatedUser.getPassword()));
        assertEquals(inputUser.getBirthDate(), updatedUser.getBirthDate());

    }

    @AfterEach
    public void destructor() {userRepository.deleteAll();}


}
