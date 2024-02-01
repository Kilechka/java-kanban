package manager;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> history = new ArrayList<>();
    static final int MAX_SIZE_OF_HISTORY = 10;

    @Override
    public void add(Task task) {
        if (task != null) {
            history.add(task);
            if (history.size() > MAX_SIZE_OF_HISTORY) {
                history.remove(0);
            }
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
