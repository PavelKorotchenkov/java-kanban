package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.util.Managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
	private int taskId = 0;
	private final Map<Integer, Task> tasks = new HashMap<>();
	private final Map<Integer, Epictask> epictasks = new HashMap<>();
	private final Map<Integer, Subtask> subtasks = new HashMap<>();
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

	//feat: добавить historyManager.remove(taskId) в случае очистки списков
	@Override
	public boolean clearTasks() {
		for (Integer id : tasks.keySet()) {
			historyManager.remove(id);
		}
		tasks.clear();
		return true;
	}

	@Override
	public boolean clearEpictasks() {
		for (Integer id : subtasks.keySet()) {
			historyManager.remove(id);
		}

		for (Integer id : epictasks.keySet()) {
			historyManager.remove(id);
		}
		subtasks.clear();
		epictasks.clear();
		return true;
	}

	@Override
	public boolean clearSubtasks() {
		for (Integer id : subtasks.keySet()) {
			historyManager.remove(id);
		}

		for (Epictask epictask : epictasks.values()) {
			epictask.getSubtasks().clear();
			checkStatus(epictask.getId());
		}

		subtasks.clear();
		return true;
	}

	private Task getTask(int taskId, Map<Integer, ? extends Task> taskList) {
		final Task task = taskList.get(taskId);
		if (task != null) {
			historyManager.add(task);
		}
		return task;
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
	public Task createNewTask(Task task) {
		task.setId(++taskId);
		tasks.put(taskId, task);
		return task;
	}

	@Override
	public Epictask createNewEpictask(Epictask task) {
		task.setId(++taskId);
		epictasks.put(taskId, task);
		return task;
	}

	@Override
	public Subtask createNewSubtask(Subtask task) {
		task.setId(++taskId);
		subtasks.put(taskId, task);
		epictasks.get(task.getEpicTaskId()).add(task.getId());
		checkStatus(task.getEpicTaskId());
		return task;
	}

	@Override
	public boolean updateTask(Task task) {
		if (tasks.containsKey(task.getId())) {
			tasks.get(task.getId()).setName(task.getName());
			tasks.get(task.getId()).setDescription(task.getDescription());
			tasks.get(task.getId()).setStatus(task.getStatus());
			return true;
		}

		return false;
	}

	@Override
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

	@Override
	public boolean updateEpictask(Epictask task) {
		if (epictasks.containsKey(task.getId())) {
			epictasks.get(task.getId()).setName(task.getName());
			epictasks.get(task.getId()).setDescription(task.getDescription());
			return true;
		}

		return false;
	}

	//feat: во все методы удаления добавлен метод очистки из истории просмотров historyManager.remove(taskId)
	@Override
	public Task deleteTaskById(int taskId) {
		historyManager.remove(taskId);
		return tasks.remove(taskId);
	}

	@Override
	public Epictask deleteEpictaskById(int taskId) {
		Epictask epictask = epictasks.remove(taskId);

		for (Integer id : epictask.getSubtasksIds()) {
			historyManager.remove(id);
			subtasks.remove(id);
		}

		historyManager.remove(taskId);
		return epictask;
	}

	@Override
	public Subtask deleteSubtaskById(int taskId) {
		Subtask subtask = subtasks.remove(taskId);
		int epicId = subtask.getEpicTaskId();
		epictasks.get(epicId).getSubtasks().remove((Integer) taskId);
		checkStatus(epicId);
		historyManager.remove(taskId);
		return subtask;
	}

	//Вопрос - у Егора на вебинаре увидел, что у него в эпиках хранятся сабтаски, а не их id, а как в итоге лучше?
	@Override
	public List<Integer> getSubtasks(int taskId) {
		return List.copyOf(epictasks.get(taskId).getSubtasksIds());
	}

	private void checkStatus(int taskId) {
		int subtasksAmount = getSubtasks(taskId).size();
		int countNew = 0;
		int countDone = 0;
		List<Integer> subtasksIds = getSubtasks(taskId);

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

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistory();
	}
}



