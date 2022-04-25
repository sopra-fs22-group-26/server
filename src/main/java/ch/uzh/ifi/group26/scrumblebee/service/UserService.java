package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.constant.RoleType;
import ch.uzh.ifi.group26.scrumblebee.entity.Role;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.RoleRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserPostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Return a list of all users.
     * @return List<User>
     */
    public List<User> getUsers() {
    return this.userRepository.findAll();
  }


    /**
     * Return a single user by ID if this user exists, null otherwise.
     * @param userID
     * @return
     */
    public User getUser(long userID) {
        String baseErrorMessage = "Error: No user found with userID %d!";
        return userRepository.findById(userID).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, userID)));
    }


    /**
     * Used by: POST /register
     * @param newUser
     * @return the created user
     */
    public User createUser(User newUser) {

        checkIfUserExists(newUser);

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
    public void updateUser(User userToUpdate, UserPostDTO inputUser){

        if (inputUser.getUsername() != null) {
            userToUpdate.setUsername(inputUser.getUsername());
        }
        if (inputUser.getBirthDate() != null){
            userToUpdate.setBirthDate(inputUser.getBirthDate());
        }
        if (inputUser.getEmailAddress() != null){
            userToUpdate.setEmailAddress(inputUser.getEmailAddress());
        }
        if (inputUser.getPassword() != null && inputUser.getNewPassword() != null ){
            if (encoder.matches(inputUser.getPassword(),userToUpdate.getPassword())){
                userToUpdate.setPassword(encoder.encode(inputUser.getNewPassword()));
            }
            else { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format("Current password incorrect - try again")); }
        }

        updateRepository(userToUpdate);
    }

    /**
     * Used by: PUT /users/{id}
     * @param userId
     * @return User found by ID
     */
    public User getUserByIDNum(Long userId){
        Optional<User> userRepo = userRepository.findById(userId);
        User user;
        try{
            user = userRepo.orElse(null);
            if (user == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ID not found"));
            }
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("ID not found"));
        }
        return user;

    }

    /**
     * Used by: PUT /users/{id}
     * @param aUser
     * @return void
     */
    private void updateRepository(User aUser){
        userRepository.save(aUser);
        userRepository.flush();
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
