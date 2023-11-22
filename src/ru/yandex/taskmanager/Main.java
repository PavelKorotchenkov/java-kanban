package ru.yandex.taskmanager;

import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.service.TaskManager;
import ru.yandex.taskmanager.util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Sprint 7
 * author: Pavel Korotchenkov
 * created 27.09.2023
 * upd 22.11.2023
 * ver. 1.5
 */

/**
 * Влад, привет!
 * Дополнительное задание с поиском конфликтов по времени задач через О(1) сделать пока не удалось, ещё подумаю на досуге
 */
public class Main {
	public static void main(String[] args) {
		TaskManager inMemoryTaskManager = Managers.getDefault();

		Task task1 = new Task("Задача №1", "Легкая", LocalDateTime.now(), Duration.ofMinutes(20));
		inMemoryTaskManager.createNewTask(task1);
		Task task2 = new Task("Задача №2", "Легкая", LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(20));
		inMemoryTaskManager.createNewTask(task2);

		Epictask task3 = new Epictask("Эпик Задача №1", "Сложная");
		inMemoryTaskManager.createNewEpictask(task3);
		Subtask task4 = new Subtask("Подзадача №1", "Средняя", LocalDateTime.now().plusMinutes(40), Duration.ofMinutes(10), task3.getId());
		inMemoryTaskManager.createNewSubtask(task4);
		Subtask task5 = new Subtask("Подзадача №2", "Средняя", LocalDateTime.now().plusMinutes(50), Duration.ofMinutes(10), task3.getId());
		inMemoryTaskManager.createNewSubtask(task5);

		List<Task> prioTasks = inMemoryTaskManager.getPrioritizedTasks();
		for (Task prioTask : prioTasks) {
			System.out.println(prioTask);
			System.out.println("Start time: " + prioTask.getStartTime());
			System.out.println("Duration: " + prioTask.getDuration());
		}
	}
}
