package ru.yandex.taskmanager.model;

public class Subtask extends Task {
	private Epictask epictask;

	public Subtask(String name, String description, Epictask epictask) {
		super(name, description);
		this.epictask = epictask;
		epictask.add(this);
	}

	public Epictask getEpicTask() {
		return epictask;
	}

	@Override
	public String toString() {
		return "ru.yandex.taskmanager.model.Subtask{'" + getName() + "'='"
				+ getDescription() + "', status=" + getStatus() + ", id=" + getId()
				+ '}';
	}
}
