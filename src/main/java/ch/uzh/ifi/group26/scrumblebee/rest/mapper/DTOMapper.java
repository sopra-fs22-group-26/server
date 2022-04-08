package ch.uzh.ifi.group26.scrumblebee.rest.mapper;

import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskPostDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.UserPostDTO;
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
  @Mapping(source = "token", target = "token")
  @Mapping(target = "score", ignore = true)
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "emailAddress", target = "emailAddress")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "birthDate", target = "birthDate")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "loggedIn", target = "loggedIn")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "score", target = "score")
  UserGetDTO convertEntityToUserGetDTO(User user);

}

