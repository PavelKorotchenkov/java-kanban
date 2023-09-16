package ru.yandex.taskmanager.model;

public class Task {
	private String name;
	private String description;
	private int id;
	private Status status;

	public Task(String name, String description) {
		this.name = name;
		this.description = description;
		this.status = Status.NEW;
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
	public String toString() {
		return "ru.yandex.taskmanager.model.Task{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				", id=" + id +
				'}';
	}
}