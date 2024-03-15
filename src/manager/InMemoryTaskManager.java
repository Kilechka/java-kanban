package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private Integer id = 1;
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Task createNewTask(Task task) {
        task.setId(createId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createNewEpic(Epic epic) {
        epic.setId(createId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createNewSubtask(Subtask subtask) {
        subtask.setId(createId());
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).setSubtasksInEpic(subtask.getId());
        changeStatusEpic(epics.get(subtask.getEpicId()));
        return subtask;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer taskId : epics.keySet()) {
            historyManager.remove(taskId);
        }
        for (Integer taskId : subtasks.keySet()) {
            historyManager.remove(taskId);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        ArrayList<Epic> epics = getAllEpics();
        for (Epic epic : epics) {
            epic.getSubtasksInEpic().clear();
            changeStatusEpic(epic);
        }
        for (Integer taskId : subtasks.keySet()) {
            historyManager.remove(taskId);
        }
        subtasks.clear();
    }

    @Override
    public Task getById(Integer id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        }
    }

    @Override
    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Subtask updateSub(Subtask sub) {
        subtasks.put(sub.getId(), sub);
        changeStatusEpic(epics.get(sub.getEpicId()));
        return sub;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.setId(epic.getId());
        return epic;
    }

    @Override
    public void removeByIdTask(Integer id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeByIdEpic(Integer id) {
        for (Integer subtaskId : epics.get(id).getSubtasksInEpic()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        epics.get(id).getSubtasksInEpic().clear();
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeByIdSub(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubtasksInEpic().remove(id);
        subtasks.remove(id);
        historyManager.remove(id);
        changeStatusEpic(epic);
    }

    @Override
    public ArrayList<Subtask> getSubtasksById(Integer epicId) {
        ArrayList<Subtask> Subtask = new ArrayList<>();
        for (Integer i : epics.get(epicId).getSubtasksInEpic()) {
            Subtask.add(subtasks.get(i));
        }
        return Subtask;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private String changeStatusEpic(Epic epic) {
        epic.setStatus("IN_PROGRESS");
        if (epic.getSubtasksInEpic().isEmpty()) {
            epic.setStatus("NEW");
        }
        int NEW = 0;
        int DONE = 0;
        for (Integer subtaskId : epic.getSubtasksInEpic()) {
            if (subtasks.get(subtaskId).getStatus().equals("NEW")) {
                NEW++;
            } else if (subtasks.get(subtaskId).getStatus().equals("DONE")) {
                DONE++;
            }
        }
        if (epic.getSubtasksInEpic().size() == NEW) {
            epic.setStatus("NEW");
        } else if (epic.getSubtasksInEpic().size() == DONE) {
            epic.setStatus("DONE");
        }
        return epic.getStatus();
    }

    private Integer createId() {
        return id++;
    }
}
