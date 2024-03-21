import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File file;

    @BeforeEach
    void BeforeEach() throws IOException {
        file = Files.createTempFile("test", ".csv").toFile();
        manager = new FileBackedTaskManager(file);
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "15.09.1999 00:00", 30));
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "15.09.1999 01:00", 30));
        manager.createNewEpic(new Epic("Test addNewTask", "Test addNewTask description"));
        manager.createNewSubtask(new Subtask("Test addNewTask", "Test addNewTask description", 3, "15.09.1999 02:00", 30));
    }

    @Test
    public void shouldLoadAndSaveEmptyFile() throws IOException {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(0, loadedTaskManager.getAllTasks().size());
    }

    @Test
    void shouldSaveAndLoadTasks() throws IOException {
        Task task1 = new Task("Task 1", "Description 1", "15.09.1999 05:00", 30);
        Task task2 = new Task("Task 2", "Description 2", "15.09.1999 06:00", 30);
        Epic epic1 = new Epic("Epic 1", "Description 3");

        manager.createNewTask(task1);
        manager.createNewTask(task2);
        manager.createNewEpic(epic1);

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(2, loadedTaskManager.getAllEpics().size());
        assertTrue(loadedTaskManager.getAllTasks().contains(task1));
        assertTrue(loadedTaskManager.getAllTasks().contains(task2));
        assertTrue(loadedTaskManager.getAllEpics().contains(epic1));
        assertEquals(manager.getAllTasks(), loadedTaskManager.getAllTasks());
        assertEquals(manager.getAllEpics(), loadedTaskManager.getAllEpics());
        assertEquals(manager.getAllSubtasks(), loadedTaskManager.getAllSubtasks());
    }

    @Test
    void shouldSaveHistory() {
        Task task1 = new Task("Task 1", "Description 1", "15.09.1999 03:00", 30);
        Epic epic1 = new Epic("Epic 1", "Description 3");

        manager.createNewTask(task1);
        manager.createNewEpic(epic1);
        manager.getById(1);
        manager.getById(2);
        manager.getById(1);

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(manager.getHistory(), loadedTaskManager.getHistory());
        assertEquals(loadedTaskManager.getHistory().get(1).getId(), 1);
    }

    @Test
    public void testException() {
        file.delete();
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(file);
        }, "Нельзя выгрузить удалённый файл");
    }

    @Test
    void shouldGiveSortedTasksAfterLoading() throws IOException {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);
        List<Task> sortedTasks = loadedTaskManager.getPrioritizedTasks();

        assertEquals(manager.getById(1), sortedTasks.get(0));
        assertEquals(manager.getById(2), sortedTasks.get(1));
        assertEquals(manager.getById(4), sortedTasks.get(2));
    }
}
