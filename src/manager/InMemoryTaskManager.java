package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
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
        setStartTimeEpic(epic);
        setDuration(epic);
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
        //   ArrayList<Epic> epics = getAllEpics();
        subtasks.keySet().stream().forEach(taskId -> historyManager.remove(taskId));
        epics.values().stream()
                .forEach(epic -> {
                    epic.getSubtasksInEpic().clear();
                    setStartTimeEpic(epic);
                    changeStatusEpic(epic);
                    setDuration(epic);
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
    public Task updateTask(Task task) {
        List<Task> intersect = getPrioritizedTasks().stream()
                .filter(task1 -> task1.doTheTasksIntersect(task))
                .filter(task1 -> task.getId() != task.getId())
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
                .filter(sub1 -> sub1.doTheTasksIntersect(sub))
                .filter(sub1 -> sub.getId() != sub.getId())
                .collect(Collectors.toList());
        if (!intersect.isEmpty()) {
            throw new IllegalArgumentException("Задачи пересекаются по времени выполнения");
        }
        Epic epic = epics.get(sub.getEpicId());
        subtasks.put(sub.getId(), sub);
        changeStatusEpic(epics.get(sub.getEpicId()));
        setStartTimeEpic(epic);
        setDuration(epic);
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
        setStartTimeEpic(epic);
        setDuration(epic);
    }

    @Override
    public ArrayList<Subtask> getSubtasksById(Integer epicId) {
        ArrayList<Subtask> Subtask = new ArrayList<>();
        epics.get(epicId).getSubtasksInEpic().stream()
                .forEach(id -> Subtask.add(subtasks.get(id)));
        return Subtask;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public TreeSet<Task> getPrioritizedTasks() {
        subtasks.values().stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .forEach(subtask -> prioritizedTasks.add(subtask));
        tasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .forEach(task -> prioritizedTasks.add(task));
        return prioritizedTasks;
    }

    protected void setStartTimeEpic(Epic epic) {
        epic.setStartTime(subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epic.getId())
                .min(Comparator.comparing(Task::getStartTime))
                .map(Task::getStartTime)
                .orElse(null));
    }

    protected void setDuration(Epic epic) {
        epic.setDuration(subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epic.getId())
                .map(Task::getDuration)
                .reduce(Duration.ZERO, Duration::plus)
        );
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
