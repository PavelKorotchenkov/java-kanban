package ru.yandex.taskmanager.model;

public class Subtask extends Task {
	private final int epicId;

	public Subtask(String name, String description, int epicId) {
		super(name, description);
		this.epicId = epicId;
	}

	public int getEpicTaskId() {
		return this.epicId;
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
