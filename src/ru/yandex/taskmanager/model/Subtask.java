package ru.yandex.taskmanager.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
	private final int epicId;
	private final TaskType type = TaskType.SUBTASK;

	public Subtask(String name, String description, int epicId) {
		super(name, description);
		this.epicId = epicId;
	}

	public Subtask(String name, String description, LocalDateTime startTime, Duration duration, int epicId) {
		super(name, description, startTime, duration);
		this.epicId = epicId;
	}

	public int getEpicTaskId() {
		return this.epicId;
	}

	@Override
	public TaskType getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return "Subtask{"
				+ "name=" + getName()
				+ ", description=" + getDescription()
				+ ", status=" + getStatus()
				+ ", id=" + getId()
				+ '}';
	}
}
