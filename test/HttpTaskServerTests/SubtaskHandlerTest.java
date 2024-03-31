package HttpTaskServerTests;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskHandlerTest {
    private HttpTaskServer httpTaskServer;
    private HttpClient httpClient;
    static TaskManager taskManager;
    private Gson gson;
    URI url = URI.create("http://localhost:8080/subtasks");

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = Managers.getDefault();
        taskManager.createNewEpic(new Epic("ep", "ep"));
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        httpClient = HttpClient.newHttpClient();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
    }
/*
    @Test
    public void shouldCreateSubTest() throws IOException, InterruptedException {
        Subtask sub = new Subtask("task", "task", 1);
        String json = gson.toJson(sub);

        Thread.sleep(1000);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Subtask> tasks = taskManager.getAllSubtasks();

        assertNotNull(tasks);
    }

 */

    @Test
    public void shouldGetAllSubTest() throws IOException, InterruptedException {
        Subtask sub = new Subtask("task", "task", 1, "15.09.1999 05:15", 30);
        taskManager.createNewSubtask(sub);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        System.out.println(response.body());
        assertTrue(response.body().contains("\"id\":2"));
    }

    @Test
    public void handleDeleteRequest() throws IOException, InterruptedException {
        taskManager.createNewSubtask(new Subtask("task", "Task", 1, "15.09.1999 05:15", 30));
        taskManager.createNewSubtask(new Subtask("task", "Task", 1, "15.09.1999 05:59", 30));

        assertEquals(2, taskManager.getAllSubtasks().size());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertTrue(taskManager.getAllSubtasks().size() == 0);

    }
}
