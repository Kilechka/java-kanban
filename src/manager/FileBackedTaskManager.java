package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static model.TasksType.EPIC;
import static model.TasksType.TASK;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            Boolean isHistory = false;
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    isHistory = true;
                    continue;
                }
                if (!isHistory) {
                    Task task = CSVTaskFormatter.fromString(line);
                    if (task.getType() == TASK) {
                        fileBackedTaskManager.tasks.put(task.getId(), task);
                    } else if (task.getType() == EPIC) {
                        fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                    } else {
                        fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                        Epic epic = fileBackedTaskManager.epics.get(((Subtask) task).getEpicId());
                        epic.setSubtasksInEpic(task.getId());
                        fileBackedTaskManager.setStartTimeEpic(epic);
                        fileBackedTaskManager.setDuration(epic);
                    }
                } else {
                    if (line.isEmpty()) {
                        continue;
                    }
                    List<Integer> taskId = CSVTaskFormatter.historyFromString(line);
                    for (Integer id : taskId) {
                        if (fileBackedTaskManager.epics.get(id) != null) {
                            fileBackedTaskManager.historyManager.add(fileBackedTaskManager.epics.get(id));
                        } else if (fileBackedTaskManager.subtasks.get(id) != null) {
                            fileBackedTaskManager.historyManager.add(fileBackedTaskManager.subtasks.get(id));
                        } else {
                            fileBackedTaskManager.historyManager.add(fileBackedTaskManager.tasks.get(id));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных из файла");
        }
        return fileBackedTaskManager;
    }

    @Override
    public Task createNewTask(Task task) {
        super.createNewTask(task);
        save();
        return task;
    }

    @Override
    public Epic createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createNewSubtask(Subtask subtask) {
        super.createNewSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Subtask updateSub(Subtask sub) {
        super.updateSub(sub);
        save();
        return sub;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public void removeByIdTask(Integer id) {
        super.removeByIdTask(id);
        save();
    }

    @Override
    public void removeByIdEpic(Integer id) {
        super.removeByIdEpic(id);
        save();
    }

    @Override
    public void removeByIdSub(Integer id) {
        super.removeByIdSub(id);
        save();
    }

    @Override
    public Task getById(Integer id) {
        Task task = super.getById(id);
        save();
        return task;
    }

    private void save() {
        try (BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(file))) {
            ArrayList<String> allTasks = CSVTaskFormatter.makeList(getAllTasks(), getAllEpics(), getAllSubtasks());
            bufferWriter.write("id, type, name, status, description, epicId, startTime, duration");
            bufferWriter.newLine();
            for (String task : allTasks) {
                bufferWriter.write(task);
                bufferWriter.newLine();
            }
            bufferWriter.newLine();
            for (Task task : getHistory()) {
                bufferWriter.write(task.getId().toString());
                bufferWriter.write(", ");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных в файл");
        }
    }
}
