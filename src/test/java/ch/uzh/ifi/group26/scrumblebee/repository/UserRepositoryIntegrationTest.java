package ch.uzh.ifi.group26.scrumblebee.repository;

import ch.uzh.ifi.group26.scrumblebee.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;


    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    User user = new User();

    @BeforeEach
    public void setup() throws ParseException {
        user.setUsername("test@user");
        user.setPassword("password");
        user.setEmailAddress("test@domain.com");
        user.setName("name@lastname");
        user.setBirthDate(dateFormat.parse("1998-11-18"));
        user.setCreationDate(dateFormat.parse("2020-12-18"));
        user.setLoggedIn(false);
        user.setScore(0);
    }

    /**
     * make sure that a user can only be saved when all mandatory fields are present
     */
    @Test
    public void throwException_missingFields(){

        user.setUsername(null);
        user.setPassword(null);

        assertThrows(Exception.class, ()->{
            entityManager.persist(user);
        });

        assertThrows(Exception.class, ()->{
            entityManager.flush();
        });

    }

    /**
     * checking that the correct user is returned
     * @throws ParseException
     */
    @Test
    public void findByName_success() {

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findByUsername(user.getUsername());

        if (found.isPresent()) {
            assertNotNull(found.get().getId());
            assertEquals(user.getUsername(), found.get().getUsername());
            assertEquals(user.getPassword(), found.get().getPassword());
            assertEquals(user.getEmailAddress(), found.get().getEmailAddress());
            assertEquals(user.getName(), found.get().getName());
            assertEquals(user.getBirthDate(), found.get().getBirthDate());
            assertEquals(user.getCreationDate(), found.get().getCreationDate());
            assertEquals(user.getLoggedIn(), found.get().getLoggedIn());
            assertEquals(user.getScore(), found.get().getScore());
        } else {
            fail();
        }

  }

    /**
     * check that a non-existing username is not found
     * @throws ParseException
     */
    @Test
    public void findByName_fail(){

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findByUsername("notHere");

        assertTrue(found.isEmpty());

    }

    /**
     * checking that the correct user is returned
     * @throws ParseException
     */
    @Test
    public void findById_success() {

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findById(user.getId());

        if (found.isPresent()) {
            assertNotNull(found.get().getId());
            assertEquals(user.getUsername(), found.get().getUsername());
            assertEquals(user.getPassword(), found.get().getPassword());
            assertEquals(user.getEmailAddress(), found.get().getEmailAddress());
            assertEquals(user.getName(), found.get().getName());
            assertEquals(user.getBirthDate(), found.get().getBirthDate());
            assertEquals(user.getCreationDate(), found.get().getCreationDate());
            assertEquals(user.getLoggedIn(), found.get().getLoggedIn());
            assertEquals(user.getScore(), found.get().getScore());
        } else {
            fail();
        }

    }

    /**
     * check that a non-existing username is not found
     * @throws ParseException
     */
    @Test
    public void findById_fail() {

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findById(9999L);

        assertTrue(found.isEmpty());

    }

    /**
     * checking that the correct user is returned
     * @throws ParseException
     */
    @Test
    public void findByEmailAddress_success() {

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findByEmailAddress(user.getEmailAddress());

        if (found.isPresent()) {
            assertNotNull(found.get().getId());
            assertEquals(user.getUsername(), found.get().getUsername());
            assertEquals(user.getPassword(), found.get().getPassword());
            assertEquals(user.getEmailAddress(), found.get().getEmailAddress());
            assertEquals(user.getName(), found.get().getName());
            assertEquals(user.getBirthDate(), found.get().getBirthDate());
            assertEquals(user.getCreationDate(), found.get().getCreationDate());
            assertEquals(user.getLoggedIn(), found.get().getLoggedIn());
            assertEquals(user.getScore(), found.get().getScore());
        } else {
            fail();
        }

    }

    /**
     * check that a non-existing username is not found
     * @throws ParseException
     */
    @Test
    public void findByEmailAddress_fail() {

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findByEmailAddress("wrong@something.com");

        assertTrue(found.isEmpty());

    }
}
