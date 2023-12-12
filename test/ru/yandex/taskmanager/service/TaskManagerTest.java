package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.*;
import ru.yandex.taskmanager.exception.StartEndTimeConflictException;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Status;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class TaskManagerTest<T extends TaskManager> {
	T manager;

	/**
	 * PREPARING TESTS
	 */

	Task createTask(String name, String description, LocalDateTime start, Duration duration) {
		Task task = new Task(name, description, start, duration);
		manager.createNewTask(task);

		return task;
	}

	Epictask createEpictask(String name, String description) {
		Epictask epictask = new Epictask(name, description);
		manager.createNewEpictask(epictask);
		return epictask;
	}

	Subtask createSubtask(String name, String description, LocalDateTime start, Duration duration, int epictask) {
		Subtask subtask = new Subtask(name, description, start, duration, epictask);
		manager.createNewSubtask(subtask);
		return subtask;
	}

	void sleep() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		Task updTask = new Task(manager.getTaskById(1));
		updTask.setDescription("after update");
		manager.updateTask(updTask);
		assertEquals("after update", manager.getTaskById(1).getDescription());
	}

	void epictaskShouldHaveNewDescriptionAfterUpdate(TaskManager manager) {
		Epictask updTask = new Epictask(manager.getEpictaskById(3));
		updTask.setDescription("after update");
		manager.updateEpictask(updTask);
		assertEquals("after update", manager.getEpictaskById(3).getDescription());
	}

	void subtaskShouldHaveNewDescriptionAfterUpdate(TaskManager manager) {
		Subtask updTask = new Subtask(manager.getSubtaskById(5));
		updTask.setDescription("after update");
		manager.updateSubtask(updTask);
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
	 * TESTING getting epictask from subtask (getEpicTaskById)
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
		assertEquals(manager.getTaskById(1).getStartTime().plusMinutes(20), manager.getTaskById(1).getEndTime());
	}

	void shouldCalculateEpictaskStartTime(TaskManager manager) {
		assertEquals(manager.getSubtaskById(5).getStartTime(), manager.getEpictaskById(3).getStartTime());
	}

	void shouldCalculateEpictaskEndTime(TaskManager manager) {
		assertEquals(manager.getSubtaskById(6).getEndTime(), manager.getEpictaskById(3).getEndTime());
	}

	void shouldCalculateSubtaskEndTime(TaskManager manager) {
		assertEquals(manager.getSubtaskById(5).getStartTime().plusMinutes(20),
				manager.getSubtaskById(5).getEndTime());
	}

	/**
	 * TESTING time sorting
	 */

	void shouldSortFromEarliestToLatestStartTime(TaskManager manager) {
		List<Task> testList = manager.getPrioritizedTasks();

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
		Task updTask = new Task(manager.getTaskById(2));
		updTask.setStartTime(LocalDateTime.now().plusMinutes(15));

		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> manager.updateTask(updTask));

		assertEquals("В это время уже есть другая задача.", exception.getMessage());
	}

	void shouldThrowStartEndTimeConflictExceptionWhenEndTimeConflicts(TaskManager manager) {
		Task task = manager.getTaskById(1);
		task.setDuration(Duration.ofMinutes(60));

		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> manager.updateTask(task));

		assertEquals("В это время уже есть другая задача.", exception.getMessage());
	}

	void whenIncludesInTimeExistingTask(TaskManager manager) {
		Task task = new Task("Task3", "id7", LocalDateTime.now().plusMinutes(70), Duration.ofMinutes(40));

		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> manager.createNewTask(task));

		assertEquals("В это время уже есть другая задача.", exception.getMessage());
	}

	void whenTaskStartTimeEqualsExistingTaskEndTime(TaskManager manager) {
		Task task = new Task("Task3", "id7", manager.getSubtaskById(6).getEndTime(), Duration.ofMinutes(20));
		manager.createNewTask(task);
		assertEquals(manager.getSubtaskById(6).getEndTime(), manager.getTaskById(7).getStartTime());
	}

	void whenTaskEndTimeEqualsExistingTaskStartTime(TaskManager manager) {
		Task task = new Task("Task3", "id7", manager.getSubtaskById(5).getEndTime()
				.plusMinutes(10).truncatedTo(ChronoUnit.SECONDS), Duration.ofMinutes(10));
		manager.createNewTask(task);
		assertEquals(manager.getSubtaskById(6).getStartTime().truncatedTo(ChronoUnit.SECONDS),
				manager.getTaskById(7).getEndTime());
	}

	void givenTaskDuration_whenChangeDuration_thenPreviousDurationShouldBeFree(TaskManager manager) {
		Task existingTask = manager.getTaskById(2);
		existingTask.setStartTime(LocalDateTime.now().plusMinutes(60));
		existingTask.setDuration(Duration.ofMinutes(10));
		Task newTask = createTask("newTask", "id7", LocalDateTime.now().plusMinutes(40), Duration.ofMinutes(15));

		assertEquals(7, manager.getTaskById(7).getId());
	}
}

// |____t1____|_________|____t2____|____________|____st5____|__________|____st6____|
// |~~~~~~~~~~|_________|~~~~~~~~~~|____________|~~~~~~~~~~~|__________|~~~~~~~~~~~|
// 0_________20_________45_________65___________80_________100________120_________140
