package ch.uzh.ifi.group26.scrumblebee.repository;

import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("taskRepository")
public interface TaskRepository extends JpaRepository <Task, Long> {
    Optional<Task> findByTaskId(long taskId);
}
