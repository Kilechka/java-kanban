package handler;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends Handler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            List<Task> prioritizedList = taskManager.getPrioritizedTasks();
            String responseBody = gson.toJson(prioritizedList);
            sendResponse(exchange, 200, responseBody);
        } else {
            sendResponse(exchange, 405, "Такой метод отсутствует");
        }
    }
}