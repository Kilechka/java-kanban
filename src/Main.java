import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task1 = manager.createNewTask(new Task("Погладить платье", "Завтра корпоратив"));
        Task task2 = manager.createNewTask(new Task("Купить когтеточку", "Кот ободрал диван"));

        Epic epic1 = manager.createNewEpic(new Epic("Купить квартиру", "Большая ставка по ипотеке меня не пугает!"));
        Subtask subtask1Epic1 = manager.createNewSubtask(new Subtask("Выбрать квартиру", "Хочу жить ближе к Москве", 3));
        Subtask subtask2Epic1 = manager.createNewSubtask(new Subtask("Найти риелтора", "Риелтор не должен стоить дороже 200к", 3));

        Epic epic2 = manager.createNewEpic(new Epic("Выучить английский", "Полезный навык"));
        Subtask subtask1Epic2 = manager.createNewSubtask(new Subtask("Купить книгу на английском", "В книжном сейчас распродажа", 6));

        System.out.println("Эпики:");
        ArrayList<Epic> epics = manager.getAllEpics();
        for (Epic epic : epics) {
            System.out.println(epic.name + " - " + epic.description);
        }
        System.out.println("Задачи:");
        ArrayList<Task> tasks = manager.getAllTasks();
        for (Task task : tasks) {
            System.out.println(task.name + " - " + task.description);
        }
        System.out.println("Подзадачи:");
        ArrayList<Subtask> subtasks = manager.getAllSubtasks();
        for (Subtask subtask : subtasks) {
            System.out.println(subtask.name + " - " + subtask.description);
        }

        System.out.println("\nСписок всех целей:");
        ArrayList<Task> objective = manager.getAllObjective();
        for (Task task : objective) {
            System.out.println(task.name + " - " + task.description);
        }

        Task taskById = manager.getById(1);
        System.out.println("\n" + taskById.name + " - " + taskById.description);


        System.out.println(manager.getStatus(2));
        System.out.println(manager.changeStatus(task2));
        System.out.println(manager.changeStatus(task2));
        System.out.println(manager.changeStatusSub(subtask1Epic1));
        System.out.println(manager.changeStatusSub(subtask2Epic1));
        System.out.println(epic1.status);
        System.out.println(manager.changeStatusSub(subtask1Epic1));
        System.out.println(manager.changeStatusSub(subtask2Epic1));
        System.out.println(epic1.status);

       // manager.deletAllTasks();

        Task newTask = manager.update(1, new Task("Погладить кота", "Корпоратив отменяется"));
        System.out.println("Новая задача: " + newTask.name + " - " + newTask.description);

        manager.removeById(2);

        ArrayList<Subtask> getSubtasksById = manager.getSubtasksById(3);
        System.out.println("\nПодзадачи эпика (id = 3)");
        for (Subtask subtask : getSubtasksById) {
            System.out.println(subtask.name + " - " + subtask.description);
        }

        // Проверка удаление эпика и его подзадач
        manager.removeById(3);
        System.out.println("\nСписок всех целей:");
        ArrayList<Task> objective1 = manager.getAllObjective();
        for (Task task : objective1) {
            System.out.println(task.name + " - " + task.description);
        }


    }
}
