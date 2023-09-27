package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
	private int taskId = 0;
	private final HashMap<Integer, Task> tasks = new HashMap<>();
	private final HashMap<Integer, Epictask> epictasks = new HashMap<>();
	private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
	private List<Task> history = new ArrayList<>();

	public ArrayList<Task> getTasksList() {
		return new ArrayList<>(tasks.values());
	}

	public ArrayList<Epictask> getEpictasksList() {
		return new ArrayList<>(epictasks.values());
	}

	public ArrayList<Subtask> getSubtasksList() {
		return new ArrayList<>(subtasks.values());
	}

	public boolean clearTasks() {
		tasks.clear();
		return true;
	}

	//fix: очистить список подзадач одновременно с эпиками
	public boolean clearEpictasks() {
		subtasks.clear();
		epictasks.clear();
		return true;
	}

	public boolean clearSubtasks() {
		for (Epictask epictask : epictasks.values()) {
			epictask.getSubtasks().clear();
			checkStatus(epictask.getId());
		}

		subtasks.clear();
		return true;
	}


	//feature: добавить сохранение истории при вызове метода
	public Task getTaskById(int taskId) {
		Task task;

		if (tasks.containsKey(taskId)) {
			task = tasks.get(taskId);
			saveHistory(task);
			return task;
		}

		if (epictasks.containsKey(taskId)) {
			task = epictasks.get(taskId);
			saveHistory(task);
			return task;
		}

		if (subtasks.containsKey(taskId)) {
			task = subtasks.get(taskId);
			saveHistory(task);
			return task;
		}

		return null;
	}


	public Task createNewTask(Task task) {
		task.setId(++taskId);
		tasks.put(taskId, task);
		return task;
	}

	public Epictask createNewEpictask(Epictask task) {
		task.setId(++taskId);
		epictasks.put(taskId, task);
		return task;
	}

	public Subtask createNewSubtask(Subtask task) {
		task.setId(++taskId);
		subtasks.put(taskId, task);
		epictasks.get(task.getEpicTaskId()).add(task.getId());
		checkStatus(task.getEpicTaskId());
		return task;
	}

	public boolean updateTask(Task task) {
		if (tasks.containsKey(task.getId())) {
			tasks.get(task.getId()).setName(task.getName());
			tasks.get(task.getId()).setDescription(task.getDescription());
			tasks.get(task.getId()).setStatus(task.getStatus());
			return true;
		}

		return false;
	}

	public boolean updateSubtask(Subtask task) {
		if (subtasks.containsKey(task.getId())) {
			subtasks.get(task.getId()).setName(task.getName());
			subtasks.get(task.getId()).setDescription(task.getDescription());
			subtasks.get(task.getId()).setStatus(task.getStatus());
			checkStatus(task.getEpicTaskId());
			return true;
		}

		return false;
	}

	public boolean updateEpictask(Epictask task) {
		if (epictasks.containsKey(task.getId())) {
			epictasks.get(task.getId()).setName(task.getName());
			epictasks.get(task.getId()).setDescription(task.getDescription());
			return true;
		}

		return false;
	}

	public Task deleteTaskById(int taskId) {
		return tasks.remove(taskId);
	}

	public Epictask deleteEpictaskById(int taskId) {
		Epictask epictask = epictasks.remove(taskId);

		for (Integer id : epictask.getSubtasksIds()) {
			subtasks.remove(id);
		}

		return epictask;
	}

	public Subtask deleteSubtaskById(int taskId) {
		Subtask subtask = subtasks.remove(taskId);
		int epicId = subtask.getEpicTaskId();
		epictasks.get(epicId).getSubtasks().remove((Integer) taskId);
		checkStatus(epicId);

		return subtask;
	}

	public ArrayList<Integer> getSubtasks(int taskId) {
		return new ArrayList<>(epictasks.get(taskId).getSubtasksIds());
	}

	//feature: добавить метод для сохранения истории просмотра задач и получение истории
	@Override
	public List<Task> getHistory() {
		for (Task task : history) {
			System.out.print(task.getId() + " ");
		}
		return history;
	}

	private void saveHistory(Task task) {
		if (history.size() >= 10) {
			List<Task> newHistory = new ArrayList<>();
			for (int i = 1; i < history.size(); i++) {
				newHistory.add(history.get(i));
			}
			newHistory.add(task);
			history = newHistory;
			return;
		}

		history.add(task);
	}

	private void checkStatus(int taskId) {
		int subtasksAmount = getSubtasks(taskId).size();
		int countNew = 0;
		int countDone = 0;
		ArrayList<Integer> subtasksIds = getSubtasks(taskId);

		for (int id : subtasksIds) {
			if (subtasks.get(id).getStatus().equals(Status.NEW)) {
				countNew++;
			} else if (subtasks.get(id).getStatus().equals(Status.DONE)) {
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



