import manager.*;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

class InMemoryHistoryManagerTest {

    private static HistoryManager manager;

    @BeforeEach
    void BeforeEach() {
        manager = Managers.getDefaultHistory();
    }

    @Test
    public void shouldSaveHistory() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        Task task3 = new Task("Task 3", "Description 3");
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        List<Task> history = manager.getHistory();
        assertNotEquals(0, manager.getHistory().size(), "История пустая");
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
    }

    @Test
    public void shouldRemove() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        manager.add(task1);
        manager.remove(task1.getId());
        assertEquals(0, manager.getHistory().size());
    }

    @Test
    public void shouldNotAddDubbling() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        manager.add(task1);
        manager.add(task1);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void shouldRemoveFirst() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        Task task3 = new Task("Task 3", "Description 3");
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(1);
        assertEquals(2, manager.getHistory().size());
        assertEquals(task2, manager.getHistory().get(0));
        assertEquals(task3, manager.getHistory().get(1));
    }

    @Test
    public void shouldRemoveLast() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        Task task3 = new Task("Task 3", "Description 3");
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(3);
        assertEquals(2, manager.getHistory().size());
        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(task2, manager.getHistory().get(1));
    }
}