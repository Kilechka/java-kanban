package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static HistoryManager manager;
    private static ArrayList<Task> history;

    @BeforeAll
    static void BeforeAll() {
        manager = new InMemoryHistoryManager();
        history = manager.getHistory();

        manager.add(new Task("Test addNewTask", "Test addNewTask description"));
        manager.add(new Task("Test addNewTask", "Test addNewTask description"));
        manager.add(new Task("Test addNewTask", "Test addNewTask description"));
        manager.add(new Task("Test addNewTask", "Test addNewTask description"));
        manager.add(new Epic("Test addNewTask", "Test addNewTask description"));
        manager.add(new Epic("Test addNewTask", "Test addNewTask description"));
        manager.add(new Epic("Test addNewTask", "Test addNewTask description"));
        manager.add(new Epic("Test addNewTask", "Test addNewTask description"));
        manager.add(new Subtask("Test addNewTask", "Test addNewTask description", 5));
        manager.add(new Subtask("Test addNewTask", "Test addNewTask description", 5));
    }

    @Test
    public void shouldSaveHistory() {
        assertNotNull(history, "История пустая");
        assertEquals(10, history.size(), "История пустая");
    }

    @Test
    public void shouldGetMaxTenValue() {
        manager.add(new Task("Test addNewTask", "Test addNewTask description"));
        manager.add(new Task("Test addNewTask", "Test addNewTask description"));
        assertEquals(10, history.size(), "Выдаёт 12 задач");
    }

    @Test
    public void shouldRemoveOldTasksIfMoreThenTen() {
        Task oldTask = history.get(0);
        manager.add(new Task("Test addNewTask", "Test addNewTask description"));
        assertTrue(oldTask != history.get(0), "Самая старая просмотренная задача была удалена");
    }


}