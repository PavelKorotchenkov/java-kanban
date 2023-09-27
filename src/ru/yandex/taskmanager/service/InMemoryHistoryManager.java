package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.model.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
	@Override
	public List<Task> getHistory() {
		return null;
	}

	@Override
	public void saveHistory(Task task) {

	}
}
