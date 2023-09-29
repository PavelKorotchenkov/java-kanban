package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;
import java.util.List;

public interface TaskManager {
	//refactor: заменить на List
	List<Task> getTasksList();
	List<Epictask> getEpictasksList();
	List<Subtask> getSubtasksList();

	boolean clearTasks();
	boolean clearEpictasks();
	boolean clearSubtasks();

	Task getTaskById(int taskId);
	Task getEpictaskById(int taskId);
	Task getSubtaskById(int taskId);

	Task createNewTask(Task task);
	Epictask createNewEpictask(Epictask task);
	Subtask createNewSubtask(Subtask task);

	boolean updateTask(Task task);
	boolean updateSubtask(Subtask task);
	boolean updateEpictask(Epictask task);

	Task deleteTaskById(int taskId);
	Epictask deleteEpictaskById(int taskId);
	Subtask deleteSubtaskById(int taskId);

	List<Integer> getSubtasks(int taskId);

	//refactor: добавить метод просмотра истории в TaskManager
	List<Task> getHistory();
}
