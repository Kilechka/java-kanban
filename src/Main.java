import manager.Manager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
/*
Вообще не понимаю по поводу обновления задач.
Я ещё изначально подумала о том, что мы должны перезаписать старую задачу на новую (изменить название и описание) или это не так?
Какой тогда смысл методов обновления обычных задач и эпиков? Зачем вообще нужны эти методы в другом случае? Чтобы обновить мапы?

Если все же нужно сделать метод, который будет перезаписывать название и описание задачи, тогда вот следующий вопрос:
Почему в параметрах указывается только объект?
Как понять, какую именно задачу мы хотим перезаписать, если мы не отправляем в метод ID старой задачи или сам объект задачи, который хотим обновить?
Можно указать в мейне, например, task1 = manager.updateTask(new Task("Погладить кота", "Корпоратив отменяется")); (вариант без айди и без старого объекта)
Но в таком случае я не понимаю, как данной задаче присвоить её прежний ID
Помогите, пожалуйста, уже голову всю сломала

p.s В самом конце метода мейна есть кусочек кода, который как раз перезаписывает имя и описание старой таски. Я пыталась...

 */

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task1 = new Task("Погладить платье", "Завтра корпоратив");
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

        subtask1Epic1.setStatus("DONE");
        manager.updateSub(subtask1Epic1);
        System.out.println("\nПоменяли статус 1 подзадаче 1 эпика - " + subtask1Epic1.getStatus());
        System.out.println("Статус 1ого эпика: " + epic1.getStatus());

        System.out.println("\nid подзадач 1ого эпика:" + epic1.getSubtasksInEpic());
        System.out.println("id подзадач 2ого эпика:" + epic2.getSubtasksInEpic());

        System.out.println("id подзадач 2ого эпика:" + epic2.getSubtasksInEpic());

       /* manager.deleteAllSubtasks();
        System.out.println("Удалили подзадачи:" + epic2.getSubtasksInEpic() + epic1.getSubtasksInEpic());
        System.out.println("Подзадачи:");
        ArrayList<Subtask> subtasks1 = manager.getAllSubtasks();
        for (Subtask subtask : subtasks1) {
            System.out.println(subtask.getName() + " - " + subtask.getDescription());
        }
        */

        // manager.deleteAllTasks();
        manager.removeByIdTask(2);
        task1.setStatus("DONE");
        System.out.println(task1.getStatus());
        System.out.println(manager.updateTask(task1).getName());

       /* System.out.println("\nУдаляем 1 подзадачу 1ого эпика");
        manager.removeByIdSub(4);
        System.out.println("Список подзадач 1ого эпика");
        System.out.println(epic1.getSubtasksInEpic());
        System.out.println("Эпики:");
        ArrayList<Epic> epics2 = manager.getAllEpics();
        for (Epic epic : epics2) {
            System.out.println(epic.getName() + " - " + epic.getDescription());
        }
        System.out.println("Подзадачи:");
        ArrayList<Subtask> subtasks2 = manager.getAllSubtasks();
        for (Subtask subtask : subtasks2) {
            System.out.println(subtask.getName() + " - " + subtask.getDescription());
        }

        System.out.println("\nУдаляем 1 эпик");
        manager.removeByIdEpic(3);
        System.out.println("Список подзадач 1ого эпика");
        System.out.println(epic1.getSubtasksInEpic());
        System.out.println("Эпики:");
        ArrayList<Epic> epics3 = manager.getAllEpics();
        for (Epic epic : epics3) {
            System.out.println(epic.getName() + " - " + epic.getDescription());
        }
        System.out.println("Подзадачи:");
        ArrayList<Subtask> subtasks4 = manager.getAllSubtasks();
        for (Subtask subtask : subtasks4) {
            System.out.println(subtask.getName() + " - " + subtask.getDescription());
        }
*/

        Task new1Task = new Task("Погладить кота", "корпоратив отменяется");
        manager.createNewTask(new1Task);
        new1Task.setId(task1.getId());
        task1 = new1Task;
        System.out.println("\n" + task1.getName() + task1.getId());
        manager.updateTask(task1);

    }
}
