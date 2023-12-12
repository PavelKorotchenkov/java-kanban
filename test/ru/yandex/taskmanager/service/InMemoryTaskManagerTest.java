package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.taskmanager.model.Epictask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

	/**
	 * TESTING GET TASKS LIST
	 */

	@BeforeEach
	void getManager() {
		super.manager = new InMemoryTaskManager();

		createTask("Task", "id1", LocalDateTime.now(), Duration.ofMinutes(20));
		sleep();
		createTask("Task2", "id2", LocalDateTime.now().plusMinutes(45), Duration.ofMinutes(20));
		sleep();
		Epictask epic = createEpictask("Epictask", "id3");
		sleep();
		createEpictask("Epictask2", "id4");
		sleep();
		createSubtask("Subtask", "id5", LocalDateTime.now().plusMinutes(80), Duration.ofMinutes(20), epic.getId());
		sleep();
		createSubtask("Subtask2", "id6", LocalDateTime.now().plusMinutes(120), Duration.ofMinutes(20), epic.getId());
	}

	@Test
	@DisplayName("Метод getTasksList должен возвращать сохранённые задачи")
	void tasksListShouldContainTaskAfterCreated() {
		super.tasksListShouldContainTaskAfterCreated(manager);
	}

	@Test
	@DisplayName("Метод getEpictasksList должен возвращать сохранённые эпики")
	void epictasksListShouldContainEpicTaskAfterCreated() {
		super.epictasksListShouldContainEpicTaskAfterCreated(manager);
	}

	@Test
	@DisplayName("Метод getSubtasksList должен возвращать сохранённые подзадачи")
	void subtasksListShouldContainSubTaskAfterCreated() {
		super.subtasksListShouldContainSubTaskAfterCreated(manager);
	}

	@Test
	@DisplayName("Методы clearTasks и clearEpictasks должен очищать списки задач, эпиков и их подзадач")
	void tasksListsAreEmptyShouldReturnEmptyLists() {
		super.tasksListsAreEmptyShouldReturnEmptyLists(manager);
	}

	/**
	 * TESTING CLEAR TASKS
	 */

	@Test
	void clearTasksShouldOnlyRemoveTasks() {
		super.clearTasksShouldOnlyRemoveTasks(manager);
	}

	@Test
	void clearEpictasksShouldRemoveEpictasksAndSubtasks() {
		super.clearEpictasksShouldRemoveEpictasksAndSubtasks(manager);
	}

	@Test
	void clearSubtasksShouldOnlyRemoveSubtasks() {
		super.clearSubtasksShouldOnlyRemoveSubtasks(manager);
	}

	/**
	 * TESTING getTaskById
	 * */


	@Test
	void getTaskByIdShouldReturnTaskWithThatId() {
		super.getTaskByIdShouldReturnTaskWithThatId(manager);
	}

	@Test
	void getEpictaskByIdShouldReturnEpictaskWithThatId() {
		super.getEpictaskByIdShouldReturnEpictaskWithThatId(manager);
	}

	@Test
	void getSubtaskByIdShouldReturnSubtaskWithThatId() {
		super.getSubtaskByIdShouldReturnSubtaskWithThatId(manager);
	}

	@Test
	void getTaskEpictaskSubtaskByIdShouldReturnNullIfThatListIsEmpty() {
		super.getTaskEpictaskSubtaskByIdShouldReturnNullIfThatListIsEmpty(manager);
	}

	/**
	 * TESTING CREATE NEW TASK
	 */

	@Test
	void createNewTaskShouldSetIdAndAddToTasksList() {
		super.createNewTaskShouldSetIdAndAddToTasksList(manager);
	}

	@Test
	void createNewEpictaskShouldSetIdAndAddToEpictasksList() {
		super.createNewEpictaskShouldSetIdAndAddToEpictasksList(manager);
	}

	@Test
	void createNewSubtaskShouldSetIdAndAddToSubtasksList() {
		super.createNewSubtaskShouldSetIdAndAddToSubtasksList(manager);
	}

	/**
	 * TESTING UPDATE TASK
	 */

	@Test
	void taskShouldHaveNewDescriptionAfterUpdate() {
		super.taskShouldHaveNewDescriptionAfterUpdate(manager);
	}

	@Test
	void epictaskShouldHaveNewDescriptionAfterUpdate() {
		super.epictaskShouldHaveNewDescriptionAfterUpdate(manager);
	}

	@Test
	void subtaskShouldHaveNewDescriptionAfterUpdate() {
		super.subtaskShouldHaveNewDescriptionAfterUpdate(manager);
	}

	/**
	 * TESTING DELETE TASK
	 */

	@Test
	void taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1() {
		super.taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1(manager);
	}

	@Test
	void epictaskWithId3ShouldBeDeletedAfterDeleteEpictaskWithId3() {
		super.epictaskWithId3ShouldBeDeletedAfterDeleteEpictaskWithId3(manager);
	}

	@Test
	void subtaskWithId5ShouldBeDeletedAfterDeleteSubtaskWithId5() {
		super.subtaskWithId5ShouldBeDeletedAfterDeleteSubtaskWithId5(manager);
	}

	/**
	 * TESTING getSubtasks
	 */

	@Test
	void shouldReturnListOfSubtasks() {
		super.shouldReturnListOfSubtasks(manager);
	}

	/**
	 * TESTING EPIC STATUS
	 */

	@Test
	void epicStatusIsNewWhenEpictaskIsCreated() {
		super.epicStatusIsNewWhenEpictaskIsCreated(manager);
	}

	@Test
	void epicStatusIsNewWhenAllSubtasksAreNew() {
		super.epicStatusIsNewWhenAllSubtasksAreNew(manager);
	}

	@Test
	void epicStatusIsDoneWhenAllSubtasksAreDone() {
		super.epicStatusIsDoneWhenAllSubtasksAreDone(manager);
	}

	@Test
	void epicStatusIsInProgressWhenSubtasksAreNewAndDone() {
		super.epicStatusIsInProgressWhenSubtasksAreNewAndDone(manager);
	}

	@Test
	void epicStatusIsInProgressWhenAllSubtasksAreInProgress() {
		super.epicStatusIsInProgressWhenAllSubtasksAreInProgress(manager);
	}

	/**
	 * TESTING trying to get epictask from subtask (getEpicTaskById)
	 */

	@Test
	void subtaskShouldHaveEpicId3() {
		super.subtaskShouldHaveEpicId3(manager);
	}

	/**
	 * TESTING HISTORY
	 */

	@Test
	void getTaskByIdShouldSaveTaskToHistory() {
		super.getTaskByIdShouldSaveTaskToHistory(manager);
	}

	@Test
	void getEpictaskByIdShouldSaveEpictaskToHistory() {
		super.getEpictaskByIdShouldSaveEpictaskToHistory(manager);
	}

	@Test
	void getSubTaskByIdShouldSaveTaskToHistory() {
		super.getSubTaskByIdShouldSaveTaskToHistory(manager);
	}

	@Test
	void shouldReturnHistoryAsListOfTasksWithId1Id2Id3Id5() {
		super.shouldReturnHistoryAsListOfTasksWithId1Id2Id3Id5(manager);
	}

	@Test
	void taskShouldGoToTheEndOfHistoryAfterGetTaskById() {
		super.taskShouldGoToTheEndOfHistoryAfterGetTaskById(manager);
	}

	/**
	 * TESTING TIME AND DURATION
	 */

	@Test
	void shouldCalculateTaskEndTime() {
		super.shouldCalculateTaskEndTime(manager);
	}

	@Test
	void shouldCalculateEpictaskStartTime() {
		super.shouldCalculateEpictaskStartTime(manager);
	}

	@Test
	void shouldCalculateEpictaskEndTime() {
		super.shouldCalculateEpictaskEndTime(manager);
	}

	@Test
	void shouldCalculateSubtaskEndTime() {
		super.shouldCalculateSubtaskEndTime(manager);
	}

	/**
	 * TESTING time sorting
	 */

	@Test
	void shouldSortFromEarliestToLatestStartTime() {
		super.shouldSortFromEarliestToLatestStartTime(manager);
	}

	@Test
	void testSortByTimeWithNulls() {
		super.testSortByTimeWithNulls(manager);
	}

	/**
	 * TESTING TIME CONFLICT
	 */

	@Test
	void shouldThrowStartEndTimeConflictExceptionWhenStartTimeConflicts() {
		super.shouldThrowStartEndTimeConflictExceptionWhenStartTimeConflicts(manager);
	}

	@Test
	void shouldThrowStartEndTimeConflictExceptionWhenEndTimeConflicts() {
		super.shouldThrowStartEndTimeConflictExceptionWhenEndTimeConflicts(manager);
	}

	@Test
	void whenIncludesInTimeExistingTask() {
		super.whenIncludesInTimeExistingTask(manager);
	}

	@Test
	void whenTaskStartTimeEqualsExistingTaskEndTime() {
		super.whenTaskStartTimeEqualsExistingTaskEndTime(manager);
	}

	@Test
	void whenTaskEndTimeEqualsExistingTaskStartTime(){
		super.whenTaskEndTimeEqualsExistingTaskStartTime(manager);
	}

	@Test
	void givenTaskDuration_whenChangeDuration_thenPreviousDurationShouldBeFree() {
		super.givenTaskDuration_whenChangeDuration_thenPreviousDurationShouldBeFree(manager);
	}
}