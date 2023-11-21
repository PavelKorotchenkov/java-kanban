package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.*;
import ru.yandex.taskmanager.exception.StartEndTimeConflictException;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Status;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class TaskManagerTest<T extends TaskManager> {
	TaskManager memoryManager;
	FileBackedTasksManager fileManager;

	/**
	 * PREPARING TESTS
	 */

	@AfterEach
	void clearAllListsAfterTests() {
		memoryManager.clearTasks();
		memoryManager.clearEpictasks();
		memoryManager.clearSubtasks();

		fileManager.clearTasks();
		fileManager.clearEpictasks();
		fileManager.clearSubtasks();
	}

	Task createTask(String name, String description, LocalDateTime start, Duration duration) {
		Task task = new Task(name, description, start, duration);
		memoryManager.createNewTask(task);
		fileManager.createNewTask(task);
		return task;
	}

	Epictask createEpictask(String name, String description) {
		Epictask epictask = new Epictask(name, description);
		memoryManager.createNewEpictask(epictask);
		fileManager.createNewEpictask(epictask);
		return epictask;
	}

	Subtask createSubtask(String name, String description, LocalDateTime start, Duration duration, int epictask) {
		Subtask subtask = new Subtask(name, description, start, duration, epictask);
		memoryManager.createNewSubtask(subtask);
		fileManager.createNewSubtask(subtask);
		return subtask;
	}

	/**
	 * Thread.sleep() использован для того, чтобы гарантировать разное startTime у задач,
	 * у меня иногда задачи создавались в одну и ту же милисекунду, из-за чего не проходили тесты на сортировку по времени
	 */

	void sleep() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@BeforeEach
	void creatingTasks() {
		memoryManager = new InMemoryTaskManager();
		fileManager = new FileBackedTasksManager("./resources/test.csv");
		createTask("Task", "id1", LocalDateTime.now(), Duration.ofMinutes(20));
		sleep();
		createTask("Task2", "id2", LocalDateTime.now().plusMinutes(45), Duration.ofMinutes(20));
		sleep();
		Epictask epic = createEpictask("Epictask", "id3");
		createEpictask("Epictask2", "id4");
		createSubtask("Subtask", "id5", LocalDateTime.now().plusMinutes(80), Duration.ofMinutes(20), epic.getId());
		sleep();
		createSubtask("Subtask2", "id6", LocalDateTime.now().plusMinutes(120), Duration.ofMinutes(20), epic.getId());
	}

	/**
	 * TESTING GET TASKS LIST
	 */

	void tasksListShouldContainTaskAfterCreated(TaskManager manager) {
		Assertions.assertArrayEquals(List.of(manager.getTaskById(1), manager.getTaskById(2)).
				toArray(), manager.getTasksList().toArray());
	}

	void epictasksListShouldContainEpicTaskAfterCreated(TaskManager manager) {
		Assertions.assertArrayEquals(List.of(manager.getEpictaskById(3), manager.getEpictaskById(4)).
				toArray(), manager.getEpictasksList().toArray());
	}

	void subtasksListShouldContainSubTaskAfterCreated(TaskManager manager) {
		Assertions.assertArrayEquals(List.of(manager.getSubtaskById(5), manager.getSubtaskById(6)).
				toArray(), manager.getSubtasksList().toArray());
	}

	void tasksListsAreEmptyShouldReturnEmptyLists(TaskManager manager) {
		manager.clearTasks();
		manager.clearEpictasks();
		assertEquals(0, manager.getTasksList().size());
		assertEquals(0, manager.getEpictasksList().size());
		assertEquals(0, manager.getSubtasksList().size());
	}

	/**
	 * TESTING CLEAR TASKS
	 */

	void clearTasksShouldOnlyRemoveTasks(TaskManager manager) {
		manager.clearTasks();
		assertEquals(0, manager.getTasksList().size());
		assertEquals(2, manager.getEpictasksList().size());
		assertEquals(2, manager.getSubtasksList().size());
	}

	void clearEpictasksShouldRemoveEpictasksAndSubtasks(TaskManager manager) {
		manager.clearEpictasks();
		assertEquals(0, manager.getEpictasksList().size());
		assertEquals(0, manager.getSubtasksList().size());
		assertEquals(2, manager.getTasksList().size());
	}

	void clearSubtasksShouldOnlyRemoveSubtasks(TaskManager manager) {
		manager.clearSubtasks();
		assertEquals(0, manager.getSubtasksList().size());
		assertEquals(2, manager.getEpictasksList().size());
		assertEquals(2, manager.getTasksList().size());
	}

	/**
	 * TESTING getTaskById
	 */

	void getTaskByIdShouldReturnTaskWithThatId(TaskManager manager) {
		assertEquals(1, manager.getTaskById(1).getId());
		assertEquals("Task", manager.getTaskById(1).getName());
	}

	void getEpictaskByIdShouldReturnEpictaskWithThatId(TaskManager manager) {
		assertEquals(3, manager.getEpictaskById(3).getId());
		assertEquals("Epictask", manager.getEpictaskById(3).getName());

	}

	void getSubtaskByIdShouldReturnSubtaskWithThatId(TaskManager manager) {
		assertEquals(5, manager.getSubtaskById(5).getId());
		assertEquals("Subtask", manager.getSubtaskById(5).getName());

	}

	void getTaskEpictaskSubtaskByIdShouldReturnNullIfThatListIsEmpty(TaskManager manager) {
		manager.clearSubtasks();
		Assertions.assertNull(manager.getSubtaskById(4));
		manager.clearTasks();
		manager.clearEpictasks();
		Assertions.assertNull(manager.getTaskById(1));
		Assertions.assertNull(manager.getEpictaskById(3));
	}

	/**
	 * TESTING CREATE NEW TASK
	 */

	void createNewTaskShouldSetIdAndAddToTasksList(TaskManager manager) {
		assertEquals(1, manager.getTaskById(1).getId());
		assertEquals(2, manager.getTaskById(2).getId());
	}

	void createNewEpictaskShouldSetIdAndAddToEpictasksList(TaskManager manager) {
		assertEquals(3, manager.getEpictaskById(3).getId());
		assertEquals(4, manager.getEpictaskById(4).getId());
	}

	void createNewSubtaskShouldSetIdAndAddToSubtasksList(TaskManager manager) {
		assertEquals(5, manager.getSubtaskById(5).getId());
		assertEquals(6, manager.getSubtaskById(6).getId());
	}

	/**
	 * TESTING UPDATE TASK
	 */

	void taskShouldHaveNewDescriptionAfterUpdate(TaskManager manager) {
		manager.getTaskById(1).setDescription("after update");
		manager.updateTask(manager.getTaskById(1));
		assertEquals("after update", manager.getTaskById(1).getDescription());
	}

	void epictaskShouldHaveNewDescriptionAfterUpdate(TaskManager manager) {
		manager.getEpictaskById(3).setDescription("after update");
		manager.updateEpictask(manager.getEpictaskById(3));
		assertEquals("after update", manager.getEpictaskById(3).getDescription());
	}

	void subtaskShouldHaveNewDescriptionAfterUpdate(TaskManager manager) {
		manager.getSubtaskById(5).setDescription("after update");
		manager.updateSubtask(manager.getSubtaskById(5));
		assertEquals("after update", manager.getSubtaskById(5).getDescription());
	}

	/**
	 * TESTING DELETE TASK
	 */

	void taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1(TaskManager manager) {
		manager.deleteTaskById(1);
		Assertions.assertArrayEquals(List.of(manager.getTaskById(2)).toArray(), manager.getTasksList().toArray());
		Assertions.assertNull(manager.getTaskById(1));
	}

	void epictaskWithId3ShouldBeDeletedAfterDeleteEpictaskWithId3(TaskManager manager) {
		manager.deleteEpictaskById(3);
		Assertions.assertArrayEquals(List.of(manager.getEpictaskById(4)).toArray(), manager.getEpictasksList()
				.toArray());
		Assertions.assertNull(manager.getEpictaskById(3));
	}

	void subtaskWithId5ShouldBeDeletedAfterDeleteSubtaskWithId5(TaskManager manager) {
		manager.deleteSubtaskById(5);
		Assertions.assertArrayEquals(List.of(manager.getSubtaskById(6)).toArray(), manager.getSubtasksList()
				.toArray());
		Assertions.assertNull(manager.getSubtaskById(5));
	}

	/**
	 * TESTING getSubtasks
	 */

	void shouldReturnListOfSubtasks(TaskManager manager) {
		Collection<Subtask> subtasks = manager.getEpictaskById(3).getSubtasks();
		Assertions.assertArrayEquals(List.of(manager.getSubtaskById(5), manager.getSubtaskById(6))
				.toArray(), subtasks.toArray());
	}

	/**
	 * TESTING EPIC STATUS
	 */

	void epicStatusIsNewWhenEpictaskIsCreated(TaskManager manager) {
		assertEquals(Status.NEW, manager.getEpictaskById(4).getStatus());
	}

	void epicStatusIsNewWhenAllSubtasksAreNew(TaskManager manager) {
		assertEquals(Status.NEW, manager.getEpictaskById(3).getStatus());
	}

	void epicStatusIsDoneWhenAllSubtasksAreDone(TaskManager manager) {
		manager.getSubtaskById(5).setStatus(Status.DONE);
		manager.getSubtaskById(6).setStatus(Status.DONE);
		manager.updateSubtask(manager.getSubtaskById(5));
		manager.updateSubtask(manager.getSubtaskById(6));
		assertEquals(Status.DONE, manager.getEpictaskById(3).getStatus());
	}

	void epicStatusIsInProgressWhenSubtasksAreNewAndDone(TaskManager manager) {
		manager.getSubtaskById(6).setStatus(Status.DONE);
		manager.updateSubtask(manager.getSubtaskById(6));
		assertEquals(Status.IN_PROGRESS, manager.getEpictaskById(3).getStatus());
	}

	void epicStatusIsInProgressWhenAllSubtasksAreInProgress(TaskManager manager) {
		manager.getSubtaskById(5).setStatus(Status.IN_PROGRESS);
		manager.getSubtaskById(6).setStatus(Status.IN_PROGRESS);
		manager.updateSubtask(manager.getSubtaskById(5));
		manager.updateSubtask(manager.getSubtaskById(6));
		assertEquals(Status.IN_PROGRESS, manager.getEpictaskById(3).getStatus());
	}

	/**
	 * TESTING trying to get epictask from subtask (getEpicTaskById method)
	 */

	void subtaskShouldHaveEpicId3(TaskManager manager) {
		assertEquals(3, manager.getSubtaskById(5).getEpicTaskId());
	}

	/**
	 * TESTING HISTORY
	 */

	void getTaskByIdShouldSaveTaskToHistory(TaskManager manager) {
		assertEquals(manager.getTaskById(1), manager.getHistory().get(0));
	}

	void getEpictaskByIdShouldSaveEpictaskToHistory(TaskManager manager) {
		assertEquals(manager.getEpictaskById(3), manager.getHistory().get(0));
	}

	void getSubTaskByIdShouldSaveTaskToHistory(TaskManager manager) {
		assertEquals(manager.getSubtaskById(5), manager.getHistory().get(0));
	}

	void shouldReturnHistoryAsListOfTasksWithId1Id2Id3Id5(TaskManager manager) {
		List<Task> expected = new ArrayList<>();
		expected.add(manager.getTaskById(1));
		expected.add(manager.getTaskById(2));
		expected.add(manager.getEpictaskById(3));
		expected.add(manager.getSubtaskById(5));
		Assertions.assertArrayEquals(expected.toArray(), manager.getHistory().toArray());
	}

	void taskShouldGoToTheEndOfHistoryAfterGetTaskById(TaskManager manager) {
		List<Task> expected = new ArrayList<>();
		manager.getTaskById(1);
		expected.add(manager.getTaskById(2));
		expected.add(manager.getTaskById(1));
		Assertions.assertArrayEquals(expected.toArray(), manager.getHistory().toArray());
	}

	/**
	 * TESTING TIME AND DURATION
	 */

	void shouldCalculateTaskEndTime(TaskManager manager) {
		LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofMinutes(20));
		assertEquals(endTime.getMinute(), manager.getTaskById(1).getEndTime().getMinute());
	}

	void shouldCalculateEpictaskEndTime(TaskManager manager) {
		Subtask subtask = new Subtask("Subtask", "for time check", LocalDateTime.now().plusMinutes(200), Duration.ofMinutes(20), manager.getEpictaskById(3).getId());
		manager.createNewSubtask(subtask);
		LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofMinutes(220));
		assertEquals(endTime.getMinute(), manager.getEpictaskById(3).getEndTime().getMinute());
	}

	void shouldCalculateSubtaskEndTime(TaskManager manager) {
		LocalDateTime endTime = LocalDateTime.now().plusMinutes(100);
		assertEquals(endTime.getMinute(), manager.getSubtaskById(5).getEndTime().getMinute());
	}

	/**
	 * TESTING getStartTimeSort
	 */

	void shouldSortFromEarliestToLatestStartTime(TaskManager manager) {
		List<Task> testList = new ArrayList<>(manager.getPrioritizedTasks());

		assertEquals(1, testList.get(0).getId());
		assertEquals(2, testList.get(1).getId());
		assertEquals(5, testList.get(2).getId());
		assertEquals(6, testList.get(3).getId());
	}

	void testSortByTimeWithNulls(TaskManager manager) {
		Task taskTime1 = new Task("test task 1", "time = null");
		manager.createNewTask(taskTime1);
		Task taskTime2 = new Task("test task 2", "time = +3 days", LocalDateTime.now().plusDays(3), Duration.ofMinutes(20));
		manager.createNewTask(taskTime2);
		List<Task> testList = new ArrayList<>(manager.getPrioritizedTasks());

		assertEquals(1, testList.get(0).getId());
		assertEquals(2, testList.get(1).getId());
		assertEquals(5, testList.get(2).getId());
		assertEquals(6, testList.get(3).getId());
		assertEquals(8, testList.get(4).getId());
		assertEquals(7, testList.get(5).getId());
	}

	/**
	 * TESTING TIME CONFLICT
	 */

	void shouldThrowStartEndTimeConflictExceptionWhenStartTimeConflicts(TaskManager manager) {
		Task task = manager.getTaskById(2);
		task.setStartTime(LocalDateTime.now().plusMinutes(15));

		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> manager.updateTask(task));

		assertEquals("Время начала задачи конфликтует с временем выполнения уже существующей задачи", exception.getMessage());
	}

	void shouldThrowStartEndTimeConflictExceptionWhenEndTimeConflicts(TaskManager manager) {
		Task task = manager.getTaskById(1);
		task.setDuration(Duration.ofMinutes(60));

		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> manager.updateTask(task));

		assertEquals("Время окончания задачи конфликтует с временем выполнения уже существующей задачи", exception.getMessage());
	}
	void whenIncludesInTimeExistingTask(TaskManager manager) {
		Task task = new Task("Task3", "id7", LocalDateTime.now().plusMinutes(70), Duration.ofMinutes(40));
		task.setDuration(Duration.ofMinutes(60));

		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> manager.createNewTask(task));

		assertEquals("В это время уже есть другая задача", exception.getMessage());
	}
}