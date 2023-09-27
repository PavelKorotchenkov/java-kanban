package ru.yandex.taskmanager;
import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.service.InMemoryTaskManager;
import ru.yandex.taskmanager.service.TaskManager;
import ru.yandex.taskmanager.util.Managers;

/**
 * author: Pavel Korotchenkov
 * created 13.09.2023
 * updated 16.09.2023
 * ver. 1.2
 */

public class Main {
	public static void main(String[] args) {
		Managers managers = new Managers();
		TaskManager inMemoryTaskManager = managers.getDefault();

		System.out.println("Создаём и выводим на экран все задачи:");
		Task task1 = new Task("Покормить кошку 1","30 гр. корма");
		Epictask task2 = new Epictask("Покормить кошку 2","30 гр. корма");
		Task task3 = new Task("Покормить кошку 3","30 гр. корма");
		Task task4 = new Task("Покормить кошку 4","30 гр. корма");
		Task task5 = new Task("Покормить кошку 5","30 гр. корма");
		Task task6 = new Task("Покормить кошку 6","30 гр. корма");
		Task task7 = new Task("Покормить кошку 7","30 гр. корма");
		Task task8 = new Task("Покормить кошку 8","30 гр. корма");
		Task task9 = new Task("Покормить кошку 9","30 гр. корма");
		Epictask task10 = new Epictask("Покормить кошку 10","30 гр. корма");
		Task task11 = new Task("Покормить кошку 11","30 гр. корма");
		inMemoryTaskManager.createNewTask(task1);
		inMemoryTaskManager.createNewTask(task2);
		inMemoryTaskManager.createNewTask(task3);
		inMemoryTaskManager.createNewTask(task4);
		inMemoryTaskManager.createNewTask(task5);
		inMemoryTaskManager.createNewTask(task6);
		inMemoryTaskManager.createNewTask(task7);
		inMemoryTaskManager.createNewTask(task8);
		inMemoryTaskManager.createNewTask(task9);
		inMemoryTaskManager.createNewTask(task10);
		inMemoryTaskManager.createNewTask(task11);
		inMemoryTaskManager.getTaskById(task1.getId());
		inMemoryTaskManager.getTaskById(task2.getId());
		inMemoryTaskManager.getTaskById(task3.getId());
		inMemoryTaskManager.getTaskById(task4.getId());
		inMemoryTaskManager.getTaskById(task5.getId());
		inMemoryTaskManager.getTaskById(task6.getId());
		inMemoryTaskManager.getTaskById(task7.getId());
		inMemoryTaskManager.getTaskById(task8.getId());
		inMemoryTaskManager.getTaskById(task9.getId());
		inMemoryTaskManager.getTaskById(task10.getId());
		inMemoryTaskManager.getHistory();
		System.out.println();
		inMemoryTaskManager.getTaskById(task11.getId());

		inMemoryTaskManager.getHistory();
		System.out.println(inMemoryTaskManager.getHistory());

		/*Task task1 = new Task("Покормить кошку утром","30 гр. корма");
		inMemoryTaskManager.createNewTask(task1);
		Task task2 = new Task("Покормить кошку вечером","40 гр. корма");
		inMemoryTaskManager.createNewTask(task2);

		Epictask epictask1 = new Epictask("Epic 1", "Get a new Job");
		inMemoryTaskManager.createNewEpictask(epictask1);
		Subtask subtask1 = new Subtask("Learn Java", "Finish Yandex Practicum", epictask1.getId());
		inMemoryTaskManager.createNewSubtask(subtask1);
		Subtask subtask2 = new Subtask("Pass Interview", "Prepare for interviews", epictask1.getId());
		inMemoryTaskManager.createNewSubtask(subtask2);

		Epictask epictask2 = new Epictask("Epic 2", "Buy a house");
		inMemoryTaskManager.createNewEpictask(epictask2);
		Subtask subtask3 = new Subtask("Money", "Get enough money", epictask2.getId());
		inMemoryTaskManager.createNewSubtask(subtask3);

		System.out.println(inMemoryTaskManager.getTasksList());
		System.out.println(inMemoryTaskManager.getEpictasksList());
		System.out.println(inMemoryTaskManager.getSubtasksList());
		System.out.println();


		System.out.println("Меняем статусы обычных задач 1, 2, подзадач 1, 2:");
		task1.setStatus(Status.DONE);
		task2.setStatus(Status.IN_PROGRESS);
		subtask1.setStatus(Status.IN_PROGRESS);
		subtask3.setStatus(Status.DONE);
		inMemoryTaskManager.updateTask(task1);
		inMemoryTaskManager.updateTask(task2);
		inMemoryTaskManager.updateSubtask(subtask1);
		inMemoryTaskManager.updateSubtask(subtask3);

		System.out.println(inMemoryTaskManager.getTasksList());
		System.out.println(inMemoryTaskManager.getEpictasksList());
		System.out.println(inMemoryTaskManager.getSubtasksList());
		System.out.println();


		System.out.println("Добавляем новую подзадачу во второй эпик, удаляем обычные задачи:");
		Subtask subtask4 = new Subtask("Receive an offer", "Contact to employer", epictask2.getId());
		inMemoryTaskManager.createNewSubtask(subtask4);
		inMemoryTaskManager.updateEpictask(epictask2);
		inMemoryTaskManager.deleteTaskById(task1.getId());
		inMemoryTaskManager.deleteTaskById(task2.getId());

		System.out.println(inMemoryTaskManager.getTasksList());
		System.out.println(inMemoryTaskManager.getEpictasksList());
		System.out.println(inMemoryTaskManager.getSubtasksList());
		System.out.println();


		System.out.println("Меняем статусы подзадач 1, 2, 4:");
		subtask1.setStatus(Status.DONE);
		subtask2.setStatus(Status.DONE);
		subtask4.setStatus(Status.DONE);
		inMemoryTaskManager.updateSubtask(subtask1);
		inMemoryTaskManager.updateSubtask(subtask2);
		inMemoryTaskManager.updateSubtask(subtask4);

		System.out.println(inMemoryTaskManager.getTasksList());
		System.out.println(inMemoryTaskManager.getEpictasksList());
		System.out.println(inMemoryTaskManager.getSubtasksList());
		System.out.println();


		System.out.println("Удаляем подзадачу 4:");
		inMemoryTaskManager.deleteSubtaskById(subtask4.getId());
		System.out.println(inMemoryTaskManager.getTasksList());
		System.out.println(inMemoryTaskManager.getEpictasksList());
		System.out.println(inMemoryTaskManager.getSubtasksList());
		System.out.println();


		System.out.println("Удаляем эпик 2:");
		inMemoryTaskManager.deleteEpictaskById(epictask2.getId());
		System.out.println(inMemoryTaskManager.getTasksList());
		System.out.println(inMemoryTaskManager.getEpictasksList());
		System.out.println(inMemoryTaskManager.getSubtasksList());
		System.out.println();


		System.out.println("Удаляем все подзадачи:");
		inMemoryTaskManager.clearSubtasks();
		System.out.println(inMemoryTaskManager.getTasksList());
		System.out.println(inMemoryTaskManager.getEpictasksList());
		System.out.println(inMemoryTaskManager.getSubtasksList());
		System.out.println();*/
	}
}
