package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.constant.RoleType;
import ch.uzh.ifi.group26.scrumblebee.entity.Role;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.RoleRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getUsers() {
    return this.userRepository.findAll();
  }

    /**
     * Used by: POST /users
     * @param newUser
     * @return the created user
     */
    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setLoggedIn(true);

        checkIfUserExists(newUser);

        java.util.Date creationDate = java.util.Calendar.getInstance().getTime();
        newUser.setCreationDate(creationDate);

        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByRoleName(RoleType.ROLE_USER);
        userRole.ifPresent(roles::add);

        newUser.setRoles(roles);

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
  }

    /**
    * This is a helper method that will check the uniqueness criteria of the
    * username and the name
    * defined in the User entity. The method will do nothing if the input is unique
    * and throw an error otherwise.
    *
    * @param userToBeCreated
    * @throws org.springframework.web.server.ResponseStatusException
    * @see User
    */
    private void checkIfUserExists(User userToBeCreated) {

        Optional<User> userByUsername = this.userRepository.findByUsername(userToBeCreated.getUsername());
        User userByEmailAddress = this.userRepository.findByEmailAddress(userToBeCreated.getEmailAddress());

        String baseErrorMessage = "The %s provided %s already used!";

        if (userByUsername.isPresent() && userByEmailAddress != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            String.format(baseErrorMessage, "username and the email address", "are"));
        }
        else if (userByUsername.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
        }
        else if (userByEmailAddress != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "email address", "is"));
        }

    }

}
