package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class TaskServiceIntegrationTest {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    Task task1 = new Task();
    Task task2 = new Task();
    Task task3 = new Task();

    @BeforeEach
    public void setup() throws ParseException {

        taskRepository.deleteAll();

        // init task1
        task1.setDueDate(dateFormat.parse("1992-11-19"));
        task1.setTitle("Some@task");
        task1.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam " +
                "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        task1.setEstimate(4);
        task1.setStatus(TaskStatus.ACTIVE);
        task1.setPriority(TaskPriority.HIGH);
        task1.setScore(200);
        task1.setAssignee(3L);
        task1.setReporter(2L);
        task1.setCreatorId(1L);

        // init task2
        task2.setDueDate(dateFormat.parse("2011-6-19"));
        task2.setTitle("Some@task");
        task2.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dol");
        task2.setEstimate(4);
        task2.setStatus(TaskStatus.COMPLETED);
        task2.setPriority(TaskPriority.HIGH);
        task2.setScore(200);
        task2.setAssignee(1L);
        task2.setReporter(2L);
        task2.setCreatorId(1L);

        // init task3
        task3.setDueDate(dateFormat.parse("2011-2-19"));
        task3.setTitle("Some@task");
        task3.setDescription("Lorem ipsum dolor sit amet, consetetur sa");
        task3.setEstimate(4);
        task3.setStatus(TaskStatus.ACTIVE);
        task3.setPriority(TaskPriority.HIGH);
        task3.setScore(200);
        task3.setAssignee(2L);
        task3.setReporter(1L);
        task3.setCreatorId(1L);

    }

    /**
     * METHOD TESTED: getTasks()
     * INPUT: valid
     * EXPECTED RESULT: all tasks should be returned
     * INTEGRATION: this test aims for the interaction with the repository to get all tasks (findAll() call)
     */
    @Test
    public void getTasks_zeroTasks_success() {

        assertTrue(taskRepository.findAll().isEmpty());

        List<Task> foundTasks = taskService.getTasks();

        assertTrue(foundTasks.isEmpty());

    }

    /**
     * METHOD TESTED: getTasks()
     * INPUT: valid
     * EXPECTED RESULT: all tasks should be returned
     * INTEGRATION: this test aims for the interaction with the repository to get all tasks (findAll() call)
     */
    @Test
    public void getTasks_multipleTasks_success() {

        assertTrue(taskRepository.findAll().isEmpty());

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        List<Task> foundTasks = taskService.getTasks();

        // verify task 1
        assertNotNull(foundTasks.get(0).getTaskId());
        assertEquals(task1.getDueDate(), foundTasks.get(0).getDueDate());
        assertEquals(task1.getTitle(), foundTasks.get(0).getTitle());
        assertEquals(task1.getDescription(), foundTasks.get(0).getDescription());
        assertEquals(task1.getEstimate(), foundTasks.get(0).getEstimate());
        assertEquals(task1.getPriority(), foundTasks.get(0).getPriority());
        assertEquals(task1.getStatus(), foundTasks.get(0).getStatus());
        assertEquals(task1.getScore(), foundTasks.get(0).getScore());
        assertEquals(task1.getAssignee(), foundTasks.get(0).getAssignee());
        assertEquals(task1.getReporter(), foundTasks.get(0).getReporter());

        // verify task 2
        assertNotNull(foundTasks.get(1).getTaskId());
        assertEquals(task2.getDueDate(), foundTasks.get(1).getDueDate());
        assertEquals(task2.getTitle(), foundTasks.get(1).getTitle());
        assertEquals(task2.getDescription(), foundTasks.get(1).getDescription());
        assertEquals(task2.getEstimate(), foundTasks.get(1).getEstimate());
        assertEquals(task2.getPriority(), foundTasks.get(1).getPriority());
        assertEquals(task2.getStatus(), foundTasks.get(1).getStatus());
        assertEquals(task2.getScore(), foundTasks.get(1).getScore());
        assertEquals(task2.getAssignee(), foundTasks.get(1).getAssignee());
        assertEquals(task2.getReporter(), foundTasks.get(1).getReporter());

        // verify task 3
        assertNotNull(foundTasks.get(2).getTaskId());
        assertEquals(task3.getDueDate(), foundTasks.get(2).getDueDate());
        assertEquals(task3.getTitle(), foundTasks.get(2).getTitle());
        assertEquals(task3.getDescription(), foundTasks.get(2).getDescription());
        assertEquals(task3.getEstimate(), foundTasks.get(2).getEstimate());
        assertEquals(task3.getPriority(), foundTasks.get(2).getPriority());
        assertEquals(task3.getStatus(), foundTasks.get(2).getStatus());
        assertEquals(task3.getScore(), foundTasks.get(2).getScore());
        assertEquals(task3.getAssignee(), foundTasks.get(2).getAssignee());
        assertEquals(task3.getReporter(), foundTasks.get(2).getReporter());

    }

    /**
     * METHOD TESTED: getTask()
     * INPUT: valid
     * EXPECTED RESULT: return task with id
     * INTEGRATION: this test aims for the interaction with the repository to get a task by its id
     */
    @Test
    public void getTask_withId_success() {

        assertTrue(taskRepository.findAll().isEmpty());
        Task saved = taskRepository.save(task2);

        Optional<Task> foundTask = taskService.getTask(saved.getTaskId());

        assertTrue(foundTask.isPresent());
        // verify task 1
        assertEquals(task2.getTaskId(), foundTask.get().getTaskId());
        assertEquals(task2.getDueDate(), foundTask.get().getDueDate());
        assertEquals(task2.getTitle(), foundTask.get().getTitle());
        assertEquals(task2.getDescription(), foundTask.get().getDescription());
        assertEquals(task2.getEstimate(), foundTask.get().getEstimate());
        assertEquals(task2.getPriority(), foundTask.get().getPriority());
        assertEquals(task2.getStatus(), foundTask.get().getStatus());
        assertEquals(task2.getScore(), foundTask.get().getScore());
        assertEquals(task2.getAssignee(), foundTask.get().getAssignee());
        assertEquals(task2.getReporter(), foundTask.get().getReporter());

    }

    /**
     * METHOD TESTED: getTask()
     * INPUT: valid
     * EXPECTED RESULT: return task with id
     * INTEGRATION: this test aims for the interaction with the repository to get a task by its id
     */
    @Test
    public void getTask_withId_fail() {

        assertTrue(taskRepository.findAll().isEmpty());
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        assertThrows(ResponseStatusException.class, ()->{
            taskService.getTask(99999L);
        });

    }

    /**
     * METHOD TESTED: createTask()
     * INPUT: valid
     * EXPECTED RESULT: task should be created
     * INTEGRATION: this test aims for the interaction with the repository to get a task by its id
     */
    @Test
    public void createTask_validInput_success() {

        assertTrue(taskRepository.findAll().isEmpty());
        task1.setStatus(null);

        // EXECUTE METHOD
        Task createdTask = taskService.createTask(task1);

        // VERIFY
        assertEquals(task1.getTaskId(), createdTask.getTaskId());
        assertEquals(task1.getDueDate(), createdTask.getDueDate());
        assertEquals(task1.getTitle(), createdTask.getTitle());
        assertEquals(task1.getDescription(), createdTask.getDescription());
        assertEquals(task1.getEstimate(), createdTask.getEstimate());
        assertEquals(task1.getPriority(), createdTask.getPriority());
        assertEquals(TaskStatus.ACTIVE, createdTask.getStatus());
        assertEquals(task1.getScore(), createdTask.getScore());
        assertEquals(task1.getAssignee(), createdTask.getAssignee());
        assertEquals(task1.getReporter(), createdTask.getReporter());

    }

    /**
     * METHOD TESTED: updateTask()
     * INPUT: valid
     * EXPECTED RESULT: task should be updated
     * INTEGRATION: this test aims for the interaction with the repository to update an existing task
     */
    @Test
    public void updateTask_validInput_success() throws ParseException {

        assertTrue(taskRepository.findAll().isEmpty());
        Task saved = taskRepository.save(task1);

        // first check that the task saved is indeed task1
        Optional<Task> savedTask = taskRepository.findByTaskId(saved.getTaskId());
        assertTrue(savedTask.isPresent());
        assertEquals(task1.getTaskId(), savedTask.get().getTaskId());
        assertEquals(task1.getDueDate(), savedTask.get().getDueDate());
        assertEquals(task1.getTitle(), savedTask.get().getTitle());
        assertEquals(task1.getDescription(), savedTask.get().getDescription());
        assertEquals(task1.getEstimate(), savedTask.get().getEstimate());
        assertEquals(task1.getPriority(), savedTask.get().getPriority());
        assertEquals(TaskStatus.ACTIVE, savedTask.get().getStatus());
        assertEquals(task1.getScore(), savedTask.get().getScore());
        assertEquals(task1.getAssignee(), savedTask.get().getAssignee());
        assertEquals(task1.getReporter(), savedTask.get().getReporter());

        // STUBBING
        Task inputTask = new Task();
        inputTask.setTitle("newTitle");
        inputTask.setDescription("newDescription");
        inputTask.setDueDate(dateFormat.parse("1200-11-11"));
        inputTask.setPriority(TaskPriority.LOW);
        inputTask.setEstimate(12);
        inputTask.setLocation("differentLocation");
        inputTask.setStatus(TaskStatus.COMPLETED);
        inputTask.setScore(300);
        inputTask.setAssignee(12L);
        inputTask.setReporter(14L);

        // EXECUTE METHOD
        Task updatedTask = taskService.updateTask(saved.getTaskId(), inputTask);

        // VERIFY
        // should stay the same
        assertEquals(savedTask.get().getTaskId(), updatedTask.getTaskId());
        // should be new values
        assertEquals(inputTask.getDueDate(), updatedTask.getDueDate());
        assertEquals(inputTask.getTitle(), updatedTask.getTitle());
        assertEquals(inputTask.getDescription(), updatedTask.getDescription());
        assertEquals(inputTask.getEstimate(), updatedTask.getEstimate());
        assertEquals(inputTask.getPriority(), updatedTask.getPriority());
        assertEquals(inputTask.getStatus(), updatedTask.getStatus());
        assertEquals(inputTask.getScore(), updatedTask.getScore());
        assertEquals(inputTask.getAssignee(), updatedTask.getAssignee());
        assertEquals(inputTask.getReporter(), updatedTask.getReporter());

    }

    /**
     * METHOD TESTED: deleteTask()
     * INPUT: valid
     * EXPECTED RESULT: task should be deleted
     * INTEGRATION: this test aims for the interaction with the repository to delete an existing task
     */
    @Test
    public void deleteTask_validInput_success() {

        assertTrue(taskRepository.findAll().isEmpty());
        Task saved = taskRepository.save(task1);

        // EXECUTE METHOD
        Task deletedTask = taskService.deleteTask(saved.getTaskId());

        // VERIFY
        assertTrue(taskRepository.findByTaskId(deletedTask.getTaskId()).isEmpty());

    }

    @AfterEach
    public void cleanUp() {
        taskRepository.deleteAll();
    }

}
