package handler;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends Handler {
    int id;

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length == 3) {
            try {
                id = Integer.parseInt(pathParts[2]);
                handleEpicsById(httpExchange, id);
            } catch (NumberFormatException e) {
                sendResponse(httpExchange, 400, "Запрос неверен");
            }
        } else if (pathParts.length == 2) {
            handleEpics(httpExchange);
        }
    }

    private void handleEpics(HttpExchange httpExchange) throws IOException {
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

    private void handleEpicsById(HttpExchange httpExchange, int id) throws IOException {
        Task task = taskManager.getById(id);
        if (task == null) {
            sendResponse(httpExchange, 404, "Такой задачи не существует");
            return;
        }
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                handleGetRequest(httpExchange, id);
                break;
            case "DELETE":
                handleDeleteRequest(httpExchange, id);
                break;
            default:
                sendResponse(httpExchange, 405, "Неизвестный метод");
        }
    }

    private void handleDeleteRequest(HttpExchange httpExchange) throws IOException {
        taskManager.deleteAllEpics();
        sendResponse(httpExchange, 201, "Успешно");
    }

    private void handlePostRequest(HttpExchange httpExchange) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), "UTF-8");
        Epic epic = gson.fromJson(requestBody, Epic.class);
        try {
            taskManager.createNewEpic(epic);
            sendResponse(httpExchange, 201, "Успешно");
        } catch (IllegalArgumentException e) {
            String responseBody = e.getMessage();
            sendResponse(httpExchange, 406, responseBody);
        }
    }

    private void handleGetRequest(HttpExchange httpExchange) throws IOException {
        List<Epic> epics = taskManager.getAllEpics();
        String responseBody = gson.toJson(epics);
        sendResponse(httpExchange, 200, responseBody);
    }

    private void handleDeleteRequest(HttpExchange httpExchange, int id) throws IOException {
        taskManager.removeByIdEpic(id);
        sendResponse(httpExchange, 201, "Успешно");
    }

    public void handleGetRequest(HttpExchange httpExchange, int id) throws IOException {
        List<Subtask> subtasks = taskManager.getSubtasksById(id);
        String responseBody = gson.toJson(subtasks);
        sendResponse(httpExchange, 200, responseBody);
    }
}
