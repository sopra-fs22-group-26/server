package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.constant.RoleType;
import ch.uzh.ifi.group26.scrumblebee.entity.Role;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.RoleRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder encoder;

    @InjectMocks
    UserService userService;

    User testUser = new User();

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");


    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);

        testUser.setId(1L);
        testUser.setEmailAddress("testEmail");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");


        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        when(userRepository.save(any())).thenReturn(testUser);
    }


    /**
     * METHOD TESTED: getUser()
     * INPUT: valid
     * EXPECTED RESULT: an empty list should be returned
     */
    @Test
    void getUsers_NoUsers_success() throws ParseException {

        List<User> listAllUsers = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(listAllUsers);

        // EXECUTE METHOD
        List<User> returnedUsers = userService.getUsers();

        // ASSERTIONS
        assertTrue(returnedUsers.isEmpty());

    }

    /**
     * METHOD TESTED: getUsers()
     * INPUT: valid
     * EXPECTED RESULT: a list of all users should be returned
     */
    @Test
    void getUsers_validInput_success() throws ParseException {

        // STUBBING
        User user1 = new User();
        user1.setId(1L);
        user1.setName("name1");
        user1.setUsername("username1");
        user1.setEmailAddress("test@domain.com");
        user1.setPassword(encoder.encode("Password123"));
        user1.setBirthDate(dateFormat.parse("1998-11-18"));
        user1.setCreationDate(dateFormat.parse("2022-11-18"));
        user1.setLoggedIn(false);
        user1.setScore(0);

        Set<Role> rolesUser1 = new HashSet<>();
        user1.setRoles(rolesUser1);

        User user2 = new User();
        user2.setId(1L);
        user2.setName("name1");
        user2.setUsername("username1");
        user2.setEmailAddress("test@domain.com");
        user2.setPassword(encoder.encode("Password123"));
        user2.setBirthDate(dateFormat.parse("1998-11-18"));
        user2.setCreationDate(dateFormat.parse("2022-11-18"));
        user2.setLoggedIn(false);
        user2.setScore(0);

        Set<Role> rolesUser2 = new HashSet<>();
        user2.setRoles(rolesUser2);

        List<User> listAllUsers = new ArrayList<>();
        listAllUsers.add(user1);
        listAllUsers.add(user2);

        when(userRepository.findAll()).thenReturn(listAllUsers);

        // EXECUTE METHOD
        List<User> returnedUsers = userService.getUsers();

        // ASSERTIONS
        // check user1
        assertEquals(user1.getId(), returnedUsers.get(0).getId());
        assertEquals(user1.getUsername(), returnedUsers.get(0).getUsername());
        assertEquals(user1.getPassword(), returnedUsers.get(0).getPassword());
        assertEquals(user1.getEmailAddress(), returnedUsers.get(0).getEmailAddress());
        assertEquals(user1.getName(), returnedUsers.get(0).getName());
        assertEquals(user1.getBirthDate(), returnedUsers.get(0).getBirthDate());
        assertEquals(user1.getCreationDate(), returnedUsers.get(0).getCreationDate());
        assertEquals(user1.getLoggedIn(), returnedUsers.get(0).getLoggedIn());
        assertEquals(user1.getScore(), returnedUsers.get(0).getScore());

        // check user2
        assertEquals(user1.getId(), returnedUsers.get(1).getId());
        assertEquals(user1.getUsername(), returnedUsers.get(1).getUsername());
        assertEquals(user1.getPassword(), returnedUsers.get(1).getPassword());
        assertEquals(user1.getEmailAddress(), returnedUsers.get(1).getEmailAddress());
        assertEquals(user1.getName(), returnedUsers.get(1).getName());
        assertEquals(user1.getBirthDate(), returnedUsers.get(1).getBirthDate());
        assertEquals(user1.getCreationDate(), returnedUsers.get(1).getCreationDate());
        assertEquals(user1.getLoggedIn(), returnedUsers.get(1).getLoggedIn());
        assertEquals(user1.getScore(), returnedUsers.get(1).getScore());

    }

    /**
     * METHOD TESTED: getUser()
     * INPUT: valid
     * EXPECTED RESULT: the user with id should be returned
     */
    @Test
    void getUser_validInput_success() throws ParseException {

        // STUBBING
        User user1 = new User();
        user1.setId(1L);
        user1.setName("name1");
        user1.setUsername("username1");
        user1.setEmailAddress("test@domain.com");
        user1.setPassword(encoder.encode("Password123"));
        user1.setBirthDate(dateFormat.parse("1998-11-18"));
        user1.setCreationDate(dateFormat.parse("2022-11-18"));
        user1.setLoggedIn(false);
        user1.setScore(0);

        Set<Role> rolesUser1 = new HashSet<>();
        user1.setRoles(rolesUser1);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        // EXECUTE METHOD
        User returnedUser = userService.getUser(user1.getId());

        // ASSERTIONS
        // check user1
        assertEquals(user1.getId(), returnedUser.getId());
        assertEquals(user1.getUsername(), returnedUser.getUsername());
        assertEquals(user1.getPassword(), returnedUser.getPassword());
        assertEquals(user1.getEmailAddress(), returnedUser.getEmailAddress());
        assertEquals(user1.getName(), returnedUser.getName());
        assertEquals(user1.getBirthDate(), returnedUser.getBirthDate());
        assertEquals(user1.getCreationDate(), returnedUser.getCreationDate());
        assertEquals(user1.getLoggedIn(), returnedUser.getLoggedIn());
        assertEquals(user1.getScore(), returnedUser.getScore());

    }

    /**
     * METHOD TESTED: getUser()
     * INPUT: invalid
     * EXPECTED RESULT: when id is not found, an exception should be thrown
     */
    @Test
    void getUser_invalidInput_fail() throws ParseException {

        // STUBBING
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // EXECUTE METHOD
        assertThrows(ResponseStatusException.class, () -> {
            userService.getUser(anyLong());
        });
    }

    /**
     * METHOD TESTED: createUser()
     * INPUT: valid
     * EXPECTED RESULT: as the username and email do not already exist, loggedIn is set to false, a creation date
     *                  is added, the password is hashed and the roles are added. Afterwards, the user is saved
     *                  to the repository.
     */
    @Test
    void createUser_validInputs_success() {

        // STUBBING
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress(anyString())).thenReturn(Optional.empty());
        when(encoder.encode(anyString())).thenReturn(testUser.getPassword());

        Role userRole = new Role();
        userRole.setRoleName(RoleType.ROLE_USER);
        when(roleRepository.findByRoleName(any(RoleType.class))).thenReturn(Optional.of(userRole));

        // EXECUTE METHOD
        User createdUser = userService.createUser(testUser);

        // VERIFY
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getEmailAddress(), createdUser.getEmailAddress());
        assertEquals(testUser.getName(), createdUser.getName());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertNotNull(createdUser.getCreationDate());
        assertNull(createdUser.getBirthDate());
        assertEquals(0, createdUser.getScore());
        assertFalse(createdUser.getLoggedIn());

    }


    /**
     * METHOD TESTED: createUser()
     * INPUT: invalid
     * EXPECTED RESULT: the username already exists (stubbed userRepository returns a full Optional, which simulates
     *                  that a user was found), therefore a ResponseStatusException should be thrown.
     */
    @Test
    void createUser_duplicateUsername_throwsException() {

        // STUBBING
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(testUser));

        // EXECUTE AND EXPECT EXCEPTION
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
        Mockito.verify(userRepository, Mockito.times(0)).save(any());

    }


    /**
     * METHOD TESTED: createUser()
     * INPUT: invalid
     * EXPECTED RESULT: the email address already exists (stubbed userRepository returns a full Optional, which
     *                  simulates that a user was found), therefore a ResponseStatusException should be thrown.
     */
    @Test
    void createUser_duplicateEmailAddress_throwsException() {

        // STUBBING
        when(userRepository.findByEmailAddress(anyString())).thenReturn(Optional.ofNullable(testUser));

        // EXECUTE AND EXPECT EXCEPTION
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
        Mockito.verify(userRepository, Mockito.times(0)).save(any());

    }

    /**
     * METHOD TESTED: createUser()
     * INPUT: invalid
     * EXPECTED RESULT: the username and email address already exists (stubbed userRepository returns a full Optional,
     *                  which simulates that a user was found), therefore a ResponseStatusException should be thrown.
     */
    @Test
    void createUser_duplicateUsernameAndEmailAddress_throwsException() {

        // STUBBING
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(testUser));
        when(userRepository.findByEmailAddress(anyString())).thenReturn(Optional.ofNullable(testUser));

        // EXECUTE AND EXPECT EXCEPTION
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
        Mockito.verify(userRepository, Mockito.times(0)).save(any());

    }

    /**
     * METHOD TESTED: updateUser()
     * INPUT: valid
     * EXPECTED RESULT: as the old password is valid, the new password is set
     */
    @Test
    void updateUser_validInputs_success() throws ParseException {

        // STUBBING
        User inputUser = new User();
        inputUser.setPassword("newPassword");
        inputUser.setBirthDate(dateFormat.parse("1920-1-1"));
        inputUser.setName("NewName");
        inputUser.setUsername("NewUsername");
        inputUser.setEmailAddress("newMail@something.com");
        inputUser.setScore(10);


        User user1 = new User();
        user1.setId(1L);
        user1.setName("name1");
        user1.setUsername("username1");
        user1.setEmailAddress("test@domain.com");
        user1.setPassword(encoder.encode("Password123"));
        user1.setBirthDate(dateFormat.parse("1998-11-18"));
        user1.setCreationDate(dateFormat.parse("2022-11-18"));
        user1.setLoggedIn(false);
        user1.setScore(0);
        Set<Role> rolesUser1 = new HashSet<>();
        user1.setRoles(rolesUser1);

        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        when(encoder.encode(anyString())).thenReturn(inputUser.getPassword());

        // EXECUTE METHOD: userToUpdate == user1, userInput == inputUser, userCredentials == user1
        User updatedUser = userService.updateUser(user1, inputUser, user1);

        // VERIFY
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
        // should stay the same
        assertEquals(testUser.getId(), updatedUser.getId());
        assertEquals(testUser.getCreationDate(), updatedUser.getCreationDate());
        assertEquals(testUser.getLoggedIn(), updatedUser.getLoggedIn());
        assertEquals(testUser.getRoles(), updatedUser.getRoles());
        // should have changed
        assertEquals(testUser.getEmailAddress(), updatedUser.getEmailAddress());
        assertEquals(testUser.getName(), updatedUser.getName());
        assertEquals(testUser.getUsername(), updatedUser.getUsername());
        assertEquals(testUser.getPassword(), updatedUser.getPassword());
        assertEquals(testUser.getBirthDate(), updatedUser.getBirthDate());
        //assertTrue(inputUser.getScore() <= updatedUser.getScore());

    }

    /**
     * METHOD TESTED: updateUser()
     * INPUT: valid
     * EXPECTED RESULT: as the old password is invalid, an exception is thrown
     */
    @Test
    void updateUser_invalidPassword_fail() throws ParseException {

        User inputUser = new User();
        inputUser.setPassword("newPassword");
        inputUser.setBirthDate(dateFormat.parse("1920-1-1"));
        inputUser.setName("NewName");
        inputUser.setUsername("NewUsername");
        inputUser.setEmailAddress("newMail@something.com");
        inputUser.setScore(10);

        // STUBBING
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        // VERIFY
        assertThrows(ResponseStatusException.class, ()->{
            // EXECUTE METHOD: userToUpdate == testUser, userInput == inputUser, userCredentials == testUser
            User updateUser = userService.updateUser(testUser, inputUser, testUser);
        });
        Mockito.verify(userRepository, Mockito.times(0)).save(any());
    }


    /**
     * METHOD TESTED: getUserIdFromUsername()
     * INPUT: valid
     * EXPECTED RESULT: the id of user with username should be returned
     */
    @Test
    void getUserId_validInput_success() throws ParseException {

        // STUBBING
        User user1 = new User();
        user1.setId(1L);
        user1.setName("name1");
        user1.setUsername("username1");
        user1.setEmailAddress("test@domain.com");
        user1.setPassword(encoder.encode("Password123"));
        user1.setBirthDate(dateFormat.parse("1998-11-18"));
        user1.setCreationDate(dateFormat.parse("2022-11-18"));
        user1.setLoggedIn(false);
        user1.setScore(0);

        Set<Role> rolesUser1 = new HashSet<>();
        user1.setRoles(rolesUser1);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user1));

        // EXECUTE METHOD
        long userId = userService.getUserIdFromUsername(user1.getUsername());

        // ASSERTIONS
        assertEquals(user1.getId(), userId);
    }


    /**
     * METHOD TESTED: getUserIdFromUsername()
     * INPUT: invalid
     * EXPECTED RESULT: when username is not found, an exception should be thrown
     */
    @Test
    void getUserId_invalidInput_fail() throws ParseException {

        // STUBBING
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // EXECUTE METHOD
        assertThrows(ResponseStatusException.class, () -> {
            userService.getUserIdFromUsername("invalidUsername");
        });
    }


    @Test
    void rateUser_success() throws ParseException {
        // STUBBING
        User user1 = new User();
        user1.setId(1L);
        user1.setName("name1");
        user1.setUsername("username1");
        user1.setEmailAddress("test@domain.com");
        user1.setPassword(encoder.encode("Password123"));
        user1.setBirthDate(dateFormat.parse("1998-11-18"));
        user1.setCreationDate(dateFormat.parse("2022-11-18"));
        user1.setLoggedIn(false);
        user1.setScore(10);
        Set<Role> rolesUser1 = new HashSet<>();
        user1.setRoles(rolesUser1);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        // EXECUTE METHOD: Rating of 4 should be added to the score of 10 => New score = 14
        userService.rateUser(user1.getId(), 4);
        assertEquals(14, user1.getScore());
    }

}
