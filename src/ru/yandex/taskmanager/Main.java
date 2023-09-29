package ru.yandex.taskmanager;

import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.service.TaskManager;
import ru.yandex.taskmanager.util.Managers;

/**
 * Sprint 4
 * author: Pavel Korotchenkov
 * created 27.09.2023
 * upd 29.09.2023
 * ver. 1.1
 */

//Влад, спасибо за ревью!)
public class Main {
	public static void main(String[] args) {
		TaskManager inMemoryTaskManager = Managers.getDefault();

		Task task1 = new Task("Задача №1", "Легкая");
		inMemoryTaskManager.createNewTask(task1);
		Epictask task2 = new Epictask("Задача №2", "Сложная");
		inMemoryTaskManager.createNewEpictask(task2);
		Subtask task3 = new Subtask("Задача №3", "Средняя", task2.getId());
		inMemoryTaskManager.createNewSubtask(task3);
		Task task4 = new Task("Задача №4", "Легкая");
		inMemoryTaskManager.createNewTask(task4);
		Task task5 = new Task("Задача №5", "Легкая");
		inMemoryTaskManager.createNewTask(task5);
		Task task6 = new Task("Задача №6", "Легкая");
		inMemoryTaskManager.createNewTask(task6);
		Task task7 = new Task("Задача №7", "Легкая");
		inMemoryTaskManager.createNewTask(task7);
		Task task8 = new Task("Задача №8", "Легкая");
		inMemoryTaskManager.createNewTask(task8);
		Task task9 = new Task("Задача №9", "Легкая");
		inMemoryTaskManager.createNewTask(task9);
		Epictask task10 = new Epictask("Задача №10", "Сложная");
		inMemoryTaskManager.createNewEpictask(task10);
		Task task11 = new Task("Задача №11", "Легкая");
		inMemoryTaskManager.createNewTask(task11);

		inMemoryTaskManager.getTaskById(task1.getId());
		inMemoryTaskManager.getEpictaskById(task2.getId());
		inMemoryTaskManager.getSubtaskById(task3.getId());
		inMemoryTaskManager.getTaskById(task4.getId());
		inMemoryTaskManager.getTaskById(task5.getId());
		inMemoryTaskManager.getTaskById(task6.getId());
		inMemoryTaskManager.getTaskById(task7.getId());
		inMemoryTaskManager.getTaskById(task8.getId());
		inMemoryTaskManager.getTaskById(task9.getId());
		inMemoryTaskManager.getEpictaskById(task10.getId());

		System.out.println(inMemoryTaskManager.getHistory());
		System.out.println();

		inMemoryTaskManager.getTaskById(task11.getId());
		System.out.println(inMemoryTaskManager.getHistory());
	}
}
