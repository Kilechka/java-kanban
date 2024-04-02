package handler;

import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.TaskManager;
import model.Task;

import java.io.IOException;

public class TaskIdHandler extends Handler {
    int id;

    public TaskIdHandler(TaskManager taskManager) {
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
            case "POST":
                handleStatusRequest(httpExchange, id);
            default:
                sendResponse(httpExchange, 405, "Неизвестный метод");
        }
    }

    private void handleStatusRequest(HttpExchange httpExchange, int id) throws IOException {
        Task task = taskManager.getByIdInside(id);
        String status = new String(httpExchange.getRequestBody().readAllBytes(), "UTF-8");
        status = gson.fromJson(status, String.class);
        task.setStatus(status);
        taskManager.updateTask(task);
        sendResponse(httpExchange, 201, "Успешно");
    }

    private void handleDeleteRequest(HttpExchange httpExchange, int id) throws IOException {
        taskManager.removeByIdTask(id);
        sendResponse(httpExchange, 201, "Успешно");
    }

    private void handlePutRequest(HttpExchange httpExchange, int id) throws IOException {
        Task task = taskManager.getByIdInside(id);
        try {
            taskManager.updateTask(task);
            sendResponse(httpExchange, 201, "Успешно");
        } catch (IllegalArgumentException e) {
            String responseBody = e.getMessage();
            sendResponse(httpExchange, 406, responseBody);
        }
    }

    public void handleGetRequest(HttpExchange httpExchange, int id) throws IOException {
        Task task = taskManager.getById(id);
        String responseBody = gson.toJson(task);
        sendResponse(httpExchange, 200, responseBody);
    }
}
