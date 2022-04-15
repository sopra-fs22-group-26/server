package ch.uzh.ifi.group26.scrumblebee.repository;

import ch.uzh.ifi.group26.scrumblebee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    User findByEmailAddress(String emailAddress);
}
