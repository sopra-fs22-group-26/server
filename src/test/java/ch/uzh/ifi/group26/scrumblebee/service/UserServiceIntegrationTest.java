package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserPutDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
class UserServiceIntegrationTest {

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
    void createUser_validInputs_success() {

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
    void createUser_duplicateUsername_throwsException() {
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
    void createUser_duplicateEmailAddress_throwsException() {
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
    void createUser_duplicateUsernameAndEmailAddress_throwsException() {
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
    void updateUser_validInputs_success() throws ParseException {

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


    /**
     * METHOD TESTED: updateUser()
     * INPUT: invalid
     * EXPECTED RESULT: user must not be updated, username already exists
     */
    @Test
    void updateUser_duplicateUsername_throwsException() {
        // PRE-CONDITION
        assertTrue(userRepository.findByUsername("testUsername").isEmpty());

        // Create 2 users
        User testUser1 = new User();
        testUser1.setName("testName1");
        testUser1.setUsername("testUsername1");
        testUser1.setEmailAddress("test1@mail.com");
        testUser1.setPassword("password1");
        userService.createUser(testUser1);

        User testUser2 = new User();
        testUser2.setName("testName2");
        testUser2.setUsername("testUsername2");
        testUser2.setEmailAddress("test2@mail.com");
        testUser2.setPassword("password2");
        userService.createUser(testUser2);

        // Input parameters
        User inputUser = new User();
        inputUser.setUsername("testUsername1");
        inputUser.setEmailAddress("newMail@something.com");

        // EXECUTE METHOD:
        Optional<User> userToUpdate = userRepository.findByUsername(testUser2.getUsername());
        assertTrue(userToUpdate.isPresent());

        // ASSERTIONS
        assertThrows(ResponseStatusException.class, () -> {
            // EXECUTE METHOD
            userService.updateUser(userToUpdate.get(), inputUser, inputUser);
        });
    }

    /**
     * METHOD TESTED: updateUser()
     * INPUT: invalid
     * EXPECTED RESULT: user must not be updated, email address already exists
     */
    @Test
    void updateUser_duplicateEmailAddress_throwsException() {
        // PRE-CONDITION
        assertTrue(userRepository.findByUsername("testUsername").isEmpty());

        // Create 2 users
        User testUser1 = new User();
        testUser1.setName("testName1");
        testUser1.setUsername("testUsername1");
        testUser1.setEmailAddress("test1@mail.com");
        testUser1.setPassword("password1");
        userService.createUser(testUser1);

        User testUser2 = new User();
        testUser2.setName("testName2");
        testUser2.setUsername("testUsername2");
        testUser2.setEmailAddress("test2@mail.com");
        testUser2.setPassword("password2");
        userService.createUser(testUser2);

        // Input parameters
        User inputUser = new User();
        inputUser.setUsername("newUsername");
        inputUser.setEmailAddress("test1@mail.com");

        // EXECUTE METHOD:
        Optional<User> userToUpdate = userRepository.findByUsername(testUser2.getUsername());
        assertTrue(userToUpdate.isPresent());

        // ASSERTIONS
        assertThrows(ResponseStatusException.class, () -> {
            // EXECUTE METHOD
            userService.updateUser(userToUpdate.get(), inputUser, inputUser);
        });
    }

    @AfterEach
    public void destructor() {userRepository.deleteAll();}


}
