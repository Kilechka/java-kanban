package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import manager.ManagerSaveException;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

import static handler.Response.sendResponse;

public class TaskHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                handleGetRequest(httpExchange);
                break;
            case "POST":
                handlePostRequest(httpExchange);
                break;
            case "DELETE":
                handleDeleteRequest(httpExchange);
                break;
            default:
                sendResponse(httpExchange, 405, "Неизвестный метод");
        }
    }

    private void handleGetRequest(HttpExchange httpExchange) throws IOException {
        List<Task> tasks = taskManager.getAllTasks();
        String responseBody = gson.toJson(tasks);
        sendResponse(httpExchange, 200, responseBody);
    }

    private void handlePostRequest(HttpExchange httpExchange) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), "UTF-8");
        Task task = gson.fromJson(requestBody, Task.class);
        try {
            taskManager.createNewTask(task);
            sendResponse(httpExchange, 201, "Успешно");
        } catch (IllegalArgumentException e) {
            String responseBody = e.getMessage();
            sendResponse(httpExchange, 406, responseBody);
        } catch (ManagerSaveException e) {
            String responseBody = e.getMessage();
            sendResponse(httpExchange, 500, responseBody);
        }
    }

    private void handleDeleteRequest(HttpExchange httpExchange) throws IOException {
        taskManager.deleteAllTasks();
        sendResponse(httpExchange, 201, "Успешно");
    }
}
