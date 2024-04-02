package HttpTaskServerTests;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrioritizedHandlerTest {
    private HttpTaskServer httpTaskServer;
    private HttpClient httpClient;
    static TaskManager taskManager;
    URI url = URI.create("http://localhost:8080/prioritized");

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        httpClient = HttpClient.newHttpClient();
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
    }

    @Test
    public void shouldGetPrioritized() throws IOException, InterruptedException {
        taskManager.createNewTask(new Task("task", "Task"));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        System.out.println(response.body());
        assertTrue(response.body().contains("\"id\":1"));
    }
}
