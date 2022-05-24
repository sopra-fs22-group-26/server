package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.constant.RoleType;
import ch.uzh.ifi.group26.scrumblebee.entity.Role;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.RoleRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    /**
     * Return a list of all users.
     * @return List<User>
     */
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }


    /**
     * Return a single user by ID if this user exists, null otherwise.
     * @param userID of user
     * @return found user or error, if now user is found with id
     */
    public User getUser(long userID) {
        String baseErrorMessage = "Error: No user found with userID %d!";
        return userRepository.findById(userID).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, userID)));
    }


    /**
     * Used by: POST /register
     * @param newUser to create
     * @return the created user
     */
    public User createUser(User newUser) {

        checkIfUserExists(newUser);

        newUser.setLoggedIn(false);

        java.util.Date creationDate = java.util.Calendar.getInstance().getTime();
        newUser.setCreationDate(creationDate);

        String plainPassword = newUser.getPassword();
        newUser.setPassword(encoder.encode(plainPassword));

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
     * Used by: PUT /users/{id}
     * @param userToUpdate and inputUser
     * @return void
     */
    public User updateUser(User userToUpdate, User inputUser, User userCredentials){
        // Check if user wants to change password => if yes, verify credentials
        if (inputUser.getPassword() != null && userCredentials.getPassword() != null ){
            if (encoder.matches(userCredentials.getPassword(),userToUpdate.getPassword())){
                userToUpdate.setPassword(encoder.encode(inputUser.getPassword()));
            }
            else { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password incorrect - try again"); }
        }

        userToUpdate.setBirthDate(inputUser.getBirthDate());

        // The following attributes must not be null => only override if values are provided
        if (inputUser.getName() != null) {
            userToUpdate.setName(inputUser.getName());
        }
        if (inputUser.getUsername() != null) {
            Optional<User> foundUsername = userRepository.findByUsername(inputUser.getUsername());
            if (foundUsername.isEmpty()) {
                userToUpdate.setUsername(inputUser.getUsername());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username does already exist");
            }
        }
        if (inputUser.getEmailAddress() != null){
            userToUpdate.setEmailAddress(inputUser.getEmailAddress());
        }
        if (inputUser.getScore() > 0) {
            userToUpdate.addScore(inputUser.getScore());
        }

        return updateRepository(userToUpdate);

    }


    /**
     * Used by: PUT /users/{id}
     * @param aUser to update
     * @return void
     */
    private User updateRepository(User aUser){
        User updated = userRepository.save(aUser);
        userRepository.flush();
        return updated;
    }

    /**
    * This is a helper method that will check the uniqueness criteria of the
    * username and the name
    * defined in the User entity. The method will do nothing if the input is unique
    * and throw an error otherwise.
    *
    * @param userToBeCreated must not exist
    * @throws org.springframework.web.server.ResponseStatusException if user already exists
    * @see User
    */
    private void checkIfUserExists(User userToBeCreated) {

        Optional<User> userByUsername = this.userRepository.findByUsername(userToBeCreated.getUsername());
        Optional<User> userByEmailAddress = this.userRepository.findByEmailAddress(userToBeCreated.getEmailAddress());

        String baseErrorMessage = "The %s provided %s already used!";

        if (userByUsername.isPresent() && userByEmailAddress.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            String.format(baseErrorMessage, "username and the email address", "are"));
        }
        else if (userByUsername.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
        }
        else if (userByEmailAddress.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "email address", "is"));
        }

    }

    /**
     * Return the id of the user with given username
     * @param username of user to find
     * @return id of user
     */
    public long getUserIdFromUsername (String username) {
        Optional<User> userByUsername = this.userRepository.findByUsername(username);
        if (userByUsername.isPresent()){
            return userByUsername.get().getId();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No user found with username %s!", username));
        }
    }

}
