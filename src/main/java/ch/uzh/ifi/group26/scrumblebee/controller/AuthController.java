package ch.uzh.ifi.group26.scrumblebee.controller;

import ch.uzh.ifi.group26.scrumblebee.entity.RefreshToken;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.*;
import ch.uzh.ifi.group26.scrumblebee.rest.mapper.DTOMapper;
import ch.uzh.ifi.group26.scrumblebee.security.utils.JwtUtils;
import ch.uzh.ifi.group26.scrumblebee.service.AuthService;
import ch.uzh.ifi.group26.scrumblebee.service.RefreshTokenService;
import ch.uzh.ifi.group26.scrumblebee.service.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SecurityUserDetailsService securityUserDetailsService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    UserRepository userRepository;

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
     * Returns: id, username, type, token, refreshToken
     */
    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AuthGetDTO verifyCredentials(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User loggedInUser = authService.verifyUser(userInput);
        AuthGetDTO response = DTOMapper.INSTANCE.convertEntityToAuthGetDTO(loggedInUser);

        // Create a jwt token for the user
        UserDetails userDetails = securityUserDetailsService.loadUserByUsername(loggedInUser.getUsername());
        String token = jwtUtils.generateJwtToken(userDetails);
        response.setToken(token);

        // Create a jwt refreshToken for the user
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loggedInUser.getId());
        response.setRefreshToken(refreshToken.getToken());

        return response;
    }

    /**
     * Handles refreshing the token with refreshToken as authentication
     * Type: POST
     * URL: /auth/refreshtoken
     * Body: refreshToken
     * Returns: id, username, type, token, refreshToken
     */
    @PostMapping("/auth/refreshtoken")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RefreshGetDTO refreshToken(@RequestBody RefreshPostDTO refreshPostDTO) {
        String refreshToken = refreshPostDTO.getRefreshToken();
        Optional<RefreshToken> foundToken = refreshTokenService.findByToken(refreshToken);
        if (foundToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is not valid!");
        } else {
            refreshTokenService.verifyExpiration(foundToken.get());
            UserDetails userDetails = securityUserDetailsService.loadUserByUsername(
                    foundToken.get().getUser().getUsername());
            RefreshGetDTO response = new RefreshGetDTO();
            response.setId(foundToken.get().getUser().getId());
            response.setRefreshToken(foundToken.get().getToken());
            response.setToken(jwtUtils.generateJwtToken(userDetails));
            return response;
        }
    }

}
