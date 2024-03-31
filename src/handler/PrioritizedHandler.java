package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

import static handler.Response.sendResponse;

public class PrioritizedHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<Task> prioritizedList = taskManager.getPrioritizedTasks();
        String responseBody = gson.toJson(prioritizedList);
        sendResponse(exchange, 200, responseBody);
    }
}