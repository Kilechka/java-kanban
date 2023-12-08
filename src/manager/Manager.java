package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.ArrayList;

public class Manager {
    final HashMap<Integer, Task> tasks = new HashMap<>();
    final HashMap<Integer, Epic> epics = new HashMap<>();
    final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private Integer id = 1;

    public Task createNewTask(Task task) {
        task.setId(createId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createNewEpic(Epic epic) {
        epic.setId(createId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createNewSubtask(Subtask subtask) { //Подзадача должна быть добавлена в список внутри эпика, при создании указывается айди эпика epicId, которому она принадлежит
        subtask.setId(createId());
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).setSubtasksInEpic(subtask.getId());
        changeStatusEpic(epics.get(subtask.getEpicId()));
        return subtask;
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        ArrayList<Epic> epic = getAllEpics();
        for (Epic epic1 : epic) {
            epic1.getSubtasksInEpic().clear();
            changeStatusEpic(epic1);
        }
        subtasks.clear();
    }

    public Task getById(Integer id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            return subtasks.get(id);
        }
    }

    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public Subtask updateSub(Subtask sub) {
        subtasks.put(sub.getId(), sub);
        System.out.println(sub.getStatus());
        changeStatusEpic(epics.get(sub.getEpicId()));
        return sub;
    }

    public Epic updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.setId(epic.getId());
        return epic;
    }

    private Integer createId() {
        return id++;
    }

    public void removeByIdTask(Integer id) {
        tasks.remove(id);
    }

    public void removeByIdEpic(Integer id) {
        for (Integer i : epics.get(id).getSubtasksInEpic()) {
            subtasks.remove(i);
        }
        epics.get(id).getSubtasksInEpic().clear();
        epics.remove(id);
    }

    public void removeByIdSub(Integer id) {
        epics.get(subtasks.get(id).getEpicId()).getSubtasksInEpic().remove(id);
        subtasks.remove(id);
    }

    public ArrayList<Subtask> getSubtasksById(Integer epicId) {
        ArrayList<Subtask> Subtask = new ArrayList<>();
        for (Integer i : epics.get(epicId).getSubtasksInEpic()) {
            Subtask.add(subtasks.get(i));
        }
        return Subtask;
    }

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
}
