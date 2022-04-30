package ch.uzh.ifi.group26.scrumblebee.repository;

import ch.uzh.ifi.group26.scrumblebee.entity.RefreshToken;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RefreshTokenRepositoryIntegrationTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    RefreshToken refreshToken = new RefreshToken();
    User user = new User();

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setup() {

        refreshToken.setExpiryDate(Instant.now());
        refreshToken.setToken("token");

    }

    /**
     * check if exception is thrown when required fields are missing
     */
    @Test
    public void throwException_missingFields() {

        refreshToken.setExpiryDate(null);

        assertThrows(Exception.class, ()->{
            entityManager.persist(refreshToken);
        });

        assertThrows(Exception.class, ()->{
            entityManager.flush();
        });
    }

    /**
     * check if the correct refreshToken is returned
     */
    @Test
    public void findByToken_success() {

        entityManager.persist(refreshToken);
        entityManager.flush();

        Optional<RefreshToken> found = refreshTokenRepository.findByToken(refreshToken.getToken());

        if (found.isPresent()) {
            assertNotNull(found.get().getId());
            assertEquals(refreshToken.getToken(), found.get().getToken());
            assertEquals(refreshToken.getExpiryDate(), found.get().getExpiryDate());
            //assertEquals(refreshToken.getUser(), found.get().getUser());
        }
        else fail();

    }

    /**
     * check if the correct refreshToken is returned
     */
    @Test
    public void findByToken_fail() {

        entityManager.persist(refreshToken);
        entityManager.flush();

        Optional<RefreshToken> found = refreshTokenRepository.findByToken("wrong");

        assertTrue(found.isEmpty());

    }

}
