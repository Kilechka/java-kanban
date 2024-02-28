package manager;

import model.Node;
import model.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private final List<Task> history = new ArrayList<>();
    private Node tail;
    private Node head;

    @Override
    public void add(Task task) {
        if (task != null) {
            Node node = nodeMap.get(task.getId());
            if (node != null) {
                remove(task.getId());
            } else {
                node = new Node(task);
            }
            linkLast(node);
            nodeMap.put(task.getId(), node);
        }
    }

    @Override
    public List<Task> getHistory() {
        getTask();
        return new ArrayList<>(history);
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.get(id);
        if (node != null && node.task != null) {
            removeNode(nodeMap.get(id));
            nodeMap.remove(id);
        }
    }

    private void linkLast(Node node) {
        if (nodeMap.size() == 0) {
            node.prev = null;
            node.next = null;
            tail = node;
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }

    private void getTask() {
        for (Node node : nodeMap.values()) {
            history.add(node.task);
        }
    }

    private void removeNode(Node node) {
        Node nodePrev = node.prev;
        Node nodeNext = node.next;
        if (node == tail && nodePrev != null) {
            nodePrev.next = null;
            tail = nodePrev;
        } else if (node == head && nodeNext != null) {
            nodeNext.prev = null;
            head = nodeNext;
        } else if (nodePrev == null && nodeNext == null) {
            tail = null;
            head = null;
        } else {
            nodePrev.next = nodeNext;
            nodeNext.prev = nodePrev;
        }
    }
}
