package manager;

import model.Epic;
import model.Task;
import model.Subtask;

import java.util.ArrayList;
import java.util.List;

import static model.TasksType.TASK;
import static model.TasksType.EPIC;
import static model.TasksType.SUBTASK;

public class CSVTaskFormatter {

    static Task fromString(String value) {
        String[] values = value.split(", ");
        String typeString = values[1];
        switch (typeString) {
            case "TASK":
                Task task = new Task(values[2], values[4]);
                task.setType(TASK);
                task.setId(Integer.valueOf(values[0]));
                task.setStatus(values[3]);
                return task;
            case "EPIC":
                Epic epic = new Epic(values[2], values[4]);
                epic.setType(EPIC);
                epic.setId(Integer.valueOf(values[0]));
                epic.setStatus(values[3]);
                return epic;
            case "SUBTASK":
                Subtask subtask = new Subtask(values[2], values[4], Integer.valueOf(values[5]));
                subtask.setType(SUBTASK);
                subtask.setId(Integer.valueOf(values[0]));
                subtask.setStatus(values[3]);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + typeString);
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


