package ru.yandex.taskmanager.model;

public class Subtask extends Task {
	private final int epicId;
	private final TaskType type = TaskType.SUBTASK;

	public Subtask(String name, String description, int epicId) {
		super(name, description);
		this.epicId = epicId;
	}

	public int getEpicTaskId() {
		return this.epicId;
	}

	public int getEpicId() {
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
