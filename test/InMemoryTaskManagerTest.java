import manager.Managers;
import manager.TaskManager;
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
    public void beforeEach() {
        manager = Managers.getDefault();
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description"));
        manager.createNewTask(new Task("Test addNewTask", "Test addNewTask description"));
        manager.createNewEpic(new Epic("Test addNewTask", "Test addNewTask description"));
        manager.createNewSubtask(new Subtask("Test addNewTask", "Test addNewTask description", 3));
    }

    @Test
    public void shouldAddNewTask() {
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
        Subtask sub = new Subtask("Sub", "Sub description", 10);
        assertNull(sub.getId(), "Подзадача с несуществующим id эпика была создана");
        boolean getSub = manager.getAllSubtasks().size() == 0;
        assertTrue(getSub, "Подзадача с несуществующим id эпика была создана");
    }

    @Test
    public void shouldNotClashInsideManager() {
        Subtask sub = new Subtask("Sub", "Sub description", 3);
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
        Subtask sub = new Subtask("Sub", "Sub description", epic.getId());
        manager.createNewSubtask(sub);

        assertEquals("NEW", epic.getStatus());

        sub.setStatus("IN_PROGRESS");
        manager.updateSub(sub);
        manager.updateEpic(epic);

        assertEquals("IN_PROGRESS", epic.getStatus(), "Статус эпика не поменялся");

        sub.setStatus("DONE");
        manager.updateSub(sub);
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
        Subtask sub = new Subtask("Sub", "Sub description", epic.getId());
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
        assertEquals(4, manager.getHistory().size(), "Колличество просмотров неверное");
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
}