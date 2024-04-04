package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            return id + ", " + TasksType.EPIC + ", " + name + ", " + status + ", " + description + ", " + startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ", " + duration;
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
