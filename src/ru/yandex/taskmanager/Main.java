package ru.yandex.taskmanager;

import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.service.TaskManager;
import ru.yandex.taskmanager.util.Managers;

/**
 * Sprint 5
 * author: Pavel Korotchenkov
 * created 27.09.2023
 * upd 26.10.2023
 * ver. 1.3
 */

public class Main {
	public static void main(String[] args) {
		TaskManager inMemoryTaskManager = Managers.getDefault();

		Task task1 = new Task("Задача №1", "Легкая");
		inMemoryTaskManager.createNewTask(task1);
		Task task2 = new Task("Задача №2", "Легкая");
		inMemoryTaskManager.createNewTask(task2);

		Epictask task3 = new Epictask("Эпик Задача №1", "Сложная");
		inMemoryTaskManager.createNewEpictask(task3);
		Subtask task4 = new Subtask("Подзадача №1", "Средняя", task3.getId());
		inMemoryTaskManager.createNewSubtask(task4);
		Subtask task5 = new Subtask("Подзадача №2", "Средняя", task3.getId());
		inMemoryTaskManager.createNewSubtask(task5);
		Subtask task6 = new Subtask("Подзадача №3", "Средняя", task3.getId());
		inMemoryTaskManager.createNewSubtask(task6);

		Epictask task7 = new Epictask("Эпик Задача №2", "Сложная");
		inMemoryTaskManager.createNewEpictask(task7);

		inMemoryTaskManager.getTaskById(task1.getId());
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.getTaskById(task2.getId());
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.getTaskById(task1.getId());
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.getEpictaskById(task3.getId());
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.getTaskById(task1.getId());
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.getSubtaskById(task4.getId());
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.getSubtaskById(task6.getId());
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.getSubtaskById(task4.getId());
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.getSubtaskById(task5.getId());
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.getSubtaskById(task5.getId());
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.getTaskById(task1.getId());
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.deleteTaskById(2);
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.deleteEpictaskById(3);
		System.out.println(inMemoryTaskManager.getHistory() + "\n");

		inMemoryTaskManager.deleteTaskById(1);
		System.out.println(inMemoryTaskManager.getHistory() + "\n");
	}
}
