package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.RefreshToken;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.RefreshTokenRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

public class RefreshTokenServiceTest {

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RefreshTokenService refreshTokenService;

    RefreshToken refreshToken = new RefreshToken();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        refreshToken.setToken("someToken");
        refreshToken.setExpiryDate(Instant.now().plusMillis(20000));

    }

    /**
     * METHOD TESTED: findByToken()
     * INPUT: valid
     * EXPECTED RESULT: the refreshToken is found and returned
     *
     */
    @Test
    public void findByToken_success() {

        // STUBBING
        given(refreshTokenRepository.findByToken(anyString())).willReturn(Optional.ofNullable(refreshToken));

        // EXECUTE METHOD
        Optional<RefreshToken> foundRefreshToken = refreshTokenService.findByToken(refreshToken.getToken());

        // ASSERTIONS
        Mockito.verify(refreshTokenRepository, Mockito.atLeastOnce()).findByToken(anyString());
        assertTrue(foundRefreshToken.isPresent());
        assertEquals(refreshToken.getToken(), foundRefreshToken.get().getToken());
        assertEquals(refreshToken.getExpiryDate(), foundRefreshToken.get().getExpiryDate());

    }

    /**
     * METHOD TESTED: createRefreshToken()
     * INPUT: valid
     * EXPECTED RESULT: the refreshToken is found and returned
     *
     */
    @Test
    public void createRefreshToken_success() {

        // STUBBING
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user");
        testUser.setEmailAddress("mail");
        testUser.setPassword("password");
        given(userRepository.findById(any())).willReturn(Optional.of(testUser));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(testUser);
        refreshToken.setExpiryDate(Instant.now().plusMillis(20000));
        refreshToken.setToken(UUID.randomUUID().toString());

        given(refreshTokenRepository.save(any())).willReturn(refreshToken);

        // EXECUTE METHOD
        RefreshToken createdRefreshToken = refreshTokenService.createRefreshToken(testUser.getId());

        // ASSERTIONS
        Mockito.verify(refreshTokenRepository, Mockito.atLeastOnce()).save(any());
        assertEquals(refreshToken.getUser(), createdRefreshToken.getUser());
        assertNotNull(createdRefreshToken.getExpiryDate());
        assertEquals(refreshToken.getToken(), createdRefreshToken.getToken());

    }

    /**
     * METHOD TESTED: verifyExpiratio()
     * INPUT: valid
     * EXPECTED RESULT: the refreshToken is not expired and returned again
     *
     */
    @Test
    public void verifyExpiration_success() {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(20000));
        refreshToken.setToken(UUID.randomUUID().toString());

        // EXECUTE METHOD
        RefreshToken nonExpiredRefreshToken = refreshTokenService.verifyExpiration(refreshToken);

        // ASSERTIONS
        Mockito.verify(refreshTokenRepository, Mockito.times(0)).delete(any());
        assertEquals(refreshToken.getExpiryDate(), nonExpiredRefreshToken.getExpiryDate());
        assertEquals(refreshToken.getToken(), nonExpiredRefreshToken.getToken());

    }

    /**
     * METHOD TESTED: verifyExpiratio()
     * INPUT: invalid
     * EXPECTED RESULT: the refreshToken is expired and deleted
     *
     */
    @Test
    public void verifyExpiration_fail() {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().minusMillis(20000));
        refreshToken.setToken(UUID.randomUUID().toString());

        // ASSERTIONS
        assertThrows(ResponseStatusException.class, ()->{
            // EXECUTE METHOD
            refreshTokenService.verifyExpiration(refreshToken);
        });
        Mockito.verify(refreshTokenRepository, Mockito.times(1)).delete(any());

    }

}
