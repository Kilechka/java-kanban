package HttpTaskServerTests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import http.HttpTaskServer;

import static org.junit.jupiter.api.Assertions.*;

public class TaskHandlerTest {
    private HttpTaskServer httpTaskServer;
    private HttpClient httpClient;
    static TaskManager taskManager;
    private Gson gson;
    private URI url;

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpClient = HttpClient.newHttpClient();
        gson = HttpTaskServer.getGson();
        httpTaskServer.start();
        url = URI.create("http://localhost:8080/tasks");
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
    }

    @Test
    public void shouldCreateTaskTest() throws IOException, InterruptedException {
        Task task = new Task("task", "task");

        String jsonTask = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("task", tasks.get(0).getName());
    }

    @Test
    public void shouldGetAllTasksTest() throws IOException, InterruptedException {
        taskManager.createNewTask(new Task("task1", "Task", "1999-09-15T05:15:00", 30));
        taskManager.createNewTask(new Task("task", "Task"));

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        final List<Task> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertEquals("task1", tasks.get(0).getName());
    }

    @Test
    public void handleDeleteRequest() throws IOException, InterruptedException {
        taskManager.createNewTask(new Task("task", "Task"));
        taskManager.createNewTask(new Task("task", "Task"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(taskManager.getAllTasks().size() == 0);
    }
}