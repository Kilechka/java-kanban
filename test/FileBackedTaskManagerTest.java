import manager.FileBackedTaskManager;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {
    File file;
    FileBackedTaskManager taskManager;

    @BeforeEach
    void BeforeEach() throws IOException {
        file = Files.createTempFile("test", ".csv").toFile();
        taskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void SaveAndLoadEmptyFile() throws IOException {
        taskManager.testSave();

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(0, loadedTaskManager.getAllTasks().size());
    }

    @Test
    void shouldSaveAndLoadTasks() throws IOException {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        Epic epic1 = new Epic("Epic 1", "Description 3");

        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        taskManager.createNewEpic(epic1);

        taskManager.testSave();

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, loadedTaskManager.getAllEpics().size());
        assertTrue(loadedTaskManager.getAllTasks().contains(task1));
        assertTrue(loadedTaskManager.getAllTasks().contains(task2));
        assertTrue(loadedTaskManager.getAllEpics().contains(epic1));
    }
}
