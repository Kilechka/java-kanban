package model;

public class Task {
    private Integer id;
    private String name;
    private String description;
    private String status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
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
}
