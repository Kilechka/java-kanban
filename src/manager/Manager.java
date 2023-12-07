package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.ArrayList;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();


    Integer id = 1;

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
        return subtask;
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>(tasks.values());
        return allTasks;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>(subtasks.values());
        return allSubtasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasksByEpicId(Integer epicId) {
        for (Integer i : epics.get(epicId).getSubtasksInEpic()) {
            if (subtasks.containsKey(i)) {
                subtasks.remove(i);
            }
        }
        epics.get(epicId).getSubtasksInEpic().clear();
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

    public Task update(Integer id, Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
            return task;
        } else if (epics.containsKey(id)) {
            epics.put(id, (Epic) task);
            return task;
        } else {
            subtasks.put(id, (Subtask) task);
            return task;
        }
    }

    private Integer createId() {
        return id++;
    }

    public void removeByIdTask(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void removeByIdEpic(Integer id) {
        if (epics.containsKey(id)) {
            ArrayList<Subtask> allSubtasks = new ArrayList<>(subtasks.values());
            for (Subtask subtask : allSubtasks) {
                if (subtask.getEpicId() == id) {
                    subtasks.remove(subtask.getId());
                }
            }
            epics.remove(id);
        }
    }

    public void removeByIdSub(Integer id) {
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        }
    }

    public ArrayList<Subtask> getSubtasksById(Integer epicId) {
        ArrayList<Subtask> Subtask = new ArrayList<>();
        for (Integer i : epics.get(epicId).getSubtasksInEpic()) {
            Subtask.add(subtasks.get(i));
        }
        return Subtask;
    }

    public String getStatus(Integer id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id).getStatus();
        } else if (epics.containsKey(id)) {
            return epics.get(id).getStatus();
        } else {
            return subtasks.get(id).getStatus();
        }
    }

    public String changeStatus(Task task) {
        if (task.getStatus().equals("NEW")) {
            task.setStatus("IN_PROGRESS");
        } else if (task.getStatus().equals("IN_PROGRESS")) {
            task.setStatus("DONE");
        }
        return task.getStatus();
    }

    public String changeStatusSub(Epic epic) {
        epic.setStatus("IN_PROGRESS");
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
