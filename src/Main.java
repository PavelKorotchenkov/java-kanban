/**
 * author: Pavel Korotchenkov
 * created 13.09.2023
 * ver. 1.0
 */

//Так и не понял, как правильно менять статус у задач)
//Если через setStatus - то зачем метод updateTask
//А если создавать новый объект - то как из него получить нужный ID, т.к. у нового объекта его ещё нет

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
		taskManager.createNewEpictask(epictask1);
		taskManager.createNewSubtask(subtask1);
		taskManager.createNewSubtask(subtask2);
		taskManager.createNewEpictask(epictask2);
		taskManager.createNewSubtask(subtask3);

		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();

		System.out.println("Меняем статус подзадач:");
		subtask1.setStatus(Status.IN_PROGRESS);
		subtask3.setStatus(Status.DONE);
		taskManager.updateSubtask(subtask1);
		taskManager.updateSubtask(subtask3);

		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();

		System.out.println("Добавляем новую подзадачу в первый эпик:");
		Subtask subtask4 = new Subtask("Receive an offer", "Contact to employer", epictask1);
		taskManager.createNewSubtask(subtask4);
		taskManager.updateEpictask(epictask1);

		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();

		System.out.println("Меняем статус задач и подзадач:");
		task1.setStatus(Status.DONE);
		task2.setStatus(Status.DONE);
		subtask4.setStatus(Status.DONE);
		subtask4.setDescription("ЧТО-ТО СДЕЛАНО!");
		taskManager.updateTask(task1);
		taskManager.updateTask(task2);
		taskManager.updateSubtask(subtask4);

		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();

		System.out.println("Удаляем подзадачу:");
		taskManager.deleteTaskById(subtask4.getId());
		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();

		System.out.println("Удаляем эпик:");
		taskManager.deleteTaskById(epictask2.getId());
		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();

		System.out.println("Удаляем все задачи:");
		taskManager.clearTasks();
		taskManager.clearEpictasks();
		taskManager.clearSubtasks();
		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();
	}
}
