package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.*;
import ch.uzh.ifi.group26.scrumblebee.repository.RefreshTokenRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
class RefreshTokenServiceIntegrationTest {

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    User testUser = new User();

    @BeforeEach
    public void setup() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
        testUser.setId(1L);
        testUser.setUsername("username");
        testUser.setPassword("password");
        testUser.setEmailAddress("mail@domain.com");
        testUser.setCreationDate(java.util.Calendar.getInstance().getTime());
        testUser.setRoles(new HashSet<>());
        testUser.setLoggedIn(false);
        testUser.setScore(0);
        testUser.setPollMeetings(new HashSet<>());
    }

    /**
     * METHOD TESTED: findByToken()
     * INPUT: valid
     * EXPECTED RESULT: the refreshToken is found and returned
     *
     */
    @Test
    void integrationTest_findByToken_success() {
        // PREPARATION
        //  assert repositories are empty
        assertTrue(refreshTokenRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        //  create user and token
        User savedUser = userRepository.save(testUser);
        userRepository.flush();
        RefreshToken tokenToSave = new RefreshToken();
        tokenToSave.setUser(savedUser);
        tokenToSave.setToken("token");
        tokenToSave.setExpiryDate(Instant.now());
        //  save the token
        RefreshToken savedRefreshToken = refreshTokenRepository.save(tokenToSave);
        // EXECUTE METHOD
        Optional<RefreshToken> foundRefreshToken = refreshTokenService.findByToken(savedRefreshToken.getToken());
        // ASSERTIONS
        assertTrue(foundRefreshToken.isPresent());
        assertEquals(savedUser.getId(), foundRefreshToken.get().getUser().getId());
        assertEquals(tokenToSave.getToken(), foundRefreshToken.get().getToken());
        assertNotNull(foundRefreshToken.get().getExpiryDate());
    }

    /**
     * METHOD TESTED: createRefreshToken()
     * INPUT: valid
     * EXPECTED RESULT: the refreshToken is found and returned
     */
    @Test
    void integrationTest_createRefreshToken_success() {
        // PREPARATION
        //  assert repositories are empty
        assertTrue(refreshTokenRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        User savedUser = userRepository.save(testUser);
        userRepository.flush();
        // EXECUTE METHOD
        RefreshToken createdRefreshToken = refreshTokenService.createRefreshToken(savedUser.getId());
        // ASSERTIONS
        assertEquals(savedUser.getId(), createdRefreshToken.getUser().getId());
        assertNotNull(createdRefreshToken.getExpiryDate());
        assertNotNull(createdRefreshToken.getToken());
    }


    /**
     * METHOD TESTED: createRefreshToken()
     * INPUT: invalid
     * EXPECTED RESULT: the refreshToken cannot be created because no user was found
     */
    @Test
    void integrationTest_createRefreshToken_error() {
        // PREPARATION
        //  assert repositories are empty
        assertTrue(refreshTokenRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        // ASSERTION
        assertThrows(ResponseStatusException.class, ()->{
            // EXECUTE METHOD
            refreshTokenService.createRefreshToken(42L);
        });
    }

    /**
     * METHOD TESTED: verifyExpiration()
     * INPUT: valid
     * EXPECTED RESULT: the refreshToken is not expired and returned again
     *
     */
    @Test
    void integrationTest_verifyExpiration_success() throws InterruptedException {
        // PREPARATION
        //  assert repositories are empty
        assertTrue(refreshTokenRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        User savedUser = userRepository.save(testUser);
        userRepository.flush();
        RefreshToken createdRefreshToken = refreshTokenService.createRefreshToken(savedUser.getId());
        Thread.sleep(2000);
        // ASSERTIONS
        assertDoesNotThrow(()->{
            // EXECUTE METHOD
            refreshTokenService.verifyExpiration(createdRefreshToken);
        });
    }

    /**
     * METHOD TESTED: verifyExpiration()
     * INPUT: invalid
     * EXPECTED RESULT: the refreshToken is expired and deleted
     *
     */
    @Test
    void integrationTest_verifyExpiration_fail() throws InterruptedException {
        // PREPARATION
        //  assert repositories are empty
        assertTrue(refreshTokenRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        User savedUser = userRepository.save(testUser);
        userRepository.flush();
        RefreshToken createdRefreshToken = refreshTokenService.createRefreshToken(savedUser.getId());
        createdRefreshToken.setExpiryDate(Instant.now().minusMillis(1000));
        // ASSERTIONS
        assertThrows(ResponseStatusException.class, ()->{
            // EXECUTE METHOD
            refreshTokenService.verifyExpiration(createdRefreshToken);
        });
    }

    @AfterEach
    public void cleanUp() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

}

















