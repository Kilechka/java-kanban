package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;
    private static CSVTaskFormatter formatter = new CSVTaskFormatter();

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        HistoryManager historyManager = Managers.getDefaultHistory();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            Boolean isHistory = false;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    isHistory = true;
                    continue;
                }
                if (!isHistory) {
                    Task task = formatter.fromString(line);
                    if (task instanceof Subtask) {
                        fileBackedTaskManager.createNewSubtask((Subtask) task);
                    } else if (task instanceof Epic) {
                        fileBackedTaskManager.createNewEpic((Epic) task);
                    } else {
                        fileBackedTaskManager.createNewTask(task);
                    }
                } else {
                    List<Integer> taskId = formatter.historyFromString(line);
                    for (Integer id : taskId) {
                        historyManager.add(fileBackedTaskManager.getById(id));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке данных из файла", e);
        }
        return fileBackedTaskManager;
    }

    public void testSave() {
        save();
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
            ArrayList<String> allTasks = formatter.makeList(getAllTasks(), getAllEpics(), getAllSubtasks());
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
            throw new RuntimeException(e);
        }
    }
}
