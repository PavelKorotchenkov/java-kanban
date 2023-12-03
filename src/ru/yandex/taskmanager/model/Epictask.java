package ru.yandex.taskmanager.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class Epictask extends Task {
	private final TaskType type = TaskType.EPIC;
	private final Map<Integer, Subtask> subtasks = new HashMap<>();

	private LocalDateTime endTime;

	public Epictask(String name, String description) {
		super(name, description);
	}

	public Epictask(Epictask epictask) {
		super(epictask);
		endTime = epictask.getEndTime();
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	@Override
	public LocalDateTime getEndTime() {
		return endTime;
	}

	@Override
	public TaskType getType() {
		return this.type;
	}

	public Collection<Subtask> getSubtasks() {
		return subtasks.values();
	}

	public void addSubtask(Subtask subtask) {
		subtasks.put(subtask.getId(), subtask);
	}

	public void deleteSubtask(int subtaskId) {
		subtasks.remove(subtaskId);
	}

	@Override
	public String toString() {
		return "EpicTask{"
				+ "name=" + getName()
				+ ", description=" + getDescription()
				+ ", status=" + getStatus()
				+ ", id=" + getId()
				+ ", subtasks=" + subtasks.keySet() +
				'}';
	}
}
