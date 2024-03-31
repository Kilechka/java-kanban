package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
    private Integer id = 1;

    @Override
    public Task createNewTask(Task task) {
        List<Task> intersect = getPrioritizedTasks().stream()
                .filter(task1 -> task1.doTheTasksIntersect(task))
                .collect(Collectors.toList());
        if (!intersect.isEmpty()) {
            throw new IllegalArgumentException("Задачи пересекаются по времени выполнения");
        }
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
        List<Task> intersect = getPrioritizedTasks().stream()
                .filter(subtask1 -> subtask1.doTheTasksIntersect(subtask))
                .collect(Collectors.toList());
        if (!intersect.isEmpty()) {
            throw new IllegalArgumentException("Задачи пересекаются по времени выполнения");
        }
        subtask.setId(createId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.setSubtasksInEpic(subtask.getId());
        setTimeAndDurationEpic(epic);
        changeStatusEpic(epic);
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
        tasks.keySet().stream().forEach(taskId -> historyManager.remove(taskId));
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().stream().forEach(taskId -> historyManager.remove(taskId));
        subtasks.keySet().stream().forEach(taskId -> historyManager.remove(taskId));
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.keySet().stream().forEach(taskId -> historyManager.remove(taskId));
        epics.values().stream()
                .forEach(epic -> {
                    epic.getSubtasksInEpic().clear();
                    setTimeAndDurationEpic(epic);
                    changeStatusEpic(epic);
                });
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
    public Task getByIdInside(Integer id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            return subtasks.get(id);
        }
    }

    @Override
    public Task updateTask(Task task) {
        List<Task> intersect = getPrioritizedTasks().stream()
                .filter(otherTask -> otherTask.doTheTasksIntersect(task))
                .filter(otherTask -> otherTask.getId() != task.getId())
                .collect(Collectors.toList());
        if (!intersect.isEmpty()) {
            throw new IllegalArgumentException("Задачи пересекаются по времени выполнения");
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Subtask updateSub(Subtask sub) {
        List<Task> intersect = getPrioritizedTasks().stream()
                .filter(otherSub -> otherSub.doTheTasksIntersect(sub))
                .filter(otherSub -> otherSub.getId() != sub.getId())
                .collect(Collectors.toList());
        if (!intersect.isEmpty()) {
            throw new IllegalArgumentException("Задачи пересекаются по времени выполнения");
        }
        Epic epic = epics.get(sub.getEpicId());
        subtasks.put(sub.getId(), sub);
        changeStatusEpic(epics.get(sub.getEpicId()));
        setTimeAndDurationEpic(epic);
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
        epics.get(id).getSubtasksInEpic().stream()
                .forEach(subtaskId -> {
                    subtasks.remove(subtaskId);
                    historyManager.remove(subtaskId);
                });

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
        setTimeAndDurationEpic(epic);
    }

    @Override
    public ArrayList<Subtask> getSubtasksById(Integer epicId) {
        ArrayList<Subtask> subtask = new ArrayList<>();
        epics.get(epicId).getSubtasksInEpic().stream()
                .forEach(id -> subtask.add(subtasks.get(id)));
        return subtask;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
        prioritizedTasks.addAll(tasks.values());
        prioritizedTasks.addAll(subtasks.values());
        return new ArrayList<>(prioritizedTasks);
    }

    protected void setTimeAndDurationEpic(Epic epic) {
        List<Integer> subs = epic.getSubtasksInEpic();
        if (subs.isEmpty()) {
            epic.setDuration(null);
            epic.setStartTime(null);
            epic.setEndTime(null);
            return;
        }
        LocalDateTime startTime = subtasks.get(subs.get(0)).getStartTime();
        LocalDateTime endTime = subtasks.get(subs.get(0)).getStartTime();
        Integer duration = 0;
        for (int id : subs) {
            Subtask subtask = subtasks.get(id);
            duration += subtask.getDuration();
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
            if (subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

    private String changeStatusEpic(Epic epic) {
        epic.setStatus("IN_PROGRESS");
        if (epic.getSubtasksInEpic().isEmpty()) {
            epic.setStatus("NEW");
        }
        int countNew = 0;
        int countDone = 0;
        for (Integer subtaskId : epic.getSubtasksInEpic()) {
            if (subtasks.get(subtaskId).getStatus().equals("NEW")) {
                countNew++;
            } else if (subtasks.get(subtaskId).getStatus().equals("DONE")) {
                countDone++;
            }
        }
        if (epic.getSubtasksInEpic().size() == countNew) {
            epic.setStatus("NEW");
        } else if (epic.getSubtasksInEpic().size() == countDone) {
            epic.setStatus("DONE");
        }
        return epic.getStatus();
    }

    private Integer createId() {
        return id++;
    }
}
