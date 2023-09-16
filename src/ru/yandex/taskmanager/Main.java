package ru.yandex.taskmanager;
import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.service.TaskManager;

/**
 * author: Pavel Korotchenkov
 * created 13.09.2023
 * updated 16.09.2023
 * ver. 1.2
 */

public class Main {
	public static void main(String[] args) {
		TaskManager taskManager = new TaskManager();

		System.out.println("Создаём и выводим на экран все задачи:");

		Task task1 = new Task("Покормить кошку утром","30 гр. корма");
		taskManager.createNewTask(task1);
		Task task2 = new Task("Покормить кошку вечером","40 гр. корма");
		taskManager.createNewTask(task2);

		Epictask epictask1 = new Epictask("Epic 1", "Get a new Job");
		taskManager.createNewEpictask(epictask1);
		Subtask subtask1 = new Subtask("Learn Java", "Finish Yandex Practicum", epictask1.getId());
		taskManager.createNewSubtask(subtask1);
		Subtask subtask2 = new Subtask("Pass Interview", "Prepare for interviews", epictask1.getId());
		taskManager.createNewSubtask(subtask2);

		Epictask epictask2 = new Epictask("Epic 2", "Buy a house");
		taskManager.createNewEpictask(epictask2);
		Subtask subtask3 = new Subtask("Money", "Get enough money", epictask2.getId());
		taskManager.createNewSubtask(subtask3);

		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();


		System.out.println("Меняем статусы обычных задач 1, 2, подзадач 1, 2:");
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
		Subtask subtask4 = new Subtask("Receive an offer", "Contact to employer", epictask2.getId());
		taskManager.createNewSubtask(subtask4);
		taskManager.updateEpictask(epictask2);
		taskManager.deleteTaskById(task1.getId());
		taskManager.deleteTaskById(task2.getId());

		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();


		System.out.println("Меняем статусы подзадач 1, 2, 4:");
		subtask1.setStatus(Status.DONE);
		subtask2.setStatus(Status.DONE);
		subtask4.setStatus(Status.DONE);
		taskManager.updateSubtask(subtask1);
		taskManager.updateSubtask(subtask2);
		taskManager.updateSubtask(subtask4);

		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();


		System.out.println("Удаляем подзадачу 4:");
		taskManager.deleteSubtaskById(subtask4.getId());
		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();


		System.out.println("Удаляем эпик 2:");
		taskManager.deleteEpictaskById(epictask2.getId());
		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();


		System.out.println("Удаляем все подзадачи:");
		taskManager.clearSubtasks();
		System.out.println(taskManager.getTasksList());
		System.out.println(taskManager.getEpictasksList());
		System.out.println(taskManager.getSubtasksList());
		System.out.println();
	}
}
