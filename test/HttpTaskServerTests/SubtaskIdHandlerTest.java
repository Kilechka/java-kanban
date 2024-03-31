package HttpTaskServerTests;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
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

// почему в idea именно в этом классе у меня пропали все кнопки запуска тестов?

public class SubtaskIdHandlerTest {
    private HttpTaskServer httpTaskServer;
    private HttpClient httpClient;
    static TaskManager taskManager;
    private Gson gson;
    URI url = URI.create("http://localhost:8080/subtasks/2");


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
    private void shouldupdateSub() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("task", "task", 1, "15.09.1999 05:15", 30);
        taskManager.createNewTask(subtask);

        subtask.setStatus("DONE");

        String json = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task updatedTask = taskManager.getById(subtask.getId());
        assertEquals("DONE", updatedTask.getStatus());
    }
}