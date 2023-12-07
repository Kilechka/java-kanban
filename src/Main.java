import manager.Manager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
/*
Надеюсь, я ничего не упустила :D
Подскажите, правильно ли я поняла, что метод удаления подчадач удаляет подзадачи определённого эпика?
Или нужно было сразу всё удалить?
p.s Хотела поблагодарить вас за проделанную вами работу. Спасибо за терепение!
 */

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
            System.out.println(epic.getName() + " - " + epic.getDescription());
        }
        System.out.println("Задачи:");
        ArrayList<Task> tasks = manager.getAllTasks();
        for (Task task : tasks) {
            System.out.println(task.getName() + " - " + task.getDescription());
        }
        System.out.println("Подзадачи:");
        ArrayList<Subtask> subtasks = manager.getAllSubtasks();
        for (Subtask subtask : subtasks) {
            System.out.println(subtask.getName() + " - " + subtask.getDescription());
        }

        System.out.println("\nid подзадач 1ого эпика:" + epic1.getSubtasksInEpic());
        System.out.println("id подзадач 2ого эпика:" + epic2.getSubtasksInEpic());

        System.out.println("\nСтатус задачи под 3 id: " + manager.getStatus(3));

        System.out.println("\nПолучение задачи под номером 1: ");
        Task taskById = manager.getById(1);
        System.out.println(taskById.getName() + " - " + taskById.getDescription());

        System.out.println("\nПроцесс изменения статуса задачи 2:");
        System.out.println(manager.getStatus(2));
        System.out.println(manager.changeStatus(task2));
        System.out.println(manager.changeStatus(task2));


        System.out.println("\nПроцесс изменения статуса задачи 3(эпик):");
        System.out.println(manager.changeStatusSub(epic1));
        System.out.println("Меняем 1 саб 3его эпика");
        System.out.println(manager.changeStatus(subtask1Epic1));
        System.out.println("Меняем 2 саб 3его эпика");
        System.out.println(manager.changeStatus(subtask2Epic1));
        System.out.println("Статус задачи 3(эпик):");
        System.out.println(manager.changeStatusSub(epic1));
        System.out.println("Меняем 1 саб 3его эпика");
        System.out.println(manager.changeStatus(subtask1Epic1));
        System.out.println("Меняем 2 саб 3его эпика");
        System.out.println(manager.changeStatus(subtask2Epic1));
        System.out.println("Статус задачи 3(эпик):");
        System.out.println(manager.changeStatusSub(epic1));


        System.out.println("id подзадач 2ого эпика:" + epic2.getSubtasksInEpic());
        manager.deleteSubtasksByEpicId(6);
        System.out.println("Удалили 6 задачу. id подзадач 2ого эпика:" + epic2.getSubtasksInEpic());
        System.out.println("Подзадачи:");
        ArrayList<Subtask> subtasks1 = manager.getAllSubtasks();
        for (Subtask subtask : subtasks1) {
            System.out.println(subtask.getName() + " - " + subtask.getDescription());
        }


        // manager.deleteAllTasks();
        manager.removeByIdTask(2);


        Task newTask = manager.update(1, new Task("Погладить кота", "Корпоратив отменяется"));
        System.out.println("\nНовая задача: " + newTask.getName() + " - " + newTask.getDescription());


        ArrayList<Subtask> getSubtasksById = manager.getSubtasksById(3);
        System.out.println("\nПодзадачи эпика (id = 3)");
        for (Subtask subtask : getSubtasksById) {
            System.out.println(subtask.getName() + " - " + subtask.getDescription());
        }


    }


}
