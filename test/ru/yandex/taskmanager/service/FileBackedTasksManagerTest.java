package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.taskmanager.exception.StartEndTimeConflictException;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Status;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
	File file = new File("./resources/test.csv");

	/**
	 * TESTING GET TASKS LIST
	 */

	@Test
	void tasksListShouldContainTaskAfterCreated() {
		super.tasksListShouldContainTaskAfterCreated(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertArrayEquals(List.of(fileManager2.getTaskById(1), fileManager2.getTaskById(2)).
				toArray(), fileManager2.getTasksList().toArray());
	}

	@Test
	void afterLoadShouldHaveSameListsSameHistoryAndIdShouldBeMaxIdCreated() {
		fileManager.getTaskById(1);
		fileManager.getEpictaskById(3);
		fileManager.getSubtaskById(5);

		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);

		Assertions.assertEquals(6, fileManager2.taskId);
		Assertions.assertArrayEquals(fileManager.getTasksList().toArray(), fileManager2.getTasksList().toArray());
		Assertions.assertArrayEquals(fileManager.getEpictasksList().toArray(), fileManager2.getEpictasksList().toArray());
		Assertions.assertArrayEquals(fileManager.getSubtasksList().toArray(), fileManager2.getSubtasksList().toArray());
		Assertions.assertArrayEquals(List.of(fileManager.getTaskById(1), fileManager.getEpictaskById(3),
				fileManager.getSubtaskById(5)).toArray(), fileManager.getHistory().toArray());
	}

	@Test
	void afterLoadWithNoHistoryShouldHaveSameListsAndEmptyHistory() {
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);

		Assertions.assertArrayEquals(fileManager.getTasksList().toArray(), fileManager2.getTasksList().toArray());
		Assertions.assertArrayEquals(fileManager.getEpictasksList().toArray(), fileManager2.getEpictasksList().toArray());
		Assertions.assertArrayEquals(fileManager.getSubtasksList().toArray(), fileManager2.getSubtasksList().toArray());
		System.out.println(fileManager.getHistory());
		Assertions.assertEquals(0, fileManager.getHistory().size());
	}

	@Test
	void afterLoadEpictaskWithoutSubtasksShouldCreateEpicWithoutSubtasks() {
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(0, fileManager2.getEpictaskById(4).getSubtasks().size());
	}

	@Test
	void afterLoadEpictaskWithSubtasksShouldCreateEpicWithSubtasks() {
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(2, fileManager2.getEpictaskById(3).getSubtasks().size());
	}

	@Test
	void afterLoadEmptyFileShouldHaveEmptyListsAndEmptyHistory() {
		fileManager.clearTasks();
		fileManager.clearEpictasks();
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);

		Assertions.assertEquals(0, fileManager2.getTasksList().size());
		Assertions.assertEquals(0, fileManager2.getEpictasksList().size());
		Assertions.assertEquals(0, fileManager2.getSubtasksList().size());
		Assertions.assertEquals(0, fileManager.getHistory().size());
	}

	/**
	 * TESTING CLEAR TASKS
	 */

	@Test
	void clearTasksShouldOnlyRemoveTasks() {
		super.clearTasksShouldOnlyRemoveTasks(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(0, fileManager2.getTasksList().size());
		Assertions.assertEquals(2, fileManager2.getEpictasksList().size());
		Assertions.assertEquals(2, fileManager2.getSubtasksList().size());
	}

	@Test
	void clearEpictasksShouldRemoveEpictasksAndSubtasks() {
		super.clearEpictasksShouldRemoveEpictasksAndSubtasks(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(0, fileManager2.getEpictasksList().size());
		Assertions.assertEquals(0, fileManager2.getSubtasksList().size());
		Assertions.assertEquals(2, fileManager2.getTasksList().size());
	}

	@Test
	void clearSubtasksShouldOnlyRemoveSubtasks() {
		super.clearSubtasksShouldOnlyRemoveSubtasks(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(0, fileManager2.getSubtasksList().size());
		Assertions.assertEquals(2, fileManager2.getEpictasksList().size());
		Assertions.assertEquals(2, fileManager2.getTasksList().size());
	}

	/**
	 * TESTING getTaskById
	 */

	@Test
	void getTaskByIdShouldReturnTaskWithThatId() {
		super.getTaskByIdShouldReturnTaskWithThatId(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(1, fileManager2.getTaskById(1).getId());
		Assertions.assertEquals("Task", fileManager2.getTaskById(1).getName());
	}

	@Test
	void getEpictaskByIdShouldReturnEpictaskWithThatId() {
		super.getEpictaskByIdShouldReturnEpictaskWithThatId(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(3, fileManager2.getEpictaskById(3).getId());
		Assertions.assertEquals("Epictask", fileManager2.getEpictaskById(3).getName());
	}

	@Test
	void getSubtaskByIdShouldReturnSubtaskWithThatId() {
		super.getSubtaskByIdShouldReturnSubtaskWithThatId(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(5, fileManager2.getSubtaskById(5).getId());
		Assertions.assertEquals("Subtask", fileManager2.getSubtaskById(5).getName());
	}

	@Test
	void getTaskEpictaskSubtaskByIdShouldReturnNullIfThatListIsEmpty() {
		super.getTaskEpictaskSubtaskByIdShouldReturnNullIfThatListIsEmpty(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		fileManager2.clearSubtasks();
		Assertions.assertNull(fileManager2.getSubtaskById(4));
		fileManager2.clearTasks();
		fileManager2.clearEpictasks();
		Assertions.assertNull(fileManager2.getTaskById(1));
		Assertions.assertNull(fileManager2.getEpictaskById(3));
	}

	/**
	 * TESTING CREATE NEW TASK
	 */

	@Test
	void createNewTaskShouldSetIdAndAddToTasksListAndSaveToFile() {
		super.createNewTaskShouldSetIdAndAddToTasksList(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(1, fileManager2.getTaskById(1).getId());
	}

	@Test
	void createNewEpicTaskShouldSetIdAndAddToEpicTasksListAndSaveToFile() {
		super.createNewEpictaskShouldSetIdAndAddToEpictasksList(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(3, fileManager2.getEpictaskById(3).getId());
	}

	@Test
	void createNewSubTaskShouldSetIdAndAddToEpicTasksListAndSaveToFile() {
		super.createNewSubtaskShouldSetIdAndAddToSubtasksList(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(5, fileManager2.getSubtaskById(5).getId());
	}

	/**
	 * TESTING UPDATE TASK
	 */

	@Test
	void taskShouldHaveNewDescriptionAfterUpdateAndSaveToFile() {
		super.taskShouldHaveNewDescriptionAfterUpdate(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals("after update", fileManager2.getTaskById(1).getDescription());
	}

	@Test
	void epictaskShouldHaveNewDescriptionAfterUpdateAndSaveToFile() {
		super.epictaskShouldHaveNewDescriptionAfterUpdate(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals("after update", fileManager2.getEpictaskById(3).getDescription());
	}

	@Test
	void subtaskShouldHaveNewDescriptionAfterUpdateAndSaveToFile() {
		super.subtaskShouldHaveNewDescriptionAfterUpdate(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals("after update", fileManager2.getSubtaskById(5).getDescription());
	}

	/**
	 * TESTING DELETE TASK
	 */

	@Test
	void taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1AndSaveToFile() {
		super.taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertNull(fileManager2.getTaskById(1));
	}

	@Test
	void epictaskWithId3ShouldBeDeletedAfterDeleteEpictaskWithId3() {
		super.epictaskWithId3ShouldBeDeletedAfterDeleteEpictaskWithId3(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertNull(fileManager2.getEpictaskById(3));
	}

	@Test
	void subtaskWithId5ShouldBeDeletedAfterDeleteSubtaskWithId5() {
		super.subtaskWithId5ShouldBeDeletedAfterDeleteSubtaskWithId5(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertNull(fileManager2.getSubtaskById(5));
	}

	/**
	 * TESTING getSubtasks
	 */

	@Test
	void shouldReturnListOfSubtasks() {
		super.shouldReturnListOfSubtasks(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Collection<Subtask> subtasks = fileManager2.getEpictaskById(3).getSubtasks();

		Assertions.assertArrayEquals(List.of(fileManager2.getSubtaskById(5), fileManager2
				.getSubtaskById(6)).toArray(), subtasks.toArray());
	}

	/**
	 * TESTING EPIC STATUS
	 */

	@Test
	void epicStatusIsNewWhenEpictaskIsCreated() {
		super.epicStatusIsNewWhenEpictaskIsCreated(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(Status.NEW, fileManager2.getEpictaskById(4).getStatus());
	}

	@Test
	void epicStatusIsNewWhenAllSubtasksAreNew() {
		super.epicStatusIsNewWhenAllSubtasksAreNew(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(Status.NEW, fileManager2.getEpictaskById(3).getStatus());
	}

	@Test
	void epicStatusIsDoneWhenAllSubtasksAreDone() {
		super.epicStatusIsDoneWhenAllSubtasksAreDone(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(Status.DONE, fileManager2.getEpictaskById(3).getStatus());
	}

	@Test
	void epicStatusIsInProgressWhenSubtasksAreNewAndDone() {
		super.epicStatusIsInProgressWhenSubtasksAreNewAndDone(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(Status.IN_PROGRESS, fileManager2.getEpictaskById(3).getStatus());
	}

	@Test
	void epicStatusIsInProgressWhenAllSubtasksAreInProgress() {
		super.epicStatusIsInProgressWhenAllSubtasksAreInProgress(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(Status.IN_PROGRESS, fileManager2.getEpictaskById(3).getStatus());
	}

	/**
	 * TESTING trying to get epictask from subtask (getEpicTaskById)
	 */

	@Test
	void subtaskShouldHaveEpicId3() {
		super.subtaskShouldHaveEpicId3(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(3, fileManager2.getSubtaskById(5).getEpicTaskId());
	}

	/**
	 * TESTING HISTORY
	 */
	@Test
	void shouldReturnHistoryAsListOfTasksWithId1Id2Id3Id5() {
		super.shouldReturnHistoryAsListOfTasksWithId1Id2Id3Id5(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		List<Task> expected = new ArrayList<>();

		expected.add(fileManager2.getTaskById(1));
		expected.add(fileManager2.getTaskById(2));
		expected.add(fileManager2.getEpictaskById(3));
		expected.add(fileManager2.getSubtaskById(5));
		Assertions.assertArrayEquals(expected.toArray(), fileManager2.getHistory().toArray());
	}

	@Test
	void getTaskByIdShouldSaveTaskToHistory() {
		Task task = fileManager.getTaskById(1);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertArrayEquals(List.of(task).toArray(), fileManager2.getHistory().toArray());
	}

	@Test
	void getEpictaskByIdShouldSaveEpictaskToHistory() {
		Epictask epictask = fileManager.getEpictaskById(3);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertArrayEquals(List.of(epictask).toArray(), fileManager2.getHistory().toArray());
	}

	@Test
	void getSubTaskByIdShouldSaveTaskToHistory() {
		Subtask subtask = fileManager.getSubtaskById(5);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertArrayEquals(List.of(subtask).toArray(), fileManager2.getHistory().toArray());
	}

	@Test
	void taskShouldGoToTheEndOfHistoryAfterGetTaskById() {
		List<Task> expected = new ArrayList<>();
		fileManager.getTaskById(1);
		expected.add(fileManager.getTaskById(2));
		expected.add(fileManager.getTaskById(1));
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertArrayEquals(expected.toArray(), fileManager2.getHistory().toArray());
	}

	/**
	 * TESTING TIME AND DURATION
	 */

	@Test
	void shouldCalculateTaskEndTime() {
		super.shouldCalculateTaskEndTime(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		assertEquals(fileManager2.getTaskById(1).getStartTime().plusMinutes(20), fileManager2.getTaskById(1).getEndTime());
	}

	@Test
	void shouldCalculateEpictaskStartTime() {
		super.shouldCalculateEpictaskStartTime(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Assertions.assertEquals(fileManager2.getSubtaskById(5).getStartTime(), fileManager2.getEpictaskById(3).getStartTime());
	}

	@Test
	void shouldCalculateEpictaskEndTime() {
		super.shouldCalculateEpictaskEndTime(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		assertEquals(fileManager2.getSubtaskById(6).getEndTime(), fileManager2.getEpictaskById(3).getEndTime());
	}

	@Test
	void shouldCalculateSubtaskEndTime() {
		super.shouldCalculateSubtaskEndTime(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		assertEquals(fileManager2.getSubtaskById(5).getStartTime().plusMinutes(20),
				fileManager2.getSubtaskById(5).getEndTime());
	}

	/**
	 * TESTING time sorting
	 */

	@Test
	void shouldSortFromEarliestToLatestStartTime() {
		super.shouldSortFromEarliestToLatestStartTime(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		List<Task> testList = new ArrayList<>(fileManager2.getPrioritizedTasks());
		Assertions.assertEquals(1, testList.get(0).getId());
		Assertions.assertEquals(2, testList.get(1).getId());
		Assertions.assertEquals(5, testList.get(2).getId());
		Assertions.assertEquals(6, testList.get(3).getId());
	}

	@Test
	void testSortByTimeWithNulls() {
		super.testSortByTimeWithNulls(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		List<Task> testList = new ArrayList<>(fileManager2.getPrioritizedTasks());
		Assertions.assertEquals(1, testList.get(0).getId());
		Assertions.assertEquals(2, testList.get(1).getId());
		Assertions.assertEquals(5, testList.get(2).getId());
		Assertions.assertEquals(6, testList.get(3).getId());
		Assertions.assertEquals(8, testList.get(4).getId());
		Assertions.assertEquals(7, testList.get(5).getId());
	}

	/**
	 * TESTING TIME CONFLICT
	 */

	@Test
	void shouldThrowStartEndTimeConflictExceptionWhenStartTimeConflicts() {
		super.shouldThrowStartEndTimeConflictExceptionWhenStartTimeConflicts(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Task task = fileManager2.getTaskById(2);
		task.setStartTime(LocalDateTime.now().plusMinutes(15));
		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> fileManager2.updateTask(task));

		assertEquals("В это время уже есть другая задача.", exception.getMessage());
	}

	@Test
	void shouldThrowStartEndTimeConflictExceptionWhenEndTimeConflicts() {
		super.shouldThrowStartEndTimeConflictExceptionWhenEndTimeConflicts(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Task task = fileManager2.getTaskById(1);
		task.setDuration(Duration.ofMinutes(60));

		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> fileManager2.updateTask(task));

		assertEquals("В это время уже есть другая задача.", exception.getMessage());
	}

	@Test
	void whenIncludesInTimeExistingTask() {
		super.whenIncludesInTimeExistingTask(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		Task task = new Task("Task3", "id7", LocalDateTime.now().plusMinutes(70), Duration.ofMinutes(40));

		final StartEndTimeConflictException exception = assertThrows(
				StartEndTimeConflictException.class,
				() -> fileManager2.createNewTask(task));

		assertEquals("В это время уже есть другая задача.", exception.getMessage());
	}

	@Test
	void whenTaskStartTimeEqualsExistingTaskEndTime() {
		super.whenTaskStartTimeEqualsExistingTaskEndTime(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		assertEquals(fileManager2.getSubtaskById(6).getEndTime(), fileManager2.getTaskById(7).getStartTime());
	}

	@Test
	void whenTaskEndTimeEqualsExistingTaskStartTime(){
		super.whenTaskEndTimeEqualsExistingTaskStartTime(fileManager);
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);
		assertEquals(fileManager2.getSubtaskById(6).getStartTime().truncatedTo(ChronoUnit.SECONDS),
				fileManager2.getTaskById(7).getEndTime());
	}
}