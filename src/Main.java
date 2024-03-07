import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {
    
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Погладить платье", "Завтра свидание");
        manager.createNewTask(task1);
        Task task2 = new Task("Купить когтеточку", "Кот ободрал диван");
        manager.createNewTask(task2);
        Epic epic1 = new Epic("Купить квартиру", "Большая ставка по ипотеке меня не пугает!");
        manager.createNewEpic(epic1);
        Subtask subtask1Epic1 = new Subtask("Выбрать квартиру", "Хочу жить ближе к Москве", 3);
        manager.createNewSubtask(subtask1Epic1);
        Subtask subtask2Epic1 = new Subtask("Найти риелтора", "Риелтор не должен стоить дороже 200к", 3);
        manager.createNewSubtask(subtask2Epic1);
        Epic epic2 = new Epic("Выучить английский", "Полезный навык");
        manager.createNewEpic(epic2);
        Subtask subtask1Epic2 = new Subtask("Купить книгу на английском", "В книжном сейчас распродажа", 6);
        manager.createNewSubtask(subtask1Epic2);
        printAllTasks(manager);
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
