package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.SecurityUserDetails;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebAppConfiguration
@SpringBootTest
public class SecurityUserDetailsServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityUserDetailsService securityUserDetailsService;

    User testUser = new User();

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        MockitoAnnotations.openMocks(this);
        testUser.setUsername("Test User");
        testUser.setPassword("123");
        testUser.setLoggedIn(true);
        testUser.setEmailAddress("mail@test.com");
        testUser.setScore(0);
        testUser = userService.createUser(testUser);
    }

    /**
     * Should return a UserDetail object
     */
    @Test
    public void loadUserByUsername_success() {
        // EXECUTE METHOD
        UserDetails createdUserDetails = securityUserDetailsService.loadUserByUsername(testUser.getUsername());
        // ASSERTIONS
        assertEquals(testUser.getUsername(), createdUserDetails.getUsername());
        assertEquals(testUser.getPassword(), createdUserDetails.getPassword());
    }

    /**
     * Should return a UserDetail object
     */
    @Test
    public void loadUserByUsername_fail() {
        // EXECUTE METHOD
        assertThrows(Exception.class, ()->{
           securityUserDetailsService.loadUserByUsername("WrongUsername");
        });

    }

}
