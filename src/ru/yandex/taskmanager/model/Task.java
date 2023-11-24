package ru.yandex.taskmanager.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
	private int id;
	private String name;
	private String description;
	private Status status;
	private final TaskType type = TaskType.TASK;

	private LocalDateTime startTime;
	private Duration duration;

	public Task(String name, String description) {
		this.name = name;
		this.description = description;
		this.status = Status.NEW;
	}

	public Task(String name, String description, LocalDateTime startTime, Duration duration) {
		this.name = name;
		this.description = description;
		this.status = Status.NEW;
		this.startTime = startTime;
		this.duration = duration;
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
		return "Task{"
				+ "name=" + name
				+ ", description=" + description
				+ ", status=" + status
				+ ", id=" + id
				+ ", startTime=" + startTime
				+ ", endTime=" + getEndTime()
				+ '}';
	}
}
