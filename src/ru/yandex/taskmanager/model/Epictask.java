package ru.yandex.taskmanager.model;

import java.util.ArrayList;

public class Epictask extends Task {
	private ArrayList<Integer> subtasksIds = new ArrayList<>();

	public Epictask(String name, String description) {
		super(name, description);
	}

	public ArrayList<Integer> getSubtasksIds() {
		return subtasksIds;
	}

	public void add(int subtaskId) {
		subtasksIds.add(subtaskId);
	}

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
