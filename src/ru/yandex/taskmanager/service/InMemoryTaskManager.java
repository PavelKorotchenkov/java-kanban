package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.util.Managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
	protected int taskId = 0;
	protected final Map<Integer, Task> tasks = new HashMap<>();
	protected final Map<Integer, Epictask> epictasks = new HashMap<>();
	protected final Map<Integer, Subtask> subtasks = new HashMap<>();
	private final HistoryManager historyManager = Managers.getDefaultHistory();

	@Override
	public List<Task> getTasksList() {
		return List.copyOf(tasks.values());
	}

	@Override
	public List<Epictask> getEpictasksList() {
		return List.copyOf(epictasks.values());
	}

	@Override
	public List<Subtask> getSubtasksList() {
		return List.copyOf(subtasks.values());
	}

	@Override
	public void clearTasks() {
		for (Integer id : tasks.keySet()) {
			historyManager.remove(id);
		}
		tasks.clear();
	}

	@Override
	public void clearEpictasks() {
		for (Integer id : subtasks.keySet()) {
			historyManager.remove(id);
		}

		for (Integer id : epictasks.keySet()) {
			historyManager.remove(id);
		}
		subtasks.clear();
		epictasks.clear();
	}

	@Override
	public void clearSubtasks() {
		for (Integer id : subtasks.keySet()) {
			historyManager.remove(id);
		}

		for (Epictask epictask : epictasks.values()) {
			epictask.getSubtasks().clear();
			epictask.setStatus(Status.NEW);
		}

		subtasks.clear();
	}

	@Override
	public Task getTaskById(int taskId) {
		return getTask(taskId, tasks);
	}

	@Override
	public Epictask getEpictaskById(int taskId) {
		return (Epictask) getTask(taskId, epictasks);
	}

	@Override
	public Subtask getSubtaskById(int taskId) {
		return (Subtask) getTask(taskId, subtasks);
	}

	@Override
	public void createNewTask(Task task) {
		task.setId(++taskId);
		tasks.put(taskId, task);
	}

	@Override
	public void createNewEpictask(Epictask task) {
		task.setId(++taskId);
		epictasks.put(taskId, task);
	}

	@Override
	public void createNewSubtask(Subtask subtask) {
		subtask.setId(++taskId);
		subtasks.put(taskId, subtask);
		epictasks.get(subtask.getEpicTaskId()).addSubtask(subtask);
		checkStatus(subtask.getEpicTaskId());
	}

	@Override
	public void updateTask(Task task) {
		if (tasks.containsKey(task.getId())) {
			tasks.get(task.getId()).setName(task.getName());
			tasks.get(task.getId()).setDescription(task.getDescription());
			tasks.get(task.getId()).setStatus(task.getStatus());
		}
	}

	@Override
	public void updateSubtask(Subtask task) {
		if (subtasks.containsKey(task.getId())) {
			subtasks.get(task.getId()).setName(task.getName());
			subtasks.get(task.getId()).setDescription(task.getDescription());
			subtasks.get(task.getId()).setStatus(task.getStatus());
			checkStatus(task.getEpicTaskId());
		}
	}

	@Override
	public void updateEpictask(Epictask task) {
		if (epictasks.containsKey(task.getId())) {
			epictasks.get(task.getId()).setName(task.getName());
			epictasks.get(task.getId()).setDescription(task.getDescription());
		}
	}

	@Override
	public void deleteTaskById(int taskId) {
		tasks.remove(taskId);
		historyManager.remove(taskId);
	}

	@Override
	public void deleteEpictaskById(int taskId) {
		Epictask epictask = epictasks.remove(taskId);
		for (Subtask subtask : epictask.getSubtasks()) {
			historyManager.remove(subtask.getId());
			subtasks.remove(subtask.getId());
		}
		historyManager.remove(taskId);
	}

	@Override
	public void deleteSubtaskById(int taskId) {
		Subtask subtask = subtasks.remove(taskId);
		int epicId = subtask.getEpicTaskId();
		epictasks.get(epicId).deleteSubtask(taskId);
		checkStatus(epicId);
		historyManager.remove(taskId);
	}

	@Override
	public List<Subtask> getSubtasks(int taskId) {
		return List.copyOf(epictasks.get(taskId).getSubtasks());
	}

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistory();
	}

	protected HistoryManager getHistoryManager() {
		return historyManager;
	}

	protected Task getTask(int taskId, Map<Integer, ? extends Task> taskList) {
		final Task task = taskList.get(taskId);
		if (task != null) {
			historyManager.add(task);
		}
		return task;
	}

	private void checkStatus(int taskId) {
		int subtasksAmount = getSubtasks(taskId).size();
		int countNew = 0;
		int countDone = 0;
		List<Subtask> subtasks = getSubtasks(taskId);

		for (Subtask subtask : subtasks) {
			if (subtask.getStatus().equals(Status.NEW)) {
				countNew++;
			} else if (subtask.getStatus().equals(Status.DONE)) {
				countDone++;
			}
		}

		if (countNew == subtasksAmount) {
			epictasks.get(taskId).setStatus(Status.NEW);
			return;
		}

		if (countDone == subtasksAmount) {
			epictasks.get(taskId).setStatus(Status.DONE);
			return;
		}

		epictasks.get(taskId).setStatus(Status.IN_PROGRESS);
	}
}



