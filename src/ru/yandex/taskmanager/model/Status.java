package ru.yandex.taskmanager.model;

public enum Status {
	NEW("Новая задача"),
	IN_PROGRESS("Задача в работе"),
	DONE("Задача завершена");

	private final String title;

	Status(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return title;
	}
}
