package handler;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.List;

public class SubtaskHandler extends Handler {
    int id;


    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length == 3) {
            try {
                id = Integer.parseInt(pathParts[2]);
                handleSubtaskById(httpExchange, id);
            } catch (NumberFormatException e) {
                sendResponse(httpExchange, 400, "Запрос неверен");
            }
        } else if (pathParts.length == 2) {
            handleSubtask(httpExchange);
        }
    }

    private void handleSubtask(HttpExchange httpExchange) throws IOException {
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

    private void handleSubtaskById(HttpExchange httpExchange, int id) throws IOException {
        Task task = taskManager.getById(id);
        if (task == null) {
            sendResponse(httpExchange, 404, "Такой задачи не существует");
            return;
        }
        switch (httpExchange.getRequestMethod()) {
            case "POST":
                handlePostRequest(httpExchange, id);
                break;
            case "DELETE":
                handleDeleteRequest(httpExchange, id);
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

    private void handleDeleteRequest(HttpExchange httpExchange, int id) throws IOException {
        taskManager.removeByIdSub(id);
        sendResponse(httpExchange, 201, "Успешно");
    }

    private void handlePostRequest(HttpExchange httpExchange, int id) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), "UTF-8");
        Task updatedTask = gson.fromJson(requestBody, Task.class);
        updatedTask.setId(id);

        taskManager.getById(id).setStatus(updatedTask.getStatus());

        try {
            taskManager.updateSub((Subtask) taskManager.getByIdInside(id));
            sendResponse(httpExchange, 201, "Успешно");
        } catch (IllegalArgumentException e) {
            String responseBody = e.getMessage();
            sendResponse(httpExchange, 406, responseBody);
        }
    }
}
