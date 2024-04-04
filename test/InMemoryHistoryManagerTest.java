import manager.*;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    private static HistoryManager manager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void BeforeEach() {
        manager = Managers.getDefaultHistory();
        task1 = new Task("Task 1", "Description 1", "1999-09-15T05:15:00", 30);
        task2 = new Task("Task 2", "Description 2", "1999-09-15T06:15:00", 30);
        task3 = new Task("Task 3", "Description 3", "1999-09-15T07:15:00", 30);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
    }

    @Test
    public void shouldSaveHistory() {
        assertNotEquals(0, manager.getHistory().size(), "История пустая");
        assertEquals(3, manager.getHistory().size());
        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(task2, manager.getHistory().get(1));
        assertEquals(task3, manager.getHistory().get(2));
    }

    @Test
    public void shouldRemove() {
        Task task1 = new Task("Task 1", "Description 1", "1999-09-15T08:15:00", 30);
        task1.setId(1);
        manager.add(task1);
        manager.remove(task1.getId());
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    public void shouldNotAddDubbling() {
        Task task1 = new Task("Task 1", "Description 1", "1999-09-15T09:15:00", 30);
        task1.setId(1);
        manager.add(task1);
        manager.add(task1);
        assertEquals(3, manager.getHistory().size());
    }

    @Test
    public void shouldRemoveFirst() {
        manager.remove(1);
        assertEquals(2, manager.getHistory().size());
        assertEquals(task2, manager.getHistory().get(0));
        assertEquals(task3, manager.getHistory().get(1));
    }

    @Test
    public void shouldRemoveLast() {
        manager.remove(3);
        assertEquals(2, manager.getHistory().size());
        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(task2, manager.getHistory().get(1));
    }
}