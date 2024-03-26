package model;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String description, Integer epicId, String startTime, Integer duration) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer epicId) {
        this(name, description, epicId, null, null);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public TasksType getType() {
        return TasksType.SUBTASK;
    }

    @Override
    public String toString() {
        if (startTime == null & duration == null) {
            return id + ", " + TasksType.SUBTASK + ", " + name + ", " + status + ", " + description + ", " + epicId;
        } else {
            return id + ", " + TasksType.SUBTASK + ", " + name + ", " + status + ", " + description + ", " + epicId + ", " + formatter.format(startTime) + ", " + duration;
        }
    }
}


