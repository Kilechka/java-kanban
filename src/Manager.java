import java.util.HashMap;
import java.util.ArrayList;

class Manager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    Task NullPointerException = new Task("Такс-такс", "Что-то сделано неправильно");


    Integer id = 1;
    public Task createNewTask(Task task) {
        task.status = "NEW";
        task.id = createId();
        tasks.put(task.id, task);
        return task;
    }
    public Epic createNewEpic(Epic epic) {
        epic.status = "NEW";
        epic.id = createId();
        epics.put(epic.id, epic);
        this.id++;
        return epic;
    }
    public Subtask createNewSubtask(Subtask subtask) {
        subtask.status = "NEW";
        subtask.id = createId();
        subtasks.put(subtask.id, subtask);
        this.id++;
        return subtask;
    }
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>(epics.values());
        return allEpics;
    }
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>(tasks.values());
        return allTasks;
    }
    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>(subtasks.values());
        return allSubtasks;
    }

    public void deletAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }
    public ArrayList<Task> getAllObjective() {
        ArrayList<Epic> Epics = new ArrayList<>(epics.values());
        ArrayList<Task> Tasks = new ArrayList<>(tasks.values());
        ArrayList<Subtask> Subtasks = new ArrayList<>(subtasks.values());
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Epic epic : Epics) {
            allTasks.add(epic);
        }
        for (Task task : Tasks) {
            allTasks.add(task);
        }
        for (Subtask subtask : Subtasks) {
            allTasks.add(subtask);
        }
        return allTasks;
    }
    public Task getById(Integer id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else {
            return NullPointerException;
        }
    }
    public Task update(Integer id, Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
            return task;
        } else if (epics.containsKey(id)) {
            epics.put(id, (Epic) task);
            return task;
        } else if (subtasks.containsKey(id)) {
            subtasks.put(id, (Subtask) task);
            return task;
        } else {
            return NullPointerException;
        }
    }
    public Integer createId() {
       return id++;
    }
    public void removeById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            ArrayList<Subtask> allSubtasks = new ArrayList<>(subtasks.values());
            for (Subtask subtask : allSubtasks) {
                if (subtask.epicId == id) {
                    subtasks.remove(subtask.id);
                }
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        }
    }
    public ArrayList<Subtask> getSubtasksById(Integer id) {
        ArrayList<Subtask> subtasksById = new ArrayList<>();
        ArrayList<Subtask> allSubtasks = new ArrayList<>(subtasks.values());

            for (Subtask subtask : allSubtasks) {
                if (subtask.epicId == id) {
                    subtasksById.add(subtask);
                }
            }
            return subtasksById;
    }
    public String getStatus(Integer id) {
        ArrayList<Epic> Epics = new ArrayList<>(epics.values());
        ArrayList<Task> Tasks = new ArrayList<>(tasks.values());
        ArrayList<Subtask> Subtasks = new ArrayList<>(subtasks.values());
        for (Epic epic : Epics) {
            if (epic.id == id) {
                return epic.status;
            }
        }
        for (Task task : Tasks) {
            if (task.id == id) {
                return task.status;
            }
        }
        for (Subtask subtask : Subtasks) {
            if (subtask.id == id) {
                return subtask.status;
            }
        }
        return NullPointerException.toString();
    }
    public String changeStatus(Task task) {
        if (task.status.equals("NEW")) {
            task.status = "IN_PROGRESS";
        } else if (task.status.equals("IN_PROGRESS")) {
            task.status = "DONE";
        }
        return task.status;
    }
    public String changeStatusSub(Subtask subtask) {
        ArrayList<Subtask> Subtasks = new ArrayList<>(subtasks.values());
        ArrayList<Subtask> SubtasksById = new ArrayList<>();

        if (subtask.status.equals("NEW")) {
            subtask.status = "IN_PROGRESS";
        } else if (subtask.status.equals("IN_PROGRESS")) {
            subtask.status = "DONE";
        }

        for (Subtask subtask1 : Subtasks) {
           if (subtask.epicId == subtask1.epicId) {
               SubtasksById.add(subtask1);
           }
        }
        int inProgress = 0;
        int done = 0;
        for (Subtask subtask1 : SubtasksById) {
           if  (subtask1.status.equals("IN_PROGRESS")) {
               inProgress++;
           }
        }
        for (Subtask subtask1 : SubtasksById) {
            if  (subtask1.status.equals("DONE")) {
                done++;
            }
        }
        if (inProgress == SubtasksById.size()) {
            epics.get(subtask.epicId).status = subtask.status;
        } else if (done == SubtasksById.size()) {
            epics.get(subtask.epicId).status = subtask.status;
        }
        return subtask.status;

    }

}
