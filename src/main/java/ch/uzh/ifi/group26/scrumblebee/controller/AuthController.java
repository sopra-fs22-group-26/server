package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.AuthGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import ch.uzh.ifi.group26.scrumblebee.security.utils.JwtUtils;
import ch.uzh.ifi.group26.scrumblebee.service.AuthService;
import ch.uzh.ifi.group26.scrumblebee.service.SecurityUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SecurityUserDetailsService securityUserDetailsService;

    AuthController(AuthService authService) { this.authService = authService; }

    /**
     * Handles the logout process. User is identified by their username.
     * Type: POST
     * URL: /auth/logout
     * Body: username
     * Returns: id, username
     */
    @PostMapping("/auth/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AuthGetDTO logOutUser(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User loggedInUser = authService.logoutUser(userInput);
        return DTOMapper.INSTANCE.convertEntityToAuthGetDTO(loggedInUser);
    }

    /**
     * Handles login process. User is identified by username and password.
     * Type: POST
     * URL: /auth/login
     * Body: username, password
     * Returns: id, username, token
     */
    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AuthGetDTO verifyCredentials(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User loggedInUser = authService.verifyUser(userInput);
        AuthGetDTO response = DTOMapper.INSTANCE.convertEntityToAuthGetDTO(loggedInUser);

        // Create a jwt token for the user
        UserDetails userDetails = securityUserDetailsService.loadUserByUsername(userInput.getUsername());
        String token = jwtUtils.generateJwtToken(userDetails);
        log.info(token);

        response.setToken(token);

        return response;
    }
}
