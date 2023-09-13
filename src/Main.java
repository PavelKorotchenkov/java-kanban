/**
 * author: Pavel Korotchenkov
 * created 13.09.2023
 * ver. 1.0
 */

//При выполнении задачи осталось много вопросов:
//1. Если эпик завершен, можно ли в него добавить новые подзадачи с изменением статуса эпика на IN_PROGRESS?
// Или нужно установить в завершенный эпик запрет на добавление новых подзадач?
//2. Как правильно обновлять задачи - нужно в аргументы метода update передавать и айди, и сам объект?
//3. Как именно должен обновляться статус у простых задач и подзадач? Статус передается методу update в качестве аргумента
// (но тогда нужно делать отдельные методы для каждого типа задач, т.к. эпик задача не может сама менять свой статус, т.е. будут разные параметры)
// или сначала статус задачи обновляется путем setStatus и затем в метод update передается только ID и объект?
// Из ТЗ непонятно - написано, что не должно быть отдельного метода на обновление статуса, но как тогда его изменить,
// всё-таки делать отдельные методы и передавать статус?

public class Main {
	public static void main(String[] args) {
		TaskManager taskManager = new TaskManager();

		Task task1 = new Task("Покормить кошку утром","30 гр. корма");
		Task task2 = new Task("Покормить кошку вечером","40 гр. корма");

		Epictask epictask1 = new Epictask("Epic 1", "Get a new Job");
		Subtask subtask1 = new Subtask("Learn Java", "Finish Yandex Practicum", epictask1);
		Subtask subtask2 = new Subtask("Pass Interview", "Prepare for interviews", epictask1);

		Epictask epictask2 = new Epictask("Epic 2", "Buy a house");
		Subtask subtask3 = new Subtask("Money", "Get enough money", epictask2);

		System.out.println("Создаём и выводим на экран все задачи:");
		taskManager.createNewTask(task1);
		taskManager.createNewTask(task2);
		taskManager.createNewTask(epictask1);
		taskManager.createNewTask(subtask1);
		taskManager.createNewTask(subtask2);
		taskManager.createNewTask(epictask2);
		taskManager.createNewTask(subtask3);

		System.out.println(taskManager.getTasksList());
		System.out.println();

		System.out.println("Меняем статус подзадач:");

		subtask1.setStatus(Status.IN_PROGRESS);
		subtask3.setStatus(Status.DONE);
		taskManager.updateTask(subtask1.getId(), subtask1);
		taskManager.updateTask(subtask3.getId(), subtask3);

		System.out.println(taskManager.getTasksList());
		System.out.println();

		System.out.println("Добавляем новую подзадачу в первый эпик:");
		Subtask subtask4 = new Subtask("Receive an offer", "Contact to employer", epictask1);
		taskManager.createNewTask(subtask4);
		taskManager.updateTask(epictask1.getId(), epictask1);

		System.out.println(taskManager.getTasksList());
		System.out.println();

		System.out.println("Меняем статус задач и подзадач:");
		task1.setStatus(Status.DONE);
		task2.setStatus(Status.DONE);
		subtask4.setStatus(Status.DONE);
		subtask4.setDescription("ЧТО-ТО СДЕЛАНО!");
		taskManager.updateTask(task1.getId(), task1);
		taskManager.updateTask(task2.getId(), task2);
		taskManager.updateTask(subtask4.getId(), subtask4);

		System.out.println(taskManager.getTasksList());
		System.out.println();

		System.out.println("Удаляем подзадачу:");
		taskManager.deleteTaskById(subtask4.getId());
		System.out.println(taskManager.getTasksList());
		System.out.println();

		System.out.println("Удаляем эпик:");
		taskManager.deleteTaskById(epictask2.getId());
		System.out.println(taskManager.getTasksList());
		System.out.println();

		System.out.println("Удаляем все задачи:");
		taskManager.clearAllTasks();
		System.out.println(taskManager.getTasksList());
		System.out.println();
	}
}
