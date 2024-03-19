import manager.TaskManager;
import manager.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    @BeforeEach
    public void beforeEach() {
        manager = (T) Managers.getDefault();
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "15.09.1999 00:00", 30));
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description", "15.09.1999 01:00", 30));
        manager.createNewEpic(new Epic("Test addNewTask", "Test addNewTask description"));
        manager.createNewSubtask(new Subtask("Test addNewTask", "Test addNewTask description", 3, "15.09.1999 02:00", 30));
    }

    @Test
    public void shouldAddNewTask() {
        manager.deleteAllTasks();
        Task task = new Task("Test addNewTask", "Test addNewTask description", "15.09.1999 03:00", 30);
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
        assertEquals(manager.getById(1), (manager.getById(1)), "Экземпляры класса Task не равны друг другу");
        assertEquals(manager.getById(3), (manager.getById(3)), "Наследники класса Task не равны друг другу");
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
        boolean deleteSub = manager.getAllSubtasks().size() == 0;
        assertTrue(deleteSub, "Не получилось удалить все подзадачи");

        manager.deleteAllTasks();
        boolean deleteTask = manager.getAllTasks().size() == 0;
        assertTrue(deleteTask, "Не получилось удалить все подзадачи");

        manager.deleteAllEpics();
        boolean deleteEpic = manager.getAllEpics().size() == 0;
        assertTrue(deleteEpic, "Не получилось удалить все подзадачи");
    }

    @Test
    public void shouldNotCreateWithNonExistenseId() {
        manager.deleteAllSubtasks();
        Subtask sub = new Subtask("Sub", "Sub description", 3, "15.09.1999 03:00", 30);
        assertNull(sub.getId(), "Подзадача с несуществующим id эпика была создана");
        boolean getSub = manager.getAllSubtasks().size() == 0;
        assertTrue(getSub, "Подзадача с несуществующим id эпика была создана");
    }

    @Test
    public void shouldNotClashInsideManager() {
        Subtask sub = new Subtask("Sub", "Sub description", 3, "15.09.1999 03:00", 30);
        manager.createNewSubtask(sub);
        assertFalse(sub.getId() == 3, "Мы задали id 3 при создании подзадачи. Этот id был ей присвоен");
    }

    @Test
    public void shouldGetAllTask() {
        assertNotNull(manager.getAllEpics());
        assertNotNull(manager.getAllTasks());
        assertNotNull(manager.getAllSubtasks());
    }

    @Test
    public void shouldUpdateStatusEpic() {
        Epic epic = new Epic("Epic", "Epic description");
        manager.createNewEpic(epic);
        Subtask sub = new Subtask("Sub", "Sub description", epic.getId(), "15.09.1999 03:00", 30);
        manager.createNewSubtask(sub);
        Subtask sub1 = new Subtask("Sub", "Sub description", epic.getId(), "15.09.1999 04:00", 30);
        manager.createNewSubtask(sub1);

        assertEquals("NEW", epic.getStatus());

        sub.setStatus("IN_PROGRESS");
        manager.updateSub(sub);
        manager.updateEpic(epic);

        assertEquals("IN_PROGRESS", epic.getStatus(), "Статус эпика не поменялся");

        sub.setStatus("DONE");
        manager.updateSub(sub);
        manager.updateEpic(epic);

        assertEquals("IN_PROGRESS", epic.getStatus(), "Статус эпика не поменялся");

        sub1.setStatus("DONE");
        manager.updateSub(sub1);
        manager.updateEpic(epic);

        assertEquals("DONE", epic.getStatus(), "Статус эпика не поменялся");


    }

    @Test
    public void shouldUpdate() {
        assertEquals("NEW", manager.getById(1).getStatus());
        manager.getById(1).setStatus("DONE");
        manager.updateTask(manager.getById(1));
        assertEquals("DONE", manager.getById(1).getStatus(), "Статус задачи не обновился");
        assertEquals("NEW", manager.getById(4).getStatus());

        Epic epic = new Epic("Epic", "Epic description");
        manager.createNewEpic(epic);
        Subtask sub = new Subtask("Sub", "Sub description", epic.getId(), "01.09.1999 07:00", 30);
        manager.createNewSubtask(sub);
        sub.setStatus("DONE");
        manager.updateSub(sub);
        manager.updateEpic(epic);
        assertEquals("DONE", sub.getStatus(), "Статус подзадачи не обновился");
        assertEquals("DONE", epic.getStatus(), "Статус эпика не обновился");
    }

    @Test
    public void shouldGetHistory() {
        manager.getById(1);
        manager.getById(2);
        manager.getById(3);
        manager.getById(1);

        assertNotNull(manager.getHistory(), "История не отображается");
    }

    @Test
    public void shouldNotAddToHistoryTaskWithNullId() {
        manager.getById(10);
        assertEquals(0, manager.getHistory().size());
    }

    @Test
    public void shouldRemoveById() {
        manager.removeByIdTask(1);
        manager.removeByIdSub(4);
        manager.removeByIdEpic(3);

        assertNull(manager.getById(1));
        assertNull(manager.getById(4));
        assertNull(manager.getById(3));
    }

    @Test
    public void shouldNotHaveOldSub() {
        manager.removeByIdSub(manager.getById(4).getId());
        assertNull(manager.getById(4));
    }

    @Test
    public void shouldNotHaveOldSubInEpic() {
        manager.removeByIdSub(manager.getById(4).getId());
        assertEquals(0, manager.getSubtasksById(3).size());
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
    }
}

