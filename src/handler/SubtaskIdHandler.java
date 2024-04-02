package handler;

import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.TaskManager;
import model.Subtask;

import java.io.IOException;
import java.util.List;


public class SubtaskIdHandler extends Handler {
    int id;

    public SubtaskIdHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length < 2) {
            sendResponse(httpExchange, 400, "Запрос неверен");
            return;
        }
        try {
            id = Integer.parseInt(pathParts[2]);
        } catch (NumberFormatException e) {
            sendResponse(httpExchange, 400, "Запрос неверен");
            return;
        }
        if (taskManager.getByIdInside(id) == null) {
            sendResponse(httpExchange, 404, "Такой задачи не существует");
            return;
        }
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                handleGetRequest(httpExchange, id);
                break;
            case "PUT":
                handlePutRequest(httpExchange, id);
                break;
            case "DELETE":
                handleDeleteRequest(httpExchange, id);
                break;
            default:
                sendResponse(httpExchange, 405, "Неизвестный метод");
        }
    }

    private void handleDeleteRequest(HttpExchange httpExchange, int id) throws IOException {
        taskManager.removeByIdTask(id);
        sendResponse(httpExchange, 201, "Успешно");
    }

    private void handlePutRequest(HttpExchange httpExchange, int id) throws IOException {
        Subtask subtask = (Subtask) taskManager.getByIdInside(id);
        try {
            taskManager.updateSub(subtask);
            sendResponse(httpExchange, 201, "Успешно");
        } catch (IllegalArgumentException e) {
            String responseBody = e.getMessage();
            sendResponse(httpExchange, 406, responseBody);
        }
    }

    public void handleGetRequest(HttpExchange httpExchange, int id) throws IOException {
        List<Subtask> subtasks = taskManager.getSubtasksById(id);
        String responseBody = gson.toJson(subtasks);
        sendResponse(httpExchange, 200, responseBody);
    }
}

