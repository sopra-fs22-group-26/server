package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.Role;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.RoleRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.awt.image.RescaleOp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder encoder;

    @InjectMocks
    AuthService authService;

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    User user1 = new User();

    @BeforeEach
    public void setup() throws ParseException {
        MockitoAnnotations.openMocks(this);


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
    }

    /**
     * METHOD TESTED: verifyUser()
     * INPUT: valid
     * EXPECTED RESULT: the successfully verified user is returned
     *
     */
    @Test
    public void verifyUser_success() {

        // STUBBING
        given(userRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(user1));
        given(encoder.matches(any(), any())).willReturn(true);

        // EXECUTE METHOD
        User verifiedUser = authService.verifyUser(user1);

        // ASSERTIONS
        Mockito.verify(userRepository, Mockito.atLeastOnce()).findByUsername(anyString());
        assertEquals(user1.getId(), verifiedUser.getId());
        assertEquals(user1.getUsername(), verifiedUser.getUsername());
        assertEquals(user1.getPassword(), verifiedUser.getPassword());
        assertEquals(user1.getEmailAddress(), verifiedUser.getEmailAddress());
        assertEquals(user1.getName(), verifiedUser.getName());
        assertEquals(user1.getBirthDate(), verifiedUser.getBirthDate());
        assertEquals(user1.getCreationDate(), verifiedUser.getCreationDate());
        assertEquals(user1.getScore(), verifiedUser.getScore());
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

        // STUBBING
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
        given(encoder.matches(any(), any())).willReturn(true);

        // ASSERTION
        assertThrows(ResponseStatusException.class, ()->{
            // EXECUTE METHOD
            User verifiedUser = authService.verifyUser(user1);
        });

    }

    /**
     * METHOD TESTED: verifyUser()
     * INPUT: invalid
     * EXPECTED RESULT: wrong password, throws exception
     *
     */
    @Test
    public void verifyUser_WrongPassword_fail() {

        // STUBBING
        given(userRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(user1));
        given(encoder.matches(any(), any())).willReturn(false);

        // ASSERTION
        assertThrows(ResponseStatusException.class, ()->{
            // EXECUTE METHOD
            User verifiedUser = authService.verifyUser(user1);
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

        // STUBBING
        given(userRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(user1));

        // EXECUTE METHOD
        User loggedOutUser = authService.logoutUser(user1);

        // ASSERTIONS
        Mockito.verify(userRepository, Mockito.atLeastOnce()).findByUsername(anyString());
        assertEquals(user1.getId(), loggedOutUser.getId());
        assertEquals(user1.getUsername(), loggedOutUser.getUsername());
        assertEquals(user1.getPassword(), loggedOutUser.getPassword());
        assertEquals(user1.getEmailAddress(), loggedOutUser.getEmailAddress());
        assertEquals(user1.getName(), loggedOutUser.getName());
        assertEquals(user1.getBirthDate(), loggedOutUser.getBirthDate());
        assertEquals(user1.getCreationDate(), loggedOutUser.getCreationDate());
        assertEquals(user1.getScore(), loggedOutUser.getScore());
        // must be true for logged in
        Assertions.assertFalse(loggedOutUser.getLoggedIn());

    }

    /**
     * METHOD TESTED: logoutUser()
     * INPUT: invalid
     * EXPECTED RESULT: wrong username, throws exception
     *
     */
    @Test
    public void logoutUser_WrongUsername_fail() {

        // STUBBING
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

        // ASSERTION
        assertThrows(ResponseStatusException.class, ()->{
            // EXECUTE METHOD
            User loggedOutUser = authService.logoutUser(user1);
        });

    }

}
