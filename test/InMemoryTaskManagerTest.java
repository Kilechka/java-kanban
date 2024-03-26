import manager.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "15.09.1999 00:00", 30));
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "15.09.1999 01:00", 30));
        manager.createNewEpic(new Epic("Test addNewTask", "Test addNewTask description"));
        manager.createNewSubtask(new Subtask("Test addNewTask", "Test addNewTask description", 3, "15.09.1999 02:00", 30));
    }

    @Test
    public void shouldSetStartTimeEpic() {
        assertEquals(LocalDateTime.of(1999, 9, 15, 2, 0), manager.getById(3).getStartTime());
    }

    @Test
    public void shouldSetDuration() {
        assertEquals(30, manager.getById(3).getDuration());
    }

    @Test
    public void shouldAddTimeAndDurationToEpic() {
        manager.createNewSubtask(new Subtask("Test addNewTask", "Test addNewTask description", 3, "16.09.1999 00:00", 30));
        assertEquals(LocalDateTime.of(1999, 9, 15, 2, 0), manager.getById(3).getStartTime());
        assertEquals(60, manager.getById(3).getDuration());
        assertEquals(LocalDateTime.of(1999, 9, 16, 00, 30), manager.getById(3).getEndTime());
    }

    @Test
    public void shouldNotIntersect() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "15.09.1999 02:00", 30));
        }, "Нельзя создать задачу, которая пересекается с другими задачами"); // таска 1 - 02:00, такса 2 - 02:00

        assertThrows(IllegalArgumentException.class, () -> {
            manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "15.09.1999 02:29", 30));
        }, "Нельзя создать задачу, которая пересекается с другими задачами"); // таска 1 - 02:00, такса 2 - 02:29

        assertThrows(IllegalArgumentException.class, () -> {
            manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "14.09.1999 23:30", 30));
        }, "Нельзя создать задачу, которая пересекается с другими задачами"); // таска 1 - 00:00, такса 2 - 23:30

        assertDoesNotThrow(() -> {
            manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "14.09.1999 23:29", 30));
        }, "Нельзя создать задачу, которая пересекается с другими задачами"); // таска 1 - 00:00, такса 2 - 23:29

        assertDoesNotThrow(() -> {
            manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "15.09.1999 02:31", 30));
        }, "Нельзя создать задачу, которая пересекается с другими задачами"); // таска 1 - 02:00, такса 2 - 02:31
    }

    @Test
    public void shouldSort() {
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description"));
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "15.09.1999 06:00", 30));
        List<Task> sortTasks = manager.getPrioritizedTasks();
        assertEquals(1, sortTasks.get(0).getId()); // 00:00
        assertEquals(2, sortTasks.get(1).getId()); // 01:00
        assertEquals(4, sortTasks.get(2).getId()); // 02:00
        assertEquals(6, sortTasks.get(3).getId()); // 06:00
        assertEquals(5, sortTasks.get(4).getId()); // null
    }
}