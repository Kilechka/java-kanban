package handler;

import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends Handler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod() == "GET") {
            List<Task> history = taskManager.getHistory();
            String responseBody = gson.toJson(history);
            sendResponse(exchange, 200, responseBody);
        } else {
            sendResponse(exchange, 405, "Такой метод отсутствует");
        }
    }
}
