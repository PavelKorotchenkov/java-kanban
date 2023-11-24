package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.exception.StartEndTimeConflictException;
import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.util.Managers;

import java.time.*;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
	protected int taskId = 0;
	protected final Map<Integer, Task> tasks = new HashMap<>();
	protected final Map<Integer, Epictask> epictasks = new HashMap<>();
	protected final Map<Integer, Subtask> subtasks = new HashMap<>();
	private final HistoryManager historyManager = Managers.getDefaultHistory();

	protected final TreeSet<Task> tasksSortedByStartTime = new TreeSet<>((Comparator.comparing(Task::getStartTime,
			Comparator.nullsLast(Comparator.naturalOrder()))));

	protected HistoryManager getHistoryManager() {
		return historyManager;
	}

	public List<Task> getPrioritizedTasks() {
		return List.copyOf(tasksSortedByStartTime);
	}

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistory();
	}

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
		tasksSortedByStartTime.removeIf(task -> task.getType().equals(TaskType.TASK));

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
		tasksSortedByStartTime.removeIf(task -> task.getType().equals(TaskType.SUBTASK));

		subtasks.clear();
	}

	@Override
	public Task getTaskById(int taskId) {
		return getTask(taskId, tasks);
	}

	@Override
	public Epictask getEpictaskById(int epictaskId) {
		return (Epictask) getTask(epictaskId, epictasks);
	}

	@Override
	public Subtask getSubtaskById(int subtaskId) {
		return (Subtask) getTask(subtaskId, subtasks);
	}

	@Override
	public void createNewTask(Task task) {
		task.setId(++taskId);
		tasks.put(taskId, task);

		timeValidation(task);
		tasksSortedByStartTime.add(task);
	}

	@Override
	public void createNewEpictask(Epictask epictask) {
		epictask.setId(++taskId);
		epictasks.put(taskId, epictask);
	}

	@Override
	public void createNewSubtask(Subtask subtask) {
		subtask.setId(++taskId);
		subtasks.put(taskId, subtask);
		final int epicId = subtask.getEpicTaskId();
		epictasks.get(epicId).addSubtask(subtask);

		timeValidation(subtask);
		tasksSortedByStartTime.add(subtask);

		checkStatus(epicId);
		calculateEpicStartEndTime(epicId);
	}

	@Override
	public void updateTask(Task task) {
		if (tasks.containsKey(task.getId())) {
			tasks.get(task.getId()).setName(task.getName());
			tasks.get(task.getId()).setDescription(task.getDescription());
			tasks.get(task.getId()).setStatus(task.getStatus());
			tasks.get(task.getId()).setStartTime(task.getStartTime());
			tasks.get(task.getId()).setDuration(task.getDuration());

			tasksSortedByStartTime.remove(task);
			timeValidation(task);
			tasksSortedByStartTime.add(task);
		}
	}

	@Override
	public void updateSubtask(Subtask task) {
		if (subtasks.containsKey(task.getId())) {
			subtasks.get(task.getId()).setName(task.getName());
			subtasks.get(task.getId()).setDescription(task.getDescription());
			subtasks.get(task.getId()).setStatus(task.getStatus());
			subtasks.get(task.getId()).setStartTime(task.getStartTime());
			subtasks.get(task.getId()).setDuration(task.getDuration());

			tasksSortedByStartTime.remove(task);
			timeValidation(task);
			tasksSortedByStartTime.add(task);

			checkStatus(task.getEpicTaskId());
			calculateEpicStartEndTime(task.getEpicTaskId());
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
		Task task = tasks.remove(taskId);
		historyManager.remove(taskId);
		tasksSortedByStartTime.remove(task);
	}

	@Override
	public void deleteEpictaskById(int epictaskId) {
		Epictask epictask = epictasks.remove(epictaskId);
		for (Subtask subtask : epictask.getSubtasks()) {
			historyManager.remove(subtask.getId());
			subtasks.remove(subtask.getId());
		}
		historyManager.remove(epictaskId);
	}

	@Override
	public void deleteSubtaskById(int taskId) {
		Subtask subtask = subtasks.remove(taskId);
		int epicId = subtask.getEpicTaskId();
		epictasks.get(epicId).deleteSubtask(taskId);
		checkStatus(epicId);
		calculateEpicStartEndTime(subtask.getEpicTaskId());
		historyManager.remove(taskId);
		tasksSortedByStartTime.remove(subtask);
	}

	@Override
	public List<Subtask> getSubtasks(int taskId) {
		return List.copyOf(epictasks.get(taskId).getSubtasks());
	}

	protected Task getTask(int taskId, Map<Integer, ? extends Task> taskList) {
		final Task task = taskList.get(taskId);
		if (task != null) {
			historyManager.add(task);
		}
		return task;
	}

	private void checkStatus(int epicId) {
		int subtasksAmount = getSubtasks(epicId).size();
		int countNew = 0;
		int countDone = 0;
		List<Subtask> subtasks = getSubtasks(epicId);

		for (Subtask subtask : subtasks) {
			if (subtask.getStatus().equals(Status.NEW)) {
				countNew++;
			} else if (subtask.getStatus().equals(Status.DONE)) {
				countDone++;
			}
		}

		if (countNew == subtasksAmount) {
			epictasks.get(epicId).setStatus(Status.NEW);
			return;
		}

		if (countDone == subtasksAmount) {
			epictasks.get(epicId).setStatus(Status.DONE);
			return;
		}

		epictasks.get(epicId).setStatus(Status.IN_PROGRESS);
	}

	private void calculateEpicStartEndTime(int epicId) {
		List<Subtask> subtasks = getSubtasks(epicId);

		LocalDateTime startTime = subtasks.get(0).getStartTime();
		LocalDateTime endTime = subtasks.get(0).getEndTime();
		for (Subtask subtask : subtasks) {
			if (subtask.getStartTime().isBefore(startTime)) {
				startTime = subtask.getStartTime();
			}

			if (subtask.getEndTime().isAfter(endTime)) {
				endTime = subtask.getEndTime();
			}
		}

		epictasks.get(epicId).setStartTime(startTime);
		epictasks.get(epicId).setEndTime(endTime);
		epictasks.get(epicId).setDuration(Duration.between(startTime, endTime));
	}

	private void timeValidation(Task task) {
		if (task.getStartTime() == null) {
			return;
		}
		LocalDateTime taskEndTime = task.getEndTime();
		LocalDateTime taskStartTime = task.getStartTime();
		for (Task anotherTask : tasksSortedByStartTime) {
			if (anotherTask.getStartTime() == null) {
				continue;
			}
			LocalDateTime anotherTaskEndTime = anotherTask.getEndTime();
			LocalDateTime anotherTaskStartTime = anotherTask.getStartTime();
			/**
			 * Влад, привет! Спасбио за ревью)
			 * Второй раз случайно с телефона отправил, пытался код посмотреть с комментариями, сорри, там баг какой-то - не видно ни кода, ни комментов
			 * По поводу реализации проверки, второй continue сама IDEA подсказывает убрать, вернул.
			 * А насчет break - может я что-то упускаю, но я не понимаю, зачем в первом условии continue. У нас же список отсортирован по старту задач,
			 * т.е. если мы нашли задачу, которая начинается позже, чем заканчивается новая задача, понятно, что
			 * у всех остальных по умолчанию старт тайм будет позже.
			 * (Например, есть список задач: 10:00-11:00, 11:00-12:00, 12:30-13:30. Если мы попытаемся создать задачу, 9:00-9:30,
			 * достаточно будет сверить ее с первой задачей в списке, чтобы понять, что она удовлетворяет условию,
			 * т.к. все остальные задачи начинаются позже первой, а значит, гарантированно позже окончания нашей новой задачи.
			 * Так что можно, конечно, использовать continue, но в данном случае это кажется излишним.
			 * */
			if (taskEndTime.isBefore(anotherTaskStartTime) || taskEndTime.equals(anotherTaskStartTime)) {
				continue;
			} else if (anotherTaskEndTime.isBefore(taskStartTime) || anotherTaskEndTime.equals(taskStartTime)) {
				continue;
			} else {
				throw new StartEndTimeConflictException("В это время уже есть другая задача.");
			}

			/*if (taskEndTime.isBefore(anotherTaskStartTime) || taskEndTime.equals(anotherTaskStartTime)) {
				break;
			} else if (anotherTaskEndTime.isBefore(taskStartTime) || anotherTaskEndTime.equals(taskStartTime)) {
				continue;
			} else {
				throw new StartEndTimeConflictException("В это время уже есть другая задача.");
			}*/
		}
	}
}



