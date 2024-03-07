package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksInEpic = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubtasksInEpic() {
        return subtasksInEpic;
    }

    public void setSubtasksInEpic(Integer id) {
        subtasksInEpic.add(id);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
