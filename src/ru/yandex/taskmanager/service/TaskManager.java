package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
	List<Task> getTasksList();
	List<Epictask> getEpictasksList();
	List<Subtask> getSubtasksList();

	void clearTasks();
	void clearEpictasks();
	void clearSubtasks();

	Task getTaskById(int taskId);
	Epictask getEpictaskById(int taskId);
	Subtask getSubtaskById(int taskId);

	void createNewTask(Task task);
	void createNewEpictask(Epictask task);
	void createNewSubtask(Subtask task);

	void updateTask(Task task);
	void updateSubtask(Subtask task);
	void updateEpictask(Epictask task);

	void deleteTaskById(int taskId);
	void deleteEpictaskById(int taskId);
	void deleteSubtaskById(int taskId);

	List<Subtask> getSubtasks(int taskId);

	List<Task> getHistory();

	TreeSet<Task> getTaskSortedByStartTime();
}
