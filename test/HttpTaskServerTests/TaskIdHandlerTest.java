package HttpTaskServerTests;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskIdHandlerTest {
    private HttpTaskServer httpTaskServer;
    private HttpClient httpClient;
    static TaskManager taskManager;
    private Gson gson;
    URI url = URI.create("http://localhost:8080/tasks/1");
    Task task;
    Task task1;

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        httpClient = HttpClient.newHttpClient();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        task = new Task("task", "task", "1999-09-15T06:00:00", 30);
        taskManager.createNewTask(task);
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
    }

    @Test
    public void shouldHandleGetRequestTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Task task = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode());
        assertTrue(task.getId() == 1);
    }

    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        Task task = taskManager.getById(1);
        task.setStatus("DONE");
        String jsonTask = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        Task updatedTask = taskManager.getByIdInside(task.getId());
        assertEquals("DONE", updatedTask.getStatus());
    }

    @Test
    public void shouldDeleteById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        assertTrue(taskManager.getAllTasks().size() == 0);
    }
}
