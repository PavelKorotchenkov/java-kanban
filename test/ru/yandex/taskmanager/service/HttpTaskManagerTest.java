package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.taskmanager.api.KVServer;
import ru.yandex.taskmanager.exception.StartEndTimeConflictException;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Status;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;
import ru.yandex.taskmanager.util.Managers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
	KVServer server;

	@BeforeEach
	void getManager() throws IOException, InterruptedException {
		server = Managers.getDefaultKVServer();
		server.start();
		super.manager = Managers.getDefault();

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

	@AfterEach
	void stop() {
		server.stop();
	}

	@Test
	void tasksListShouldContainTaskAfterCreated() {
		super.tasksListShouldContainTaskAfterCreated(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Assertions.assertArrayEquals(List.of(httpManager2.getTaskById(1), httpManager2.getTaskById(2)).
				toArray(), httpManager2.getTasksList().toArray());
	}

	@Test
	void afterLoadShouldHaveSameListsSameHistoryAndIdShouldBeMaxIdCreated() {
		manager.getTaskById(1);
		manager.getEpictaskById(3);
		manager.getSubtaskById(5);

		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");

		assertEquals(6, httpManager2.taskId);
		Assertions.assertArrayEquals(manager.getTasksList().toArray(), httpManager2.getTasksList().toArray());
		Assertions.assertArrayEquals(manager.getEpictasksList().toArray(), httpManager2.getEpictasksList().toArray());
		Assertions.assertArrayEquals(manager.getSubtasksList().toArray(), httpManager2.getSubtasksList().toArray());
		Assertions.assertArrayEquals(List.of(manager.getTaskById(1), manager.getEpictaskById(3),
				manager.getSubtaskById(5)).toArray(), manager.getHistory().toArray());
	}

	@Test
	void afterLoadWithNoHistoryShouldHaveSameListsAndEmptyHistory() {
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");

		Assertions.assertArrayEquals(manager.getTasksList().toArray(), httpManager2.getTasksList().toArray());
		Assertions.assertArrayEquals(manager.getEpictasksList().toArray(), httpManager2.getEpictasksList().toArray());
		Assertions.assertArrayEquals(manager.getSubtasksList().toArray(), httpManager2.getSubtasksList().toArray());
		System.out.println(manager.getHistory());
		assertEquals(0, manager.getHistory().size());
	}

	@Test
	void afterLoadEpictaskWithoutSubtasksShouldCreateEpicWithoutSubtasks() {
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(0, httpManager2.getEpictaskById(4).getSubtasks().size());
	}

	@Test
	void afterLoadEpictaskWithSubtasksShouldCreateEpicWithSubtasks() {
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(2, httpManager2.getEpictaskById(3).getSubtasks().size());
	}

	@Test
	void afterLoadEmptyFileShouldHaveEmptyListsAndEmptyHistory() {
		manager.clearTasks();
		manager.clearEpictasks();
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");

		assertEquals(0, httpManager2.getTasksList().size());
		assertEquals(0, httpManager2.getEpictasksList().size());
		assertEquals(0, httpManager2.getSubtasksList().size());
		assertEquals(0, manager.getHistory().size());
	}

	/**
	 * TESTING CLEAR TASKS
	 */

	@Test
	void clearTasksShouldOnlyRemoveTasks() {
		super.clearTasksShouldOnlyRemoveTasks(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(0, httpManager2.getTasksList().size());
		assertEquals(2, httpManager2.getEpictasksList().size());
		assertEquals(2, httpManager2.getSubtasksList().size());
	}

	@Test
	void clearEpictasksShouldRemoveEpictasksAndSubtasks() {
		super.clearEpictasksShouldRemoveEpictasksAndSubtasks(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(0, httpManager2.getEpictasksList().size());
		assertEquals(0, httpManager2.getSubtasksList().size());
		assertEquals(2, httpManager2.getTasksList().size());
	}

	@Test
	void clearSubtasksShouldOnlyRemoveSubtasks() {
		super.clearSubtasksShouldOnlyRemoveSubtasks(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(0, httpManager2.getSubtasksList().size());
		assertEquals(2, httpManager2.getEpictasksList().size());
		assertEquals(2, httpManager2.getTasksList().size());
	}

	/**
	 * TESTING getTaskById
	 */

	@Test
	void getTaskByIdShouldReturnTaskWithThatId() {
		super.getTaskByIdShouldReturnTaskWithThatId(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(1, httpManager2.getTaskById(1).getId());
		assertEquals("Task", httpManager2.getTaskById(1).getName());
	}

	@Test
	void getEpictaskByIdShouldReturnEpictaskWithThatId() {
		super.getEpictaskByIdShouldReturnEpictaskWithThatId(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(3, httpManager2.getEpictaskById(3).getId());
		assertEquals("Epictask", httpManager2.getEpictaskById(3).getName());
	}

	@Test
	void getSubtaskByIdShouldReturnSubtaskWithThatId() {
		super.getSubtaskByIdShouldReturnSubtaskWithThatId(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(5, httpManager2.getSubtaskById(5).getId());
		assertEquals("Subtask", httpManager2.getSubtaskById(5).getName());
	}

	@Test
	void getTaskEpictaskSubtaskByIdShouldReturnNullIfThatListIsEmpty() {
		super.getTaskEpictaskSubtaskByIdShouldReturnNullIfThatListIsEmpty(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		httpManager2.clearSubtasks();
		Assertions.assertNull(httpManager2.getSubtaskById(4));
		httpManager2.clearTasks();
		httpManager2.clearEpictasks();
		Assertions.assertNull(httpManager2.getTaskById(1));
		Assertions.assertNull(httpManager2.getEpictaskById(3));
	}

	/**
	 * TESTING CREATE NEW TASK
	 */

	@Test
	void createNewTaskShouldSetIdAndAddToTasksListAndSaveToFile() {
		super.createNewTaskShouldSetIdAndAddToTasksList(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(1, httpManager2.getTaskById(1).getId());
	}

	@Test
	void createNewEpicTaskShouldSetIdAndAddToEpicTasksListAndSaveToFile() {
		super.createNewEpictaskShouldSetIdAndAddToEpictasksList(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(3, httpManager2.getEpictaskById(3).getId());
	}

	@Test
	void createNewSubTaskShouldSetIdAndAddToEpicTasksListAndSaveToFile() {
		super.createNewSubtaskShouldSetIdAndAddToSubtasksList(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(5, httpManager2.getSubtaskById(5).getId());
	}

	/**
	 * TESTING UPDATE TASK
	 */

	@Test
	void taskShouldHaveNewDescriptionAfterUpdateAndSaveToFile() {
		super.taskShouldHaveNewDescriptionAfterUpdate(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals("after update", httpManager2.getTaskById(1).getDescription());
	}

	@Test
	void epictaskShouldHaveNewDescriptionAfterUpdateAndSaveToFile() {
		super.epictaskShouldHaveNewDescriptionAfterUpdate(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals("after update", httpManager2.getEpictaskById(3).getDescription());
	}

	@Test
	void subtaskShouldHaveNewDescriptionAfterUpdateAndSaveToFile() {
		super.subtaskShouldHaveNewDescriptionAfterUpdate(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals("after update", httpManager2.getSubtaskById(5).getDescription());
	}

	/**
	 * TESTING DELETE TASK
	 */

	@Test
	void taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1AndSaveToFile() {
		super.taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Assertions.assertNull(httpManager2.getTaskById(1));
	}

	@Test
	void epictaskWithId3ShouldBeDeletedAfterDeleteEpictaskWithId3() {
		super.epictaskWithId3ShouldBeDeletedAfterDeleteEpictaskWithId3(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Assertions.assertNull(httpManager2.getEpictaskById(3));
	}

	@Test
	void subtaskWithId5ShouldBeDeletedAfterDeleteSubtaskWithId5() {
		super.subtaskWithId5ShouldBeDeletedAfterDeleteSubtaskWithId5(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Assertions.assertNull(httpManager2.getSubtaskById(5));
	}

	/**
	 * TESTING getSubtasks
	 */

	@Test
	void shouldReturnListOfSubtasks() {
		super.shouldReturnListOfSubtasks(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Collection<Subtask> subtasks = httpManager2.getEpictaskById(3).getSubtasks();

		Assertions.assertArrayEquals(List.of(httpManager2.getSubtaskById(5), httpManager2
				.getSubtaskById(6)).toArray(), subtasks.toArray());
	}

	/**
	 * TESTING EPIC STATUS
	 */

	@Test
	void epicStatusIsNewWhenEpictaskIsCreated() {
		super.epicStatusIsNewWhenEpictaskIsCreated(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(Status.NEW, httpManager2.getEpictaskById(4).getStatus());
	}

	@Test
	void epicStatusIsNewWhenAllSubtasksAreNew() {
		super.epicStatusIsNewWhenAllSubtasksAreNew(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(Status.NEW, httpManager2.getEpictaskById(3).getStatus());
	}

	@Test
	void epicStatusIsDoneWhenAllSubtasksAreDone() {
		super.epicStatusIsDoneWhenAllSubtasksAreDone(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(Status.DONE, httpManager2.getEpictaskById(3).getStatus());
	}

	@Test
	void epicStatusIsInProgressWhenSubtasksAreNewAndDone() {
		super.epicStatusIsInProgressWhenSubtasksAreNewAndDone(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(Status.IN_PROGRESS, httpManager2.getEpictaskById(3).getStatus());
	}

	@Test
	void epicStatusIsInProgressWhenAllSubtasksAreInProgress() {
		super.epicStatusIsInProgressWhenAllSubtasksAreInProgress(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(Status.IN_PROGRESS, httpManager2.getEpictaskById(3).getStatus());
	}

	/**
	 * TESTING trying to get epictask from subtask (getEpicTaskById)
	 */

	@Test
	void subtaskShouldHaveEpicId3() {
		super.subtaskShouldHaveEpicId3(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(3, httpManager2.getSubtaskById(5).getEpicTaskId());
	}

	/**
	 * TESTING HISTORY
	 */

	@Test
	void shouldReturnHistoryAsListOfTasksWithId1Id2Id3Id5() {
		super.shouldReturnHistoryAsListOfTasksWithId1Id2Id3Id5(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		List<Task> expected = new ArrayList<>();

		expected.add(httpManager2.getTaskById(1));
		expected.add(httpManager2.getTaskById(2));
		expected.add(httpManager2.getEpictaskById(3));
		expected.add(httpManager2.getSubtaskById(5));
		Assertions.assertArrayEquals(expected.toArray(), httpManager2.getHistory().toArray());
	}

	@Test
	void getTaskByIdShouldSaveTaskToHistory() {
		Task task = manager.getTaskById(1);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Assertions.assertArrayEquals(List.of(task).toArray(), httpManager2.getHistory().toArray());
	}

	@Test
	void getEpictaskByIdShouldSaveEpictaskToHistory() {
		Epictask epictask = manager.getEpictaskById(3);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Assertions.assertArrayEquals(List.of(epictask).toArray(), httpManager2.getHistory().toArray());
	}

	@Test
	void getSubTaskByIdShouldSaveTaskToHistory() {
		Subtask subtask = manager.getSubtaskById(5);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Object[] listOfSubs = List.of(subtask).toArray();
		Object[] listOfSubsLoaded = httpManager2.getHistory().toArray();
		Assertions.assertArrayEquals(listOfSubs, listOfSubsLoaded);
	}

	@Test
	void taskShouldGoToTheEndOfHistoryAfterGetTaskById() {
		List<Task> expected = new ArrayList<>();
		manager.getTaskById(1);
		expected.add(manager.getTaskById(2));
		expected.add(manager.getTaskById(1));
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Assertions.assertArrayEquals(expected.toArray(), httpManager2.getHistory().toArray());
	}

	/**
	 * TESTING TIME AND DURATION
	 */

	@Test
	void shouldCalculateTaskEndTime() {
		super.shouldCalculateTaskEndTime(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(httpManager2.getTaskById(1).getStartTime().plusMinutes(20), httpManager2.getTaskById(1).getEndTime());
	}

	@Test
	void shouldCalculateEpictaskStartTime() {
		super.shouldCalculateEpictaskStartTime(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(httpManager2.getSubtaskById(5).getStartTime(), httpManager2.getEpictaskById(3).getStartTime());
	}

	@Test
	void shouldCalculateEpictaskEndTime() {
		super.shouldCalculateEpictaskEndTime(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(httpManager2.getSubtaskById(6).getEndTime(), httpManager2.getEpictaskById(3).getEndTime());
	}

	@Test
	void shouldCalculateSubtaskEndTime() {
		super.shouldCalculateSubtaskEndTime(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(httpManager2.getSubtaskById(5).getStartTime().plusMinutes(20),
				httpManager2.getSubtaskById(5).getEndTime());
	}

	/**
	 * TESTING time sorting
	 */

	@Test
	void shouldSortFromEarliestToLatestStartTime() {
		super.shouldSortFromEarliestToLatestStartTime(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		List<Task> testList = httpManager2.getPrioritizedTasks();
		assertEquals(1, testList.get(0).getId());
		assertEquals(2, testList.get(1).getId());
		assertEquals(5, testList.get(2).getId());
		assertEquals(6, testList.get(3).getId());
	}

	@Test
	void testSortByTimeWithNulls() {
		super.testSortByTimeWithNulls(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		List<Task> testList = new ArrayList<>(httpManager2.getPrioritizedTasks());
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

	@Test
	void shouldThrowStartEndTimeConflictExceptionWhenStartTimeConflicts() {
		super.shouldThrowStartEndTimeConflictExceptionWhenStartTimeConflicts(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Task task = httpManager2.getTaskById(2);
		task.setStartTime(LocalDateTime.now().plusMinutes(15));
		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> httpManager2.updateTask(task));

		assertEquals("В это время уже есть другая задача.", exception.getMessage());
	}

	@Test
	void shouldThrowStartEndTimeConflictExceptionWhenEndTimeConflicts() {
		super.shouldThrowStartEndTimeConflictExceptionWhenEndTimeConflicts(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Task task = httpManager2.getTaskById(1);
		task.setDuration(Duration.ofMinutes(60));

		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> httpManager2.updateTask(task));

		assertEquals("В это время уже есть другая задача.", exception.getMessage());
	}

	@Test
	void whenIncludesInTimeExistingTask() {
		super.whenIncludesInTimeExistingTask(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		Task task = new Task("Task3", "id7", LocalDateTime.now().plusMinutes(70), Duration.ofMinutes(40));

		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> httpManager2.createNewTask(task));

		assertEquals("В это время уже есть другая задача.", exception.getMessage());
	}

	@Test
	void whenTaskStartTimeEqualsExistingTaskEndTime() {
		super.whenTaskStartTimeEqualsExistingTaskEndTime(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(httpManager2.getSubtaskById(6).getEndTime(), httpManager2.getTaskById(7).getStartTime());
	}

	@Test
	void whenTaskEndTimeEqualsExistingTaskStartTime(){
		super.whenTaskEndTimeEqualsExistingTaskStartTime(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(httpManager2.getSubtaskById(6).getStartTime().truncatedTo(ChronoUnit.SECONDS),
				httpManager2.getTaskById(7).getEndTime());
	}

	@Test
	void givenTaskDuration_whenChangeDuration_thenPreviousDurationShouldBeFree(){
		super.givenTaskDuration_whenChangeDuration_thenPreviousDurationShouldBeFree(manager);
		HttpTaskManager httpManager2 = HttpTaskManager.load("http://localhost:8078");
		assertEquals(7, httpManager2 .getTaskById(7).getId());
	}

}