package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
	ArrayList<Task> getTasksList();
	ArrayList<Epictask> getEpictasksList();
	ArrayList<Subtask> getSubtasksList();

	boolean clearTasks();
	boolean clearEpictasks();
	boolean clearSubtasks();

	Task getTaskById(int taskId);

	Task createNewTask(Task task);
	Epictask createNewEpictask(Epictask task);
	Subtask createNewSubtask(Subtask task);

	boolean updateTask(Task task);
	boolean updateSubtask(Subtask task);
	boolean updateEpictask(Epictask task);

	Task deleteTaskById(int taskId);
	Epictask deleteEpictaskById(int taskId);
	Subtask deleteSubtaskById(int taskId);

	ArrayList<Integer> getSubtasks(int taskId);
}
