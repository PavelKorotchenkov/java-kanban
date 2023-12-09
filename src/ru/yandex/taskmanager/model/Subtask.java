package ru.yandex.taskmanager.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
	private int epicId; // final?

	public Subtask() {
		this.type = TaskType.SUBTASK;
	}

	public Subtask(String name, String description, int epicId) {
		super(name, description);
		this.epicId = epicId;
		this.type = TaskType.SUBTASK;
	}

	public Subtask(String name, String description, LocalDateTime startTime, Duration duration, int epicId) {
		super(name, description, startTime, duration);
		this.epicId = epicId;
		this.type = TaskType.SUBTASK;
	}

	public Subtask(Subtask subtask){
		super(subtask);
		this.epicId = subtask.getEpicTaskId();
		this.type = TaskType.SUBTASK;
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
		String stringTask = "Subtask{"
				+ "name=" + name
				+ ", description=" + description
				+ ", status=" + status
				+ ", id=" + id;
		if (startTime != null) {
			stringTask = stringTask
					+ ", startTime=" + startTime
					+ ", endTime=" + getEndTime();
		}

		stringTask = stringTask + '}';

		return stringTask;
	}
}
