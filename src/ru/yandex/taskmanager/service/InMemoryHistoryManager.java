package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.model.Task;

import java.util.LinkedList;
import java.util.List;

//Добавлена константа для размера history
//Реализация history изменена на LinkedList, изменен алгоритм добавления в историю при достижении лимита
public class InMemoryHistoryManager implements HistoryManager{
	private final List<Task> history = new LinkedList<>();
	private final static int HISTORY_CAPACITY = 10;

	@Override
	public List<Task> getHistory() {
		return List.copyOf(history);
	}

	@Override
	public void add(Task task) {
		if (history.size() >= HISTORY_CAPACITY) {
			history.remove(0);
			history.add(task);
			return;
		}

		history.add(task);
	}
}
