package manager;

import model.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node tail;
    private Node head;

    @Override
    public void add(Task task) {
        if (task != null) {
            Node node = nodeMap.get(task.getId());
            linkLast(task);
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTask());
    }

    @Override
    public void remove(int id) {
        removeNode(nodeMap.get(id));
    }

    private void linkLast(Task task) {
        Node node = new Node(tail, task, null);
        if (nodeMap.size() == 0) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        nodeMap.put(task.getId(), node);
    }

    private List<Task> getTask() {
        List<Task> history = new ArrayList<>();
        Node node = head;
        while (node != null) {
            history.add(node.task);
            node = node.next;
        }
        return history;
    }

    private void removeNode(Node node) {
        if (node != null && node.task != null) {
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
            nodeMap.remove(node);
        }
    }

    private static class Node {
        public Task task;
        public Node prev;
        public Node next;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }
}
