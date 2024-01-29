package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager manager = Managers.getDefault();

    @BeforeEach
    public void BeforeEach() {
        manager = Managers.getDefault();
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description"));
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description"));
        manager.createNewEpic(new Epic("Test addNewTask", "Test addNewTask description"));
        manager.createNewSubtask(new Subtask("Test addNewTask", "Test addNewTask description", 3));
    }

    @Test
    public void addNewTask() {
        manager.deleteAllTasks();
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        manager.createNewTask(task);
        final Task savedTask = manager.getById(task.getId());
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void taskIsEqualToTaskIfEqualIfTheirIdIsEqual() {
        assertTrue(manager.getById(1).equals(manager.getById(1)), "Экземпляры класса Task не равны друг другу");
        assertTrue(manager.getById(3).equals(manager.getById(3)), "Наследники класса Task не равны друг другу");
    }

    @Test
    public void shouldFoundTasksById() {
        assertNotNull(manager.getById(1), "Нельзя найти задачу по ID");
        assertNotNull(manager.getById(3), "Нельзя найти задачу по ID");
        assertNotNull(manager.getById(4), "Нельзя найти задачу по ID");
    }

    @Test
    public void shouldRemoveAllTasks() {
        manager.deleteAllSubtasks();
        assertNull(manager.getAllSubtasks(), "Не получилось удалить все подчадачи");
        manager.deleteAllTasks();
        assertNull(manager.getAllTasks(), "Не получилось удалить все задачи");
        manager.deleteAllEpics();
        assertNull(manager.getAllEpics(), "Не получилось удалить все эпики");
    }
}