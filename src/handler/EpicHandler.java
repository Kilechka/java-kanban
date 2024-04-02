package handler;

import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.TaskManager;
import model.Epic;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends Handler {

    public EpicHandler(TaskManager taskManager) {
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
}
