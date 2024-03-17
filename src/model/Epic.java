package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksInEpic = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, null, null);
    }

    public ArrayList<Integer> getSubtasksInEpic() {
        return subtasksInEpic;
    }

    public void setSubtasksInEpic(Integer id) {
        subtasksInEpic.add(id);
    }

    @Override
    public String toString() {
        if (startTime != null && duration != null) {
            return id + ", " + TasksType.EPIC + ", " + name + ", " + status + ", " + description + ", " + formatter.format(startTime) + ", " + duration.toMinutes();
        } else {
            return id + ", " + TasksType.EPIC + ", " + name + ", " + status + ", " + description;
        }
    }

    @Override
    public TasksType getType() {
        return TasksType.EPIC;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
