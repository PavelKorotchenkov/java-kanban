package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
	HistoryManager historyManager = new InMemoryHistoryManager();

	@Test
	void historyShouldAdd1TaskWhenAdd1Task() {
		Task task = new Task("task", "t1");
		task.setId(1);
		historyManager.add(task);
		final List<Task> history = historyManager.getHistory();
		Assertions.assertNotNull(history);
		Assertions.assertEquals(1, history.size());
	}

	@Test
	void getHistoryShouldReturn1TaskAnd1EpicWhenAdd1TaskAnd1Epic() {
		Task task = new Task("task", "t1");
		task.setId(1);
		historyManager.add(task);
		Epictask epictask = new Epictask("epictask", "e1");
		epictask.setId(2);
		historyManager.add(epictask);

		final List<Task> history = historyManager.getHistory();
		Assertions.assertNotNull(history);
		Assertions.assertEquals(2, history.size());
	}

	@Test
	void getHistoryShouldReturnEmptyListWhenNothingAdded() {
		final List<Task> history = historyManager.getHistory();
		Assertions.assertEquals(0, history.size());
	}

	@Test
	void historyShouldHave1TaskWhenSameTaskAddedMultipleTimes() {
		Task task = new Task("task", "t1");
		task.setId(1);
		historyManager.add(task);
		historyManager.add(task);
		historyManager.add(task);

		final List<Task> history = historyManager.getHistory();
		Assertions.assertNotNull(history);
		Assertions.assertEquals(1, history.size());
	}

	@Test
	void taskShouldBeDeletedFromHistoryAndAddedInTheEndIfWasAlreadyThere() {
		Task task = new Task("task", "t1");
		Task task2 = new Task("task2", "t2");
		task.setId(1);
		task2.setId(2);
		historyManager.add(task);
		historyManager.add(task2);
		historyManager.add(task);

		final List<Task> history = historyManager.getHistory();
		Assertions.assertNotNull(history);
		Assertions.assertArrayEquals(List.of(task2, task).toArray(), history.toArray());
	}

	@Test
	void removeFirstElementInHistoryRemovesFirst() {
		Task task = new Task("task", "t1");
		Task task2 = new Task("task2", "t2");
		Task task3 = new Task("task3", "t3");
		task.setId(1);
		task2.setId(2);
		task2.setId(3);
		historyManager.add(task);
		historyManager.add(task2);
		historyManager.add(task3);
		historyManager.remove(task.getId());
		final List<Task> history = historyManager.getHistory();
		Assertions.assertArrayEquals(List.of(task2, task3).toArray(), history.toArray());
	}

	@Test
	void removeMiddleElementInHistoryRemovesMiddle() {
		Task task = new Task("task", "t1");
		Task task2 = new Task("task2", "t2");
		Task task3 = new Task("task3", "t3");
		task.setId(1);
		task2.setId(2);
		task2.setId(3);
		historyManager.add(task);
		historyManager.add(task2);
		historyManager.add(task3);
		historyManager.remove(task2.getId());
		final List<Task> history = historyManager.getHistory();
		Assertions.assertArrayEquals(List.of(task, task3).toArray(), history.toArray());
	}

	@Test
	void removeLastElementInHistoryRemovesLast() {
		Task task = new Task("task", "t1");
		Task task2 = new Task("task2", "t2");
		Task task3 = new Task("task3", "t3");
		task.setId(1);
		task2.setId(2);
		task2.setId(3);
		historyManager.add(task);
		historyManager.add(task2);
		historyManager.add(task3);
		historyManager.remove(task3.getId());
		final List<Task> history = historyManager.getHistory();
		Assertions.assertArrayEquals(List.of(task, task2).toArray(), history.toArray());
	}
}