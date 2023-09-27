package ru.yandex.taskmanager;
import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.service.HistoryManager;
import ru.yandex.taskmanager.service.TaskManager;
import ru.yandex.taskmanager.util.Managers;

/**
 * author: Pavel Korotchenkov
 * created 27.09.2023
 * ver. 1.0
 */

public class Main {
	public static void main(String[] args) {
		TaskManager inMemoryTaskManager = Managers.getDefault();
		HistoryManager historyManager = Managers.getDefaultHistory();

		Task task1 = new Task("Задача №1","Легкая");
		Epictask task2 = new Epictask("Задача №2","Сложная");
		inMemoryTaskManager.createNewEpictask(task2);
		Subtask task3 = new Subtask("Задача №3","Средняя", task2.getId());
		Task task4 = new Task("Задача №4","Легкая");
		Task task5 = new Task("Задача №5","Легкая");
		Task task6 = new Task("Задача №6","Легкая");
		Task task7 = new Task("Задача №7","Легкая");
		Task task8 = new Task("Задача №8","Легкая");
		Task task9 = new Task("Задача №9","Легкая");
		Epictask task10 = new Epictask("Задача №10","Сложная");
		Task task11 = new Task("Задача №11","Легкая");

		inMemoryTaskManager.createNewTask(task1);
		inMemoryTaskManager.createNewSubtask(task3);
		inMemoryTaskManager.createNewTask(task4);
		inMemoryTaskManager.createNewTask(task5);
		inMemoryTaskManager.createNewTask(task6);
		inMemoryTaskManager.createNewTask(task7);
		inMemoryTaskManager.createNewTask(task8);
		inMemoryTaskManager.createNewTask(task9);
		inMemoryTaskManager.createNewEpictask(task10);
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
		task3.setStatus(Status.IN_PROGRESS);
		inMemoryTaskManager.createNewSubtask(task3);
		System.out.println(historyManager.getHistory());
		System.out.println();

		inMemoryTaskManager.getTaskById(task11.getId());

		historyManager.getHistory();
		System.out.println(historyManager.getHistory());


	}
}
