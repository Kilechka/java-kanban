package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    protected Integer id;
    protected String name;
    protected String description;
    protected String status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description, String startTime, Integer duration) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        if (startTime != null && !startTime.isEmpty()) {
            this.startTime = LocalDateTime.parse(startTime, formatter);
        } else {
            this.startTime = null;
        }
        if (duration != null) {
            this.duration = Duration.ofMinutes(duration);
        } else {
            this.duration = Duration.ZERO;
        }
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TasksType getType() {
        return TasksType.TASK;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Task task = (Task) obj;
        return name.equals(task.name) && id == task.id && description.equals(task.description) && status.equals(task.status);
    }

    @Override
    public String toString() {
        return id + ", " + TasksType.TASK + ", " + name + ", " + status + ", " + description + ", " + formatter.format(startTime) + ", " + duration.toMinutes();
    }

    public boolean doTheTasksIntersect(Task otherTask) {
        boolean doesNotStartAfterThisEnds = !this.getStartTime().isAfter(otherTask.getEndTime());
        boolean doesNotEndBeforeThisStarts = !this.getEndTime().isBefore(otherTask.getStartTime());
        return doesNotStartAfterThisEnds && doesNotEndBeforeThisStarts;
    }
}
