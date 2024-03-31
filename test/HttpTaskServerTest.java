import http.HttpTaskServer;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    TaskManager manager;
    HttpTaskServer taskServer;

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    public void createTaskTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/task"))
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"Test task\", \"description\": \"Test description\"}"))
                .build();
        //  HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //  System.out.println(response);

        //  assertEquals(201, response.statusCode());
    }
}
