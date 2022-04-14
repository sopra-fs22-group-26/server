package ch.uzh.ifi.group26.scrumblebee.rest.mapper;

import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "emailAddress", target = "emailAddress")
  @Mapping(source = "name", target = "name")
  @Mapping(target = "birthDate", ignore = true)
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "loggedIn", ignore = true)
  @Mapping(target = "score", ignore = true)
  @Mapping(target = "roles", ignore = true)
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "emailAddress", target = "emailAddress")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "birthDate", target = "birthDate")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "loggedIn", target = "loggedIn")
  @Mapping(source = "score", target = "score")
  UserGetDTO convertEntityToUserGetDTO(User user);

  @Mapping(target = "taskId", ignore = true)
  @Mapping(source = "dueDate", target = "dueDate")
  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "estimate", target = "estimate")
  @Mapping(source = "priority", target = "priority")
  @Mapping(source = "location", target = "location")
  @Mapping(source = "status", target = "status")
  @Mapping(target = "score", ignore = true)                 // changed from " source target" to "target ignore=true"
  Task convertTaskPostDTOtoEntity(TaskPostDTO taskPostDTO);

  @Mapping(source = "taskId", target = "taskId")
  @Mapping(source = "dueDate", target = "dueDate")
  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "estimate", target = "estimate")
  @Mapping(source = "priority", target = "priority")
  @Mapping(source = "location", target = "location")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "score", target = "score")
  TaskGetDTO convertEntityToTaskGetDTO(Task task);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "id", target = "id")
  @Mapping(target = "token", ignore = true)
  AuthGetDTO convertEntityToAuthGetDTO(User user);

}

