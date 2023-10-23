package ru.yandex.taskmanager.model;

import java.util.*;

public class Epictask extends Task {
	private final Map<Integer, Subtask> subtasks = new HashMap<>();

	public Epictask(String name, String description) {
		super(name, description);
	}

	public Collection<Subtask> getSubtasks() {
		return subtasks.values();
	}

	public void addSubtask(Subtask subtask) {
		subtasks.put(subtask.getId(), subtask);
	}

	public void deleteSubtask(int id) {
		subtasks.remove(id);
	}

	@Override
	public String toString() {
		return "EpicTask{"
				+ "name=" + getName()
				+ ", description=" + getDescription()
				+ ", status=" + getStatus()
				+ ", id=" + getId()
				+ ", subtasks=" + subtasks.keySet() +
				'}';
	}
}
