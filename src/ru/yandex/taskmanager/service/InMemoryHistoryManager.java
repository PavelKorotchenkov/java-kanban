package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
	private static List<Task> history = new ArrayList<>();

	@Override
	public List<Task> getHistory() {
		return new ArrayList<>(history);
	}

	@Override
	public void add(Task task) {
		if (history.size() >= 10) {
			List<Task> newHistory = new ArrayList<>();
			for (int i = 1; i < history.size(); i++) {
				newHistory.add(history.get(i));
			}
			newHistory.add(task);
			history = newHistory;
			return;
		}

		history.add(task);
	}
}
