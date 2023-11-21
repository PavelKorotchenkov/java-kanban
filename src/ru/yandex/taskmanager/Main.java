package ru.yandex.taskmanager;

import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.service.TaskManager;
import ru.yandex.taskmanager.util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TreeSet;

/**
 * Sprint 7
 * author: Pavel Korotchenkov
 * created 27.09.2023
 * upd 16.11.2023
 * ver. 1.5
 */

public class Main {
	public static void main(String[] args) {
		TaskManager inMemoryTaskManager = Managers.getDefault();

		Task task1 = new Task("Задача №1", "Легкая", LocalDateTime.now().plusMinutes(50), Duration.ofMinutes(20));
		inMemoryTaskManager.createNewTask(task1);
		Task task2 = new Task("Задача №2", "Легкая", LocalDateTime.now().plusMinutes(3), Duration.ofMinutes(20));
		inMemoryTaskManager.createNewTask(task2);

		Epictask task3 = new Epictask("Эпик Задача №1", "Сложная");
		inMemoryTaskManager.createNewEpictask(task3);
		Subtask task4 = new Subtask("Подзадача №1", "Средняя", LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(10), task3.getId());
		inMemoryTaskManager.createNewSubtask(task4);
		Subtask task5 = new Subtask("Подзадача №2", "Средняя", LocalDateTime.now().plusMinutes(100), Duration.ofMinutes(20), task3.getId());
		inMemoryTaskManager.createNewSubtask(task5);
		Subtask task6 = new Subtask("Подзадача №3", "Средняя", LocalDateTime.now(), Duration.ofMinutes(30), task3.getId());
		inMemoryTaskManager.createNewSubtask(task6);

		Task task7 = new Task("Задача №7", "Легкая", LocalDateTime.now().plusMinutes(2), Duration.ofMinutes(20));
		inMemoryTaskManager.createNewTask(task7);

		Subtask task8 = new Subtask("Подзадача №4", "Средняя", LocalDateTime.now().plusMinutes(1), Duration.ofMinutes(10), task3.getId());
		inMemoryTaskManager.createNewSubtask(task8);

		/*System.out.println("EPIC START TIME, END TIME AND DURATION\n------------------");
		System.out.println(task3.getStartTime());
		System.out.println(task3.getEndTime());
		System.out.println(task3.getDuration());

		System.out.println("TIME CHECK ENDS");
		System.out.println();*/

		System.out.println("TESTING START TIME SORT BEGIN");
		TreeSet<Task> testing = inMemoryTaskManager.getTaskSortedByStartTime();
		for (Task task : testing) {
			System.out.println(task.getName() + " ---------- " + task.getStartTime());
		}
		System.out.println("TESTING START TIME SORT END\n");

		/*Epictask task7 = new Epictask("Эпик Задача №2", "Сложная");
		inMemoryTaskManager.createNewEpictask(task7);

		System.out.println(task7.getType());

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
		System.out.println(inMemoryTaskManager.getHistory() + "\n");*/
	}
}
