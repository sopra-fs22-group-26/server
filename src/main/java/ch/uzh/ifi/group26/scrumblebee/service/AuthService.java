package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    /**
     * Handles login procedure
     * @param currentUser
     * @return User
     */
    public User verifyUser(User currentUser) {
        User veryfiedUser = checkCredentials(currentUser);
        // No exception => login was successful
        veryfiedUser.setLoggedIn(true);
        return veryfiedUser;
    }

    /**
     * Handles logout procedure. User is identified by token.
     * @param currentUser
     * @return
     */
    public User logoutUser(User currentUser) {
        Optional<User> repUser = userRepository.findByUsername(currentUser.getUsername());

        String errorMessage = "Error: Username does not match any user in the repository. Logout failed.";
        if (repUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }
        // No exception => logout was successful
        repUser.get().setLoggedIn(false);
        return repUser.get();
    }

    /**
     * This is a helper function that will check credentials of user who wants to log in
     * and throws an error if username or password does not match any user
     * @param userToCheck
     */
    private User checkCredentials(User userToCheck) {
        String providedUsername = userToCheck.getUsername();
        String providedPassword = userToCheck.getPassword();

        Optional<User> userByUsername = userRepository.findByUsername(providedUsername);
        String errorMessage = "Error: The username/password combination did not match any user. Login failed.";

        if (!userByUsername.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMessage);
        }
        else {
            // for password checks always use the password encoder method matches()
            if (!encoder.matches(providedPassword, userByUsername.get().getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMessage);
            }
        }
        return userByUsername.get();
    }


}
