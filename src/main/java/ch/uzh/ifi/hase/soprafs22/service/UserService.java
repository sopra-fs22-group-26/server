package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.util.passwordHasher;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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

    private final passwordHasher passwordHasher = new passwordHasher();

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

        checkIfUserExists(newUser);

        String plainPassword = newUser.getPassword();
        String hashedPassword = passwordHasher.hashFunction(plainPassword);
        newUser.setPassword(hashedPassword);

        java.util.Date creationDate = java.util.Calendar.getInstance().getTime();
        newUser.setCreationDate(creationDate);

        newUser.setLoggedIn(true);

        newUser.setToken(UUID.randomUUID().toString());

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

        User userByUsername = this.userRepository.findByUsername(userToBeCreated.getUsername());
        User userByEmailAddress = this.userRepository.findByEmailAddress(userToBeCreated.getEmailAddress());

        String baseErrorMessage = "The %s provided %s already used!";

        if (userByUsername != null && userByEmailAddress != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            String.format(baseErrorMessage, "username and the email address", "are"));
        }
        else if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
        }
        else if (userByEmailAddress != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "email address", "is"));
        }

    }

}
