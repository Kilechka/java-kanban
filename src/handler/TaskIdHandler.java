package handler;

import com.sun.net.httpserver.HttpExchange;
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
                handleGetByIdRequest(httpExchange, id);
                break;
            case "POST":
                handlePostByIdRequest(httpExchange, id);
                break;
            case "DELETE":
                handleDeleteByIdRequest(httpExchange, id);
                break;
            default:
                sendResponse(httpExchange, 405, "Неизвестный метод");
        }
    }

    private void handleDeleteByIdRequest(HttpExchange httpExchange, int id) throws IOException {
        taskManager.removeByIdTask(id);
        sendResponse(httpExchange, 201, "Успешно");
    }

    private void handlePostByIdRequest(HttpExchange httpExchange, int id) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), "UTF-8");
        Task updatedTask = gson.fromJson(requestBody, Task.class);
        updatedTask.setId(id);
        taskManager.removeByIdTask(updatedTask.getId());


        Task existingTask = taskManager.getById(id);

        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setName(updatedTask.getName());
        existingTask.setStartTime(updatedTask.getStartTime());
        existingTask.setDuration(updatedTask.getDuration());
        try {
            taskManager.updateTask(existingTask);
            sendResponse(httpExchange, 201, "Успешно");
        } catch (IllegalArgumentException e) {
            String responseBody = e.getMessage();
            sendResponse(httpExchange, 406, responseBody);
        }
    }

    public void handleGetByIdRequest(HttpExchange httpExchange, int id) throws IOException {
        Task task = taskManager.getById(id);
        String responseBody = gson.toJson(task);
        sendResponse(httpExchange, 200, responseBody);
    }
}


