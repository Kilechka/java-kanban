package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    Task createNewTask(Task task);

    Epic createNewEpic(Epic epic);

    Subtask createNewSubtask(Subtask subtask);

    ArrayList<Epic> getAllEpics();

    ArrayList<Task> getAllTasks();

    ArrayList<Subtask> getAllSubtasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getById(Integer id);

    Task updateTask(Task task);

    Subtask updateSub(Subtask sub);

    Epic updateEpic(Epic epic);

    void removeByIdTask(Integer id);

    void removeByIdEpic(Integer id);

    void removeByIdSub(Integer id);

    String changeStatusEpic(Epic epic);

    ArrayList<Task> getHistory();

    public ArrayList<Subtask> getSubtasksById(Integer epicId);
}


