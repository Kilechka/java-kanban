import http.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }
}