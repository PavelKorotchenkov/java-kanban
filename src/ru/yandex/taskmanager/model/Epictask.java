package ru.yandex.taskmanager.model;

import java.util.ArrayList;

public class Epictask extends Task {
	//аналогично подзадаче решил здесь также хранить только айдишники, правда удаление подзадачи стало выглядеть
	//кривовато из-за необходимости приведения ID к Integer, чтобы удалялся именно индекс подзадачи, а не индекс списка
	private ArrayList<Integer> subtasksIds = new ArrayList<>();

	public ArrayList<Integer> getSubtasksIds() {
		return subtasksIds;
	}

	public Epictask(String name, String description) {
		super(name, description);
	}

	//refactor: поменять видимость поля на public
	public void add(int subtaskId) {
		subtasksIds.add(subtaskId);
	}

	//refactor: поменять видимость поля с подзадачами на private, доступ осуществляется через геттер
	public ArrayList<Integer> getSubtasks() {
		return subtasksIds;
	}

	@Override
	public String toString() {
		return "EpicTask: " + getName() + "=\""
				+ getDescription() + "\", status=" + getStatus() + ", id=" + getId()
				+ ", subtasks=" + getSubtasks() +
				'}';
	}
}
