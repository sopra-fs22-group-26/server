package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.constant.TaskPriority;
import ch.uzh.ifi.group26.scrumblebee.constant.TaskStatus;
import ch.uzh.ifi.group26.scrumblebee.entity.Task;
import ch.uzh.ifi.group26.scrumblebee.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    Task testTask = new Task();
    Task task1 = new Task();
    Task task2 = new Task();
    Task task3 = new Task();

    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setup() throws ParseException {

        MockitoAnnotations.openMocks(this);

        testTask.setTaskId(1L);
        testTask.setDueDate(dateFormat.parse("1992-11-19"));
        testTask.setTitle("Some@task");
        testTask.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam " +
                "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        testTask.setEstimate(4);
        testTask.setPriority(TaskPriority.HIGH);
        testTask.setScore(200);
        testTask.setAssignee(1L);
        testTask.setReporter(2L);

        when(taskRepository.save(any())).thenReturn(testTask);


        task1.setTaskId(1L);
        task1.setDueDate(dateFormat.parse("1992-11-19"));
        task1.setTitle("Some@task");
        task1.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam " +
                "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        task1.setEstimate(4);
        task1.setPriority(TaskPriority.HIGH);
        task1.setScore(200);
        task1.setAssignee(1L);
        task1.setReporter(3L);
        task1.setStatus(TaskStatus.ACTIVE);


        task2.setTaskId(2L);
        task2.setDueDate(dateFormat.parse("1992-11-19"));
        task2.setTitle("Some@task");
        task2.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam " +
                "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        task2.setEstimate(4);
        task2.setPriority(TaskPriority.HIGH);
        task2.setScore(200);
        task2.setAssignee(3L);
        task2.setReporter(2L);
        task2.setStatus(TaskStatus.COMPLETED);

        task3.setTaskId(3L);
        task3.setDueDate(dateFormat.parse("1992-11-19"));
        task3.setTitle("Some@task");
        task3.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam " +
                "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        task3.setEstimate(4);
        task3.setPriority(TaskPriority.HIGH);
        task3.setScore(200);
        task3.setAssignee(1L);
        task3.setReporter(2L);
        task3.setStatus(TaskStatus.COMPLETED);
    }

    /**
     * METHOD TESTED: getTasks()
     * INPUT: valid
     * EXPECTED RESULT: all tasks should be returned
     */
    @Test
    public void getTasks_multipleTasks_success() throws ParseException {

        // STUBBING
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.add(task2);

        given(taskRepository.findAll()).willReturn(allTasks);

        List<Task> foundTasks = taskService.getTasks();

        Mockito.verify(taskRepository, Mockito.times(1)).findAll();
        // verify task 1
        assertEquals(task1.getTaskId(), foundTasks.get(0).getTaskId());
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
        assertEquals(task2.getTaskId(), foundTasks.get(1).getTaskId());
        assertEquals(task2.getDueDate(), foundTasks.get(1).getDueDate());
        assertEquals(task2.getTitle(), foundTasks.get(1).getTitle());
        assertEquals(task2.getDescription(), foundTasks.get(1).getDescription());
        assertEquals(task2.getEstimate(), foundTasks.get(1).getEstimate());
        assertEquals(task2.getPriority(), foundTasks.get(1).getPriority());
        assertEquals(task2.getStatus(), foundTasks.get(1).getStatus());
        assertEquals(task2.getScore(), foundTasks.get(1).getScore());
        assertEquals(task2.getAssignee(), foundTasks.get(1).getAssignee());
        assertEquals(task2.getReporter(), foundTasks.get(1).getReporter());

    }

    /**
     * METHOD TESTED: getTasks(active)
     * INPUT: valid
     * EXPECTED RESULT: all tasks should be returned
     */
    @Test
    public void getTasksActive_multipleTasks_success() throws ParseException {

        // STUBBING
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.get(0).setStatus(TaskStatus.ACTIVE);
        allTasks.add(task2);

        given(taskRepository.findAll()).willReturn(allTasks);

        List<Task> foundTasks = taskService.getTasks("active");

        Mockito.verify(taskRepository, Mockito.times(1)).findAll();
        assertEquals(1, foundTasks.size());
        // verify task 1
        assertEquals(task1.getTaskId(), foundTasks.get(0).getTaskId());
        assertEquals(task1.getDueDate(), foundTasks.get(0).getDueDate());
        assertEquals(task1.getTitle(), foundTasks.get(0).getTitle());
        assertEquals(task1.getDescription(), foundTasks.get(0).getDescription());
        assertEquals(task1.getEstimate(), foundTasks.get(0).getEstimate());
        assertEquals(task1.getPriority(), foundTasks.get(0).getPriority());
        assertEquals(task1.getStatus(), foundTasks.get(0).getStatus());
        assertEquals(task1.getScore(), foundTasks.get(0).getScore());
        assertEquals(task1.getAssignee(), foundTasks.get(0).getAssignee());
        assertEquals(task1.getReporter(), foundTasks.get(0).getReporter());

    }

    /**
     * METHOD TESTED: getTasks(completed)
     * INPUT: valid
     * EXPECTED RESULT: all tasks should be returned
     */
    @Test
    public void getTasksCompleted_multipleTasks_success() throws ParseException {

        // STUBBING
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.get(0).setStatus(TaskStatus.COMPLETED);
        allTasks.add(task2);
        allTasks.get(1).setStatus(TaskStatus.ACTIVE);

        given(taskRepository.findAll()).willReturn(allTasks);

        List<Task> foundTasks = taskService.getTasks("completed");

        Mockito.verify(taskRepository, Mockito.times(1)).findAll();
        assertEquals(1, foundTasks.size());
        // verify task 1
        assertEquals(task1.getTaskId(), foundTasks.get(0).getTaskId());
        assertEquals(task1.getDueDate(), foundTasks.get(0).getDueDate());
        assertEquals(task1.getTitle(), foundTasks.get(0).getTitle());
        assertEquals(task1.getDescription(), foundTasks.get(0).getDescription());
        assertEquals(task1.getEstimate(), foundTasks.get(0).getEstimate());
        assertEquals(task1.getPriority(), foundTasks.get(0).getPriority());
        assertEquals(task1.getStatus(), foundTasks.get(0).getStatus());
        assertEquals(task1.getScore(), foundTasks.get(0).getScore());
        assertEquals(task1.getAssignee(), foundTasks.get(0).getAssignee());
        assertEquals(task1.getReporter(), foundTasks.get(0).getReporter());

    }



    /**
     * METHOD TESTED: getTasksForUser()
     * INPUT: valid
     * EXPECTED RESULT: all tasks for a user should be returned
     */
    @Test
    public void getTasksActive_forAssigneeWithId_success() throws ParseException {

        // STUBBING
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.get(0).setStatus(TaskStatus.ACTIVE);
        allTasks.add(task2);

        given(taskRepository.findAll()).willReturn(allTasks);

        List<Task> foundTasks = taskService.getTasksForUser(task1.getAssignee());

        Mockito.verify(taskRepository, Mockito.times(1)).findAll();
        assertEquals(1, foundTasks.size());
        // verify task 1
        assertEquals(task1.getTaskId(), foundTasks.get(0).getTaskId());
        assertEquals(task1.getDueDate(), foundTasks.get(0).getDueDate());
        assertEquals(task1.getTitle(), foundTasks.get(0).getTitle());
        assertEquals(task1.getDescription(), foundTasks.get(0).getDescription());
        assertEquals(task1.getEstimate(), foundTasks.get(0).getEstimate());
        assertEquals(task1.getPriority(), foundTasks.get(0).getPriority());
        assertEquals(task1.getStatus(), foundTasks.get(0).getStatus());
        assertEquals(task1.getScore(), foundTasks.get(0).getScore());
        assertEquals(task1.getAssignee(), foundTasks.get(0).getAssignee());
        assertEquals(task1.getReporter(), foundTasks.get(0).getReporter());

    }

    /**
     * METHOD TESTED: getTasksToReportForUser()
     * INPUT: valid
     * EXPECTED RESULT: all tasks to report for a user should be returned
     */
    @Test
    public void getTasksCompleted_forReporterWithId_success() throws ParseException {

        // STUBBING
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.get(0).setStatus(TaskStatus.COMPLETED);
        allTasks.add(task2);

        given(taskRepository.findAll()).willReturn(allTasks);

        List<Task> foundTasks = taskService.getTasksToReportForUser(task1.getReporter());

        Mockito.verify(taskRepository, Mockito.times(1)).findAll();
        assertEquals(1, foundTasks.size());
        // verify task 1
        assertEquals(task1.getTaskId(), foundTasks.get(0).getTaskId());
        assertEquals(task1.getDueDate(), foundTasks.get(0).getDueDate());
        assertEquals(task1.getTitle(), foundTasks.get(0).getTitle());
        assertEquals(task1.getDescription(), foundTasks.get(0).getDescription());
        assertEquals(task1.getEstimate(), foundTasks.get(0).getEstimate());
        assertEquals(task1.getPriority(), foundTasks.get(0).getPriority());
        assertEquals(task1.getStatus(), foundTasks.get(0).getStatus());
        assertEquals(task1.getScore(), foundTasks.get(0).getScore());
        assertEquals(task1.getAssignee(), foundTasks.get(0).getAssignee());
        assertEquals(task1.getReporter(), foundTasks.get(0).getReporter());

    }

    /**
     * METHOD TESTED: getTask()
     * INPUT: valid
     * EXPECTED RESULT: return task with id
     */
    @Test
    public void getTask_withId_success() throws ParseException {

        // STUBBING
        given(taskRepository.findByTaskId(anyLong())).willReturn(Optional.ofNullable(task1));

        Optional<Task> foundTask = taskService.getTask(task1.getTaskId());

        Mockito.verify(taskRepository, Mockito.times(1)).findByTaskId(anyLong());
        assertTrue(foundTask.isPresent());
        // verify task 1
        assertEquals(task1.getTaskId(), foundTask.get().getTaskId());
        assertEquals(task1.getDueDate(), foundTask.get().getDueDate());
        assertEquals(task1.getTitle(), foundTask.get().getTitle());
        assertEquals(task1.getDescription(), foundTask.get().getDescription());
        assertEquals(task1.getEstimate(), foundTask.get().getEstimate());
        assertEquals(task1.getPriority(), foundTask.get().getPriority());
        assertEquals(task1.getStatus(), foundTask.get().getStatus());
        assertEquals(task1.getScore(), foundTask.get().getScore());
        assertEquals(task1.getAssignee(), foundTask.get().getAssignee());
        assertEquals(task1.getReporter(), foundTask.get().getReporter());

    }


    /**
     * METHOD TESTED: createTask()
     * INPUT: valid
     * EXPECTED RESULT: task should be created
     */
    @Test
    public void createTask_validInput_success() {

        // STUBBING

        // EXECUTE METHOD
        Task createdTask = taskService.createTask(testTask);

        // VERIFY
        Mockito.verify(taskRepository, Mockito.times(1)).save(any());
        assertEquals(testTask.getTaskId(), createdTask.getTaskId());
        assertEquals(testTask.getDueDate(), createdTask.getDueDate());
        assertEquals(testTask.getTitle(), createdTask.getTitle());
        assertEquals(testTask.getDescription(), createdTask.getDescription());
        assertEquals(testTask.getEstimate(), createdTask.getEstimate());
        assertEquals(testTask.getPriority(), createdTask.getPriority());
        assertEquals(TaskStatus.ACTIVE, createdTask.getStatus());
        assertEquals(testTask.getScore(), createdTask.getScore());
        assertEquals(testTask.getAssignee(), createdTask.getAssignee());
        assertEquals(testTask.getReporter(), createdTask.getReporter());

    }

    /**
     * METHOD TESTED: updateTask()
     * INPUT: valid
     * EXPECTED RESULT: task should be updated
     */
    @Test
    public void updateTask_validInput_success() throws ParseException {

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

        when(taskRepository.findByTaskId(anyLong())).thenReturn(Optional.ofNullable(testTask));

        // EXECUTE METHOD
        Task updatedTask = taskService.updateTask(1L, inputTask);

        // VERIFY
        Mockito.verify(taskRepository, Mockito.times(1)).save(any());
        // should stay the same
        assertEquals(testTask.getTaskId(), updatedTask.getTaskId());
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
     */
    @Test
    public void deleteTask_validInput_success() throws ParseException {

        // STUBBING
        when(taskRepository.findByTaskId(anyLong())).thenReturn(Optional.ofNullable(testTask));

        // EXECUTE METHOD
        Task deletedTask = taskService.deleteTask(1L);

        // VERIFY
        Mockito.verify(taskRepository, Mockito.times(1)).delete(any());
        assertEquals(testTask.getTaskId(), deletedTask.getTaskId());
        assertEquals(testTask.getDueDate(), deletedTask.getDueDate());
        assertEquals(testTask.getTitle(), deletedTask.getTitle());
        assertEquals(testTask.getDescription(), deletedTask.getDescription());
        assertEquals(testTask.getEstimate(), deletedTask.getEstimate());
        assertEquals(testTask.getPriority(), deletedTask.getPriority());
        assertEquals(testTask.getStatus(), deletedTask.getStatus());
        assertEquals(testTask.getScore(), deletedTask.getScore());
        assertEquals(testTask.getAssignee(), deletedTask.getAssignee());
        assertEquals(testTask.getReporter(), deletedTask.getReporter());

    }

}
