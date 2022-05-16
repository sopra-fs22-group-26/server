package ch.uzh.ifi.group26.scrumblebee.rest.mapper;

import ch.uzh.ifi.group26.scrumblebee.constant.RoleType;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.Comment;
import ch.uzh.ifi.group26.scrumblebee.entity.Role;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * UserPostDTO ==> User
     *
     * @throws ParseException
     */
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() throws ParseException {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("name");
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("password");
        userPostDTO.setNewPassword("newPassword");
        userPostDTO.setBirthDate("2020-11-18");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

       // check content
        assertEquals(userPostDTO.getName(), user.getName());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getBirthDate(), user.getBirthDate());

    }


    /**
     * UserPutDTO ==> User
     *
     */
    @Test
    public void testCreateUser_fromUserPutDTO_toUser_success() {

        // create UserPostDTO
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setName("name");
        userPutDTO.setUsername("username");
        userPutDTO.setPassword("password");
        userPutDTO.setNewPassword("newPassword");
        userPutDTO.setBirthDate("2020-11-18");
        userPutDTO.setEmailAddress("test@email.com");
        userPutDTO.setScore(4);

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        // check content
        assertEquals(userPutDTO.getName(), user.getName());
        assertEquals(userPutDTO.getUsername(), user.getUsername());
        assertEquals(userPutDTO.getNewPassword(), user.getPassword());
        assertEquals(userPutDTO.getBirthDate(), user.getBirthDate());
        assertEquals(userPutDTO.getEmailAddress(), user.getEmailAddress());
        assertEquals(userPutDTO.getScore(), user.getScore());

    }


    /**
     * UserPutDTO ==> User (only username and password)
     *
     */
    @Test
    public void testCreateUser_fromUserPutDTO_toUser_CredentialsOnly_success() {

        // create UserPostDTO
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setName("name");
        userPutDTO.setUsername("username");
        userPutDTO.setPassword("password");
        userPutDTO.setNewPassword("newPassword");
        userPutDTO.setBirthDate("2020-11-18");
        userPutDTO.setEmailAddress("test@email.com");
        userPutDTO.setScore(4);

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        // check content
        assertEquals(userPutDTO.getUsername(), user.getUsername());
        assertEquals(userPutDTO.getNewPassword(), user.getPassword());

    }

    /**
     * User ==> UserGetDTO
     *
     */
    @Test
    public void testCreateUserGetDTO_fromUser_success() throws ParseException {

        User user = new User();
        user.setName("name");
        user.setUsername("username");
        user.setPassword("password");
        user.setBirthDate(dateFormat.parse("2020-11-18"));
        user.setCreationDate(dateFormat.parse("2021-11-18"));
        user.setEmailAddress("test@email.com");
        user.setScore(4);
        user.setLoggedIn(false);

        Set<Role> roles = new HashSet<>();
        Role userRole = new Role();
        userRole.setRoleName(RoleType.ROLE_USER);
        roles.add(userRole);
        user.setRoles(roles);

        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        assertEquals(user.getName(), userGetDTO.getName());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(dateFormat.format(user.getBirthDate()), userGetDTO.getBirthDate());
        assertEquals(dateFormat.format(user.getCreationDate()), userGetDTO.getCreationDate());
        assertEquals(user.getEmailAddress(), userGetDTO.getEmailAddress());
        assertEquals(user.getScore(), userGetDTO.getScore());
        assertEquals(user.getLoggedIn(), userGetDTO.getLoggedIn());

    }

    /**
     * TaskPostDTO ==> Task
     *
     */
    @Test
    public void testCreateTask_fromTaskPostDTO_success() throws ParseException {

        TaskPostDTO taskPostDTO = new TaskPostDTO();
        taskPostDTO.setDueDate(dateFormat.parse("1999-11-11"));
        taskPostDTO.setTitle("title");
        taskPostDTO.setDescription("This is a test description.");
        taskPostDTO.setEstimate(4);
        taskPostDTO.setPriority("HIGH");
        taskPostDTO.setLocation("Location");
        taskPostDTO.setStatus("ACTIVE");
        taskPostDTO.setScore(12);
        taskPostDTO.setAssignee(2);
        taskPostDTO.setReporter(1);

        Task task = DTOMapper.INSTANCE.convertTaskPostDTOtoEntity(taskPostDTO);

        assertEquals(taskPostDTO.getDueDate(), task.getDueDate());
        assertEquals(taskPostDTO.getTitle(), task.getTitle());
        assertEquals(taskPostDTO.getDescription(), task.getDescription());
        assertEquals(taskPostDTO.getEstimate(), task.getEstimate());
        assertEquals(taskPostDTO.getPriority(), task.getPriority());
        assertEquals(taskPostDTO.getLocation(), task.getLocation());
        assertEquals(taskPostDTO.getStatus(), task.getStatus());
        assertEquals(taskPostDTO.getAssignee(), task.getAssignee());
        assertEquals(taskPostDTO.getReporter(), task.getReporter());

    }

    /**
     *  Task ==> TaskGetDTO
     *
     */
    @Test
    public void testCreateTaskGetDTO_fromTask_success() throws ParseException {

        Task task = new Task();
        task.setTaskId(1L);
        task.setDueDate(dateFormat.parse("1999-11-11"));
        task.setTitle("title");
        task.setDescription("This is a test description.");
        task.setEstimate(4);
        task.setPriority(TaskPriority.HIGH);
        task.setLocation("Location");
        task.setStatus(TaskStatus.ACTIVE);
        task.setScore(12);
        task.setAssignee(2);
        task.setReporter(1);

        TaskGetDTO taskGetDTO = DTOMapper.INSTANCE.convertEntityToTaskGetDTO(task);

        assertEquals(task.getTaskId(), taskGetDTO.getTaskId());
        assertEquals(dateFormat.format(task.getDueDate()), taskGetDTO.getDueDate());
        assertEquals(task.getTitle(), taskGetDTO.getTitle());
        assertEquals(task.getDescription(), taskGetDTO.getDescription());
        assertEquals(task.getEstimate(), taskGetDTO.getEstimate());
        assertEquals(task.getPriority().toString(), taskGetDTO.getPriority());
        assertEquals(task.getLocation(), taskGetDTO.getLocation());
        assertEquals(task.getStatus().toString(), taskGetDTO.getStatus());
        assertEquals(task.getScore(), taskGetDTO.getScore());
        assertEquals(task.getAssignee(), taskGetDTO.getAssignee());
        assertEquals(task.getReporter(), taskGetDTO.getReporter());

    }

    /**
     * User ==> AuthGetDTO
     *
     */
    @Test
    public void createAuthGetDTO_fromUser_success() throws ParseException {

        User user = new User();
        user.setName("name");
        user.setUsername("username");
        user.setPassword("password");
        user.setBirthDate(dateFormat.parse("2020-11-18"));
        user.setCreationDate(dateFormat.parse("2021-11-18"));
        user.setEmailAddress("test@email.com");
        user.setScore(4);
        user.setLoggedIn(false);

        Set<Role> roles = new HashSet<>();
        Role userRole = new Role();
        userRole.setRoleName(RoleType.ROLE_USER);
        roles.add(userRole);
        user.setRoles(roles);

        AuthGetDTO authGetDTO = DTOMapper.INSTANCE.convertEntityToAuthGetDTO(user);

        assertEquals(user.getUsername(), authGetDTO.getUsername());
        assertEquals(user.getName(), authGetDTO.getName());
        assertEquals(user.getId(), authGetDTO.getId());

    }

    /**
     *  User ==> RefreshGetDTO
     *
     */
    @Test
    public void createRefreshGetDTO_fromUser_success() throws ParseException {

        User user = new User();
        user.setName("name");
        user.setUsername("username");
        user.setPassword("password");
        user.setBirthDate(dateFormat.parse("2020-11-18"));
        user.setCreationDate(dateFormat.parse("2021-11-18"));
        user.setEmailAddress("test@email.com");
        user.setScore(4);
        user.setLoggedIn(false);

        Set<Role> roles = new HashSet<>();
        Role userRole = new Role();
        userRole.setRoleName(RoleType.ROLE_USER);
        roles.add(userRole);
        user.setRoles(roles);

        RefreshGetDTO refreshGetDTO = DTOMapper.INSTANCE.convertEntityToRefreshGetDTO(user);

        assertEquals(user.getId(), refreshGetDTO.getId());

    }

    /**
     * CommentPostDTO ==> Comment
     */
    @Test
    public void createComment_fromCommentPostDTO_success() {
        // PREPARE
        CommentPostDTO commentPostDTO = new CommentPostDTO();
        commentPostDTO.setBelongingTask(1L);
        commentPostDTO.setAuthorId(2L);
        commentPostDTO.setContent("This is a test");
        // EXERCISE
        Comment convertedComment = DTOMapper.INSTANCE.convertCommentPostDTOtoEntity(commentPostDTO);
        // ASSERTIONS
        assertEquals(commentPostDTO.getBelongingTask(), convertedComment.getBelongingTask());
        assertEquals(commentPostDTO.getAuthorId(), convertedComment.getAuthorId());
        assertEquals(commentPostDTO.getContent(), convertedComment.getContent());
    }

    /**
     * Comment ==> CommentGetDTO
     */
    @Test
    public void createCommentGetDTO_fromComment_success() {
        // PREPARE
        Comment comment = new Comment();
        comment.setBelongingTask(1L);
        comment.setAuthorId(2L);
        comment.setContent("This is a test");
        comment.setAuthorName("Charles");
        // EXERCISE
        CommentGetDTO commentGetDTO = DTOMapper.INSTANCE.convertEntityToCommentGetDTO(comment);
        // ASSERTIONS
        assertEquals(comment.getBelongingTask(), commentGetDTO.getBelongingTask());
        assertEquals(comment.getAuthorId(), commentGetDTO.getAuthorId());
        assertEquals(comment.getContent(), commentGetDTO.getContent());
        assertEquals(comment.getAuthorName(), commentGetDTO.getAuthorName());
    }

}


