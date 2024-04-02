package handler;

import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.TaskManager;
import model.Subtask;

import java.io.IOException;
import java.util.List;

public class SubtaskHandler extends Handler {

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
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
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        String responseBody = gson.toJson(subtasks);
        sendResponse(httpExchange, 200, responseBody);
    }

    private void handlePostRequest(HttpExchange httpExchange) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), "UTF-8");
        Subtask subtask = gson.fromJson(requestBody, Subtask.class);
        try {
            taskManager.createNewSubtask(subtask);
            sendResponse(httpExchange, 201, "Успешно");
        } catch (IllegalArgumentException e) {
            String responseBody = e.getMessage();
            sendResponse(httpExchange, 406, responseBody);
        }
    }

    private void handleDeleteRequest(HttpExchange httpExchange) throws IOException {
        taskManager.deleteAllSubtasks();
        sendResponse(httpExchange, 201, "Успешно");
    }
}
