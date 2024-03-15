package manager;

import model.Epic;
import model.Task;
import model.Subtask;
import model.TasksType;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormatter {

    static Task fromString(String value) {
        String[] values = value.split(", ");
        int id = Integer.parseInt(values[0]);
        TasksType tasksType = TasksType.valueOf(values[1]);
        String name = values[2];
        String status = values[3];
        String description = values[4];
        int epicId = 0;
        if (values.length == 6) {
            epicId = Integer.parseInt(values[5]);
        }
        switch (tasksType) {
            case TASK:
                Task task = new Task(name, description);
                task.setId(id);
                task.setStatus(status);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(name, description, epicId);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + tasksType);
        }
    }

    static ArrayList<String> makeList(ArrayList<Task> tasks, ArrayList<Epic> epics, ArrayList<Subtask> subtasks) {
        ArrayList<String> taskToString = new ArrayList<>();
        for (Task task : tasks) {
            taskToString.add(task.toString());
        }
        for (Epic epic : epics) {
            taskToString.add(epic.toString());
        }
        for (Subtask sub : subtasks) {
            taskToString.add(sub.toString());
        }
        return taskToString;
    }

    static List<Integer> historyFromString(String value) {
        String[] id = value.split(", ");
        List<Integer> idInList = new ArrayList<>();
        for (int i = 0; i < id.length; i++) {
            idInList.add(Integer.parseInt(id[i]));
        }
        return idInList;
    }
}


