package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class TaskHandler extends Handler implements HttpHandler {
    int id;

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length == 3) {
            try {
                id = Integer.parseInt(pathParts[2]);
                handleTaskById(httpExchange, id);
            } catch (NumberFormatException e) {
                sendResponse(httpExchange, 400, "Запрос неверен");
            }
        } else if (pathParts.length == 2) {
            handleTasks(httpExchange);
        }
    }

    private void handleTasks(HttpExchange httpExchange) throws IOException {
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

    private void handleTaskById(HttpExchange httpExchange, int id) throws IOException {
        Task task = taskManager.getById(id);
        if (task == null) {
            sendResponse(httpExchange, 404, "Такой задачи не существует");
            return;
        }
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                handleGetByIdRequest(httpExchange, task);
                break;
            case "POST":
                handlePostByIdRequest(httpExchange, task);
                break;
            case "DELETE":
                handleDeleteByIdRequest(httpExchange, task);
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
        }
    }

    private void handleDeleteRequest(HttpExchange httpExchange) throws IOException {
        taskManager.deleteAllTasks();
        sendResponse(httpExchange, 201, "Успешно");
    }

    private void handleGetByIdRequest(HttpExchange httpExchange, Task task) throws IOException {
        String responseBody = gson.toJson(task);
        sendResponse(httpExchange, 200, responseBody);
    }

    private void handlePostByIdRequest(HttpExchange httpExchange, Task task) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), "UTF-8");
        Task updatedTask = gson.fromJson(requestBody, Task.class);
        updatedTask.setId(task.getId());
        taskManager.removeByIdTask(task.getId());

        task.setStatus(updatedTask.getStatus());
        task.setName(updatedTask.getName());
        task.setStartTime(updatedTask.getStartTime());
        task.setDuration(updatedTask.getDuration());
        try {
            taskManager.updateTask(task);
            sendResponse(httpExchange, 201, "Успешно");
        } catch (IllegalArgumentException e) {
            String responseBody = e.getMessage();
            sendResponse(httpExchange, 406, responseBody);
        }
    }

    private void handleDeleteByIdRequest(HttpExchange httpExchange, Task task) throws IOException {
        taskManager.removeByIdTask(task.getId());
        sendResponse(httpExchange, 201, "Успешно");
    }
}