package ch.uzh.ifi.group26.scrumblebee.utils;


import ch.uzh.ifi.group26.scrumblebee.constant.RoleType;
import ch.uzh.ifi.group26.scrumblebee.entity.Role;
import ch.uzh.ifi.group26.scrumblebee.entity.SecurityUserDetails;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.security.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    JwtUtils jwt = new JwtUtils();

    User testUser = new User();

    @BeforeEach
    public void setup() {
        jwt.setRefreshTokenDuration(30000L);

        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("pass");
        testUser.setEmailAddress("email@address.com");
        Set<Role> roles = new HashSet<>();
        Role userRole = new Role();
        userRole.setRoleName(RoleType.ROLE_USER);
        roles.add(userRole);
        testUser.setRoles(roles);
    }

    @Test
    public void generateJwtToken_success() {
        // BUILD USERDETAIL
        UserDetails testUserDetails = SecurityUserDetails.build(testUser);
        // GENERATE KEY
        String generatedToken = jwt.generateJwtToken(testUserDetails);
        // ASSERTIONS
        assertFalse(generatedToken.isBlank());
    }

    @Test
    public void extractUsername_success() {
        // BUILD USERDETAIL AND KEY
        UserDetails testUserDetails = SecurityUserDetails.build(testUser);
        String generatedToken = jwt.generateJwtToken(testUserDetails);
        // EXTRACT USERNAME
        String username = jwt.extractUsername(generatedToken);
        // ASSERTIONS
        assertEquals(testUser.getUsername(), username);
    }

    @Test
    public void isTokenExpired_tokenValid_success() {
        // BUILD USERDETAIL AND KEY
        UserDetails testUserDetails = SecurityUserDetails.build(testUser);
        String generatedToken = jwt.generateJwtToken(testUserDetails);
        // ASSERTIONS
        assertTrue(jwt.validateToken(generatedToken, testUserDetails));
    }

    @Test
    public void isTokenExpired_tokenExpired_success() {
        // BUILD USERDETAIL AND KEY
        jwt.setRefreshTokenDuration(1L);
        UserDetails testUserDetails = SecurityUserDetails.build(testUser);
        String generatedToken = jwt.generateJwtToken(testUserDetails);
        // ASSERTIONS
        assertThrows(Exception.class, ()->{jwt.validateToken(generatedToken, testUserDetails);});
    }
}
