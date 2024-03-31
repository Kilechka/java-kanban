package HttpTaskServerTests;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.Managers;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import http.HttpTaskServer;

import static org.junit.jupiter.api.Assertions.*;

public class TaskHandlerTest {
    private HttpTaskServer httpTaskServer;
    private HttpClient httpClient;
    static TaskManager taskManager;
    private Gson gson;

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = Managers.getDefault();
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
    public void shouldCreateTaskTest() throws IOException, InterruptedException {
        Task task = new Task("task", "task", "15.09.1999 05:15", 30);
        String json = gson.toJson(task);
        URI url = URI.create("http://localhost:8080/tasks");

        Thread.sleep(1000);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks);
    }

    @Test
    public void shouldGetAllTasksTest() throws IOException, InterruptedException {
        taskManager.createNewTask(new Task("task", "Task"));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"id\":1"));
    }

 */

    @Test
    public void handleDeleteRequest() throws IOException, InterruptedException {
        taskManager.createNewTask(new Task("task", "Task"));
        taskManager.createNewTask(new Task("task", "Task"));

        assertEquals(2, taskManager.getAllTasks().size());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Thread.sleep(1000);
        assertEquals(201, response.statusCode());
        assertTrue(taskManager.getAllTasks().size() == 0);
    }
}