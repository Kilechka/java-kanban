package HttpTaskServerTests;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void shouldCreateSubTest() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("task", "task", 1, "1999-09-15T05:15:00", 30);

        String jsonTask = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Subtask> tasks = taskManager.getAllSubtasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("task", tasks.get(0).getName());
    }

    @Test
    public void shouldGetAllSubTest() throws IOException, InterruptedException {
        Subtask sub = new Subtask("task", "task", 1, "1999-09-15T05:15:00", 30);
        taskManager.createNewSubtask(sub);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        final List<Subtask> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("task", tasks.get(0).getName());
    }

    @Test
    public void handleDeleteRequest() throws IOException, InterruptedException {
        taskManager.createNewSubtask(new Subtask("task", "Task", 1, "1999-09-15T05:15:00", 30));
        taskManager.createNewSubtask(new Subtask("task", "Task", 1, "1999-09-15T05:55:00", 30));

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
