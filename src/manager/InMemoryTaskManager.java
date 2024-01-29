package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {

    final HashMap<Integer, Task> tasks = new HashMap<>();
    final HashMap<Integer, Epic> epics = new HashMap<>();
    final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private Integer id = 1;
    private HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }


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
        if (epics.isEmpty()) {
            return null;
        }
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        if (tasks.isEmpty()) {
            return null;
        }
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        if (subtasks.isEmpty()) {
            return null;
        }
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
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
        System.out.println(sub.getStatus());
        changeStatusEpic(epics.get(sub.getEpicId()));
        return sub;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.setId(epic.getId());
        return epic;
    }

    private Integer createId() {
        return id++;
    }

    @Override
    public void removeByIdTask(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void removeByIdEpic(Integer id) {
        for (Integer subtaskId : epics.get(id).getSubtasksInEpic()) {
            subtasks.remove(subtaskId);
        }
        epics.get(id).getSubtasksInEpic().clear();
        epics.remove(id);
    }

    @Override
    public void removeByIdSub(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubtasksInEpic().remove(id);
        subtasks.remove(id);
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
    public String changeStatusEpic(Epic epic) {
        epic.setStatus("IN_PROGRESS");
        if (epic.getSubtasksInEpic().isEmpty()) {
            epic.setStatus("NEW");
        }
        ArrayList<Subtask> Subtasks = new ArrayList<>(subtasks.values());
        int NEW = 0;
        int DONE = 0;
        for (Integer i : epic.getSubtasksInEpic()) {
            for (Subtask subtask1 : Subtasks) {
                if (subtask1.getId() == i && subtask1.getStatus().equals("NEW")) {
                    NEW++;
                } else if (subtask1.getId() == i && subtask1.getStatus().equals("DONE")) {
                    DONE++;
                }
            }
        }
        if (epic.getSubtasksInEpic().size() == NEW) {
            epic.setStatus("NEW");
        } else if (epic.getSubtasksInEpic().size() == DONE) {
            epic.setStatus("DONE");
        }
        return epic.getStatus();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

}
