package ch.uzh.ifi.group26.scrumblebee.repository;

import ch.uzh.ifi.group26.scrumblebee.constant.RoleType;
import ch.uzh.ifi.group26.scrumblebee.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;


    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * make sure that a user can only be saved when all mandatory fields are present
     */
    @Test
    public void throwException_wrongFields() {

        Role role = new Role();

        assertThrows(Exception.class, ()->{
            entityManager.persist(role);
        });

        assertThrows(Exception.class, ()->{
            entityManager.flush();
        });

    }

    /**
     * checking that the correct role is returned
     * @throws ParseException
     */
    @Test
    public void findByRoleName_success() {

        Role role = new Role();
        role.setRoleName(RoleType.ROLE_USER);

        entityManager.persist(role);
        entityManager.flush();

        Optional<Role> found = roleRepository.findByRoleName(role.getRoleName());

        if (found.isPresent()) assertEquals(role.getRoleName(), found.get().getRoleName());
        else fail();

    }

    /**
     * checking that the correct role is returned
     * @throws ParseException
     */
    @Test
    public void findByRoleName_fail() {

        Role role = new Role();
        role.setRoleName(RoleType.ROLE_USER);

        entityManager.persist(role);
        entityManager.flush();

        Optional<Role> found = roleRepository.findByRoleName(RoleType.WRONG);

        assertTrue(found.isEmpty());

    }


}
