package HttpTaskServerTests;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EpicHandlerTest {
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

    @Test
    public void shouldCreateEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("task", "task");
        String json = gson.toJson(epic);
        URI url = URI.create("http://localhost:8080/epics");


        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        // Thread.sleep(1000);
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .proxy(ProxySelector.of(
                        new InetSocketAddress(
                                "1.1.1.1", 1111
                        )
                ))
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        //  HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks);
    }

    @Test
    public void shouldGetAllEpicsTest() throws IOException, InterruptedException {
        taskManager.createNewEpic(new Epic("task", "Task"));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"id\":1"));
    }

    @Test
    public void handleDeleteRequest() throws IOException, InterruptedException {
        taskManager.createNewEpic(new Epic("task", "Task"));
        taskManager.createNewEpic(new Epic("task", "Task"));

        assertEquals(2, taskManager.getAllEpics().size());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json;charset=utf-8")
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(taskManager.getAllEpics().size() == 0);
    }
}

