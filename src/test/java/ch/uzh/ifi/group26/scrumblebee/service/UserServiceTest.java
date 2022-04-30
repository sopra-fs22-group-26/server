package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.config.TestConfig;
import ch.uzh.ifi.group26.scrumblebee.constant.RoleType;
import ch.uzh.ifi.group26.scrumblebee.entity.Role;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.RoleRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder encoder;

    @InjectMocks
    UserService userService;

    User testUser = new User();

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setup() throws ParseException {

        MockitoAnnotations.openMocks(this);

        // given
        testUser.setId(1L);
        testUser.setEmailAddress("testEmail");
        testUser.setUsername("testUsername");
        testUser.setName("test");
        testUser.setPassword("password");
        testUser.setLoggedIn(false);
        testUser.setScore(0);

    }

/*
    @Test
    public void createUser_validInputs_success() {

        when(encoder.encode(anyString())).thenReturn("password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress(anyString())).thenReturn(Optional.empty());

        Role userRole = new Role();
        userRole.setRoleName(RoleType.ROLE_USER);
        when(roleRepository.findByRoleName(Mockito.any(RoleType.class))).thenReturn(Optional.of(userRole));

        when(userRepository.save(Mockito.any())).thenReturn(testUser);

        User createdUser = userService.createUser(testUser);

        Mockito.verify(encoder, Mockito.times(1)).encode(Mockito.any());
        Mockito.verify(roleRepository, Mockito.times(1)).findByRoleName(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getEmailAddress(), createdUser.getEmailAddress());
        assertEquals(testUser.getName(), createdUser.getName());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertNotNull(createdUser.getCreationDate());
        assertNull(createdUser.getBirthDate());
        assertEquals(0, createdUser.getScore());

  }

 */

/*
  @Test
  public void createUser_duplicateName_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  public void createUser_duplicateInputs_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

   */

}
