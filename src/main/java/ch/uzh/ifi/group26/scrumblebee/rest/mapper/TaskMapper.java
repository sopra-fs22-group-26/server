package ch.uzh.ifi.group26.scrumblebee.rest.mapper;

import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskGetDTO;
import ch.uzh.ifi.group26.scrumblebee.rest.dto.TaskPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "task_id", ignore = true)
    @Mapping(source = "dueDate", target = "dueDate")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "estimate", target = "estimate")
    @Mapping(source = "priority", target = "priority")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "score", ignore = true)                 // changed from " source target" to "target ignore=true"
    Task convertTaskPostDTOtoEntity(TaskPostDTO taskPostDTO);

    @Mapping(source = "task_id", target = "task_id")
    @Mapping(source = "dueDate", target = "dueDate")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "estimate", target = "estimate")
    @Mapping(source = "priority", target = "priority")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "score", target = "score")
    TaskGetDTO convertEntityToTaskGetDTO(Task task);
}
