package ru.yandex.taskmanager.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
	protected int id;
	protected String name;
	protected String description;
	protected Status status;
	protected TaskType type;
	protected LocalDateTime startTime;
	protected Duration duration;

	public Task() {
		this.status = Status.NEW;
		this.type = TaskType.TASK;
	}

	public Task(String name, String description) {
		this.name = name;
		this.description = description;
		this.status = Status.NEW;
		this.type = TaskType.TASK;
	}

	public Task(String name, String description, LocalDateTime startTime, Duration duration) {
		this.name = name;
		this.description = description;
		this.status = Status.NEW;
		this.startTime = startTime;
		this.duration = duration;
		this.type = TaskType.TASK;
	}

	public Task(Task task) {
		id = task.getId();
		name = task.getName();
		description = task.getDescription();
		status = task.getStatus();
		startTime = task.getStartTime();
		duration = task.getDuration();
		type = TaskType.TASK;
	}

	public LocalDateTime getEndTime() {
		return this.startTime.plus(this.duration);
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public TaskType getType() {
		return this.type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task task = (Task) o;
		return id == task.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		String stringTask = "Task{"
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
