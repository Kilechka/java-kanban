import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Погладить платье", "Завтра свидание", "02.09.1999 00:00", 30);
        taskManager.createNewTask(task1);
        Task task2 = new Task("Купить когтеточку", "Кот ободрал диван", "01.09.1999 00:00", 30);
        taskManager.createNewTask(task2);


        Epic epic1 = new Epic("Купить квартиру", "Большая ставка по ипотеке меня не пугает!");
        taskManager.createNewEpic(epic1);
        Subtask subtask1Epic1 = new Subtask("Выбрать квартиру", "Хочу жить ближе к Москве", 3, "01.09.1999 01:00", 15);
        taskManager.createNewSubtask(subtask1Epic1);
        Subtask subtask2Epic1 = new Subtask("Найти риелтора", "Риелтор не должен стоить дороже 200к", 3, "04.09.1999 04:00", 30);
        taskManager.createNewSubtask(subtask2Epic1);
        Epic epic2 = new Epic("Выучить английский", "Полезный навык");
        taskManager.createNewEpic(epic2);
        Subtask subtask1Epic2 = new Subtask("Купить книгу на английском", "В книжном сейчас распродажа", 6, "03.09.1999 02:00", 10);
        taskManager.createNewSubtask(subtask1Epic2);
        taskManager.getById(3);
        taskManager.getById(2);
        taskManager.getById(3);
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task.getStartTime());
        }

/*
        File file = new File("resources/history.csv");
        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);
*/

        taskManager.getAllEpics();
        taskManager.getAllTasks();

        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubtasksById(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
