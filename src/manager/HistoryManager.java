package manager;

import model.Task;

import java.util.ArrayList;

public interface HistoryManager {

    public <T extends Task> void add(T task);

    public ArrayList<Task> getHistory();

}