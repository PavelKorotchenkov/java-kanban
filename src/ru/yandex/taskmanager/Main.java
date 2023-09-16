package ru.yandex.taskmanager;
import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.service.TaskManager;

/**
 * author: Pavel Korotchenkov
 * created 13.09.2023
 * updated 14.09.2023
 * ver. 1.1
 */

//Влад, спасибо!
//Но я так и не понял, как правильно менять статус у задач)
//Если через setStatus (у меня сейчас так) - то зачем нужен метод updateTask, ведь можно все через set установить
//(кроме подзадач, т.к. нужно проверить и обновить статус эпика),
//А если создавать и передавать новый объект - то как из него получить нужный ID,
//Ведь у нового объекта ID ещё нет.

public class Main {
	public static void main(String[] args) {
		TaskManager taskManager = new TaskManager();

		Task task1 = new Task("Покормить кошку утром","30 гр. корма");
		taskManager.createNewTask(task1);
		System.out.println(taskManager.getTasksList());
		System.out.println();

		Task task2 = taskManager.getTaskById(task1.getId());
		task2.setName("Погулять");
		task2.setDescription("Не менее 30 минут");
		taskManager.updateTask(task2);
		System.out.println(taskManager.getTasksList());
		System.out.println();
		/*Task task1 = new Task("Покормить кошку утром","30 гр. корма");
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

		System.out.println("Меняем статусы :");
		task1.setStatus(Status.DONE);
		task2.setStatus(Status.IN_PROGRESS);
		subtask1.setStatus(Status.IN_PROGRESS);
		subtask3.setStatus(Status.DONE);
		taskManager.updateTask(task1);
		taskManager.updateTask(task2);
		taskManager.updateSubtask(subtask1);
		taskManager.updateSubtask(subtask3);

		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();

		System.out.println("Добавляем новую подзадачу во второй эпик, удаляем обычные задачи:");
		Subtask subtask4 = new Subtask("Receive an offer", "Contact to employer", epictask2);
		taskManager.createNewSubtask(subtask4);
		taskManager.updateEpictask(epictask2);
		taskManager.deleteTaskById(task1.getId());
		taskManager.deleteTaskById(task2.getId());

		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();

		System.out.println("Меняем статусы:");
		subtask1.setStatus(Status.DONE);
		subtask2.setStatus(Status.DONE);
		subtask2.setDescription("Задача завершена!");
		subtask4.setStatus(Status.DONE);
		taskManager.updateSubtask(subtask1);
		taskManager.updateSubtask(subtask2);
		taskManager.updateSubtask(subtask4);

		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();

		System.out.println("Удаляем подзадачу:");
		taskManager.deleteSubtaskById(subtask4.getId());
		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();

		System.out.println("Удаляем эпик:");
		taskManager.deleteEpictaskById(epictask2.getId());
		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();

		System.out.println("Удаляем подзадачи:");
		taskManager.clearSubtasks();
		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();*/
	}
}
