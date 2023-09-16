package ru.yandex.taskmanager.model;

public class Subtask extends Task {
	//refactor: хранить айди эпика, а не эпик. Всё-таки так проще и надёжнее - меньше возможностей допустить ошибку
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
		return "Subtask{'" + getName() + "'='"
				+ getDescription() + "', status=" + getStatus() + ", id=" + getId()
				+ '}';
	}
}
