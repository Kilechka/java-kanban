package HttpTaskServerTests;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EpicIdHandlerTest {
    private HttpTaskServer httpTaskServer;
    private HttpClient httpClient;
    static TaskManager taskManager;
    private Gson gson;
    URI url = URI.create("http://localhost:8080/epics/1");


    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        httpClient = HttpClient.newHttpClient();
        gson = HttpTaskServer.getGson();
        taskManager.createNewEpic(new Epic("task", "task"));
        taskManager.createNewSubtask(new Subtask("task", "task", 1, "1999-09-15T05:15:00", 30));
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
    }

    @Test
    public void shouldDeleteById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 201);
        System.out.println(taskManager.getAllEpics());
        assertTrue(taskManager.getAllEpics().size() == 0);
    }

    @Test
    public void shouldGetSubByIdEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
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
}
