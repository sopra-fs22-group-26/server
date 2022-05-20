package ch.uzh.ifi.group26.scrumblebee.repository;

import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryIntegrationTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TaskRepository taskRepository;

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    Task task = new Task();

    @BeforeEach
    public void setup() throws ParseException {

        task.setDueDate(dateFormat.parse("2022-11-1"));
        task.setTitle("some@title");
        task.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        task.setEstimate(4);
        task.setPriority(TaskPriority.LOW);
        task.setLocation("somewhere");
        task.setStatus(TaskStatus.ACTIVE);
        task.setScore(111);
        task.setAssignee(1L);
        task.setReporter(2L);
        task.setCreatorId(1L);

    }

    /**
     * check if an exception is thrown, when required fields are missing
     *
     */
    @Test
    public void throwException_missingFields() throws ParseException {

        // check due date as required
        task.setDueDate(null);

        assertThrows(Exception.class, ()->{
            entityManager.persist(task);
        });
        assertThrows(Exception.class, ()->{
            entityManager.flush();
        });

    }

    /**
     * check if the correct task is returned
     */
    @Test
    public void findByTaskId_success() {

        entityManager.persist(task);
        entityManager.flush();

        Optional<Task> foundByTitle = taskRepository.findTaskByTitle(task.getTitle());
        Optional<Task> found = taskRepository.findByTaskId(foundByTitle.get().getTaskId());

        assertTrue(found.isPresent());

        assertNotNull(found.get().getTaskId());
        assertEquals(task.getDueDate(), found.get().getDueDate());
        assertEquals(task.getTitle(), found.get().getTitle());
        assertEquals(task.getDescription(), found.get().getDescription());
        assertEquals(task.getEstimate(), found.get().getEstimate());
        assertEquals(task.getPriority(), found.get().getPriority());
        assertEquals(task.getLocation(), found.get().getLocation());
        assertEquals(task.getStatus(), found.get().getStatus());
        assertEquals(task.getScore(), found.get().getScore());
        assertEquals(task.getAssignee(), found.get().getAssignee());
        assertEquals(task.getReporter(), found.get().getReporter());

    }

    @Test
    public void findByTaskId_fail() {

        entityManager.persist(task);
        entityManager.flush();

        Optional<Task> found = taskRepository.findByTaskId(444L);

        assertTrue(found.isEmpty());

    }

    @Test
    public void findTaskByTitle_success() {
        entityManager.persist(task);
        entityManager.flush();

        Optional<Task> found = taskRepository.findTaskByTitle(task.getTitle());

        assertTrue(found.isPresent());

        assertNotNull(found.get().getTaskId());
        assertEquals(task.getDueDate(), found.get().getDueDate());
        assertEquals(task.getTitle(), found.get().getTitle());
        assertEquals(task.getDescription(), found.get().getDescription());
        assertEquals(task.getEstimate(), found.get().getEstimate());
        assertEquals(task.getPriority(), found.get().getPriority());
        assertEquals(task.getLocation(), found.get().getLocation());
        assertEquals(task.getStatus(), found.get().getStatus());
        assertEquals(task.getScore(), found.get().getScore());
        assertEquals(task.getAssignee(), found.get().getAssignee());
        assertEquals(task.getReporter(), found.get().getReporter());
    }

    @Test
    public void findTaskByTitle_fail() {
        entityManager.persist(task);
        entityManager.flush();

        Optional<Task> found = taskRepository.findTaskByTitle("wrong");

        assertTrue(found.isEmpty());
    }
}
