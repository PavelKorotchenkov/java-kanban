package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

	/**
	 * TESTING GET TASKS LIST
	 */

	@Test
	void tasksListShouldContainTaskAfterCreated() {
		super.tasksListShouldContainTaskAfterCreated(memoryManager);
	}

	@Test
	void epictasksListShouldContainEpicTaskAfterCreated() {
		super.epictasksListShouldContainEpicTaskAfterCreated(memoryManager);
	}

	@Test
	void subtasksListShouldContainSubTaskAfterCreated() {
		super.subtasksListShouldContainSubTaskAfterCreated(memoryManager);
	}

	@Test
	void tasksListsAreEmptyShouldReturnEmptyLists() {
		super.tasksListsAreEmptyShouldReturnEmptyLists(memoryManager);
	}

	/**
	 * TESTING CLEAR TASKS
	 */

	@Test
	void clearTasksShouldOnlyRemoveTasks() {
		super.clearTasksShouldOnlyRemoveTasks(memoryManager);
	}

	@Test
	void clearEpictasksShouldRemoveEpictasksAndSubtasks() {
		super.clearEpictasksShouldRemoveEpictasksAndSubtasks(memoryManager);
	}

	@Test
	void clearSubtasksShouldOnlyRemoveSubtasks() {
		super.clearSubtasksShouldOnlyRemoveSubtasks(memoryManager);
	}

	/**
	 * TESTING getTaskById
	 */

	@Test
	void getTaskByIdShouldReturnTaskWithThatId() {
		super.getTaskByIdShouldReturnTaskWithThatId(memoryManager);
	}

	@Test
	void getEpictaskByIdShouldReturnEpictaskWithThatId() {
		super.getEpictaskByIdShouldReturnEpictaskWithThatId(memoryManager);
	}

	@Test
	void getSubtaskByIdShouldReturnSubtaskWithThatId() {
		super.getSubtaskByIdShouldReturnSubtaskWithThatId(memoryManager);
	}

	@Test
	void getTaskEpictaskSubtaskByIdShouldReturnNullIfThatListIsEmpty() {
		super.getTaskEpictaskSubtaskByIdShouldReturnNullIfThatListIsEmpty(memoryManager);
	}

	/**
	 * TESTING CREATE NEW TASK
	 */

	@Test
	void createNewTaskShouldSetIdAndAddToTasksList() {
		super.createNewTaskShouldSetIdAndAddToTasksList(memoryManager);
	}

	@Test
	void createNewEpictaskShouldSetIdAndAddToEpictasksList() {
		super.createNewEpictaskShouldSetIdAndAddToEpictasksList(memoryManager);
	}

	@Test
	void createNewSubtaskShouldSetIdAndAddToSubtasksList() {
		super.createNewSubtaskShouldSetIdAndAddToSubtasksList(memoryManager);
	}

	/**
	 * TESTING UPDATE TASK
	 */

	@Test
	void taskShouldHaveNewDescriptionAfterUpdate() {
		super.taskShouldHaveNewDescriptionAfterUpdate(memoryManager);
	}

	@Test
	void epictaskShouldHaveNewDescriptionAfterUpdate() {
		super.epictaskShouldHaveNewDescriptionAfterUpdate(memoryManager);
	}

	@Test
	void subtaskShouldHaveNewDescriptionAfterUpdate() {
		super.subtaskShouldHaveNewDescriptionAfterUpdate(memoryManager);
	}

	/**
	 * TESTING DELETE TASK
	 */

	@Test
	void taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1() {
		super.taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1(memoryManager);
	}

	@Test
	void epictaskWithId3ShouldBeDeletedAfterDeleteEpictaskWithId3() {
		super.epictaskWithId3ShouldBeDeletedAfterDeleteEpictaskWithId3(memoryManager);
	}

	@Test
	void subtaskWithId5ShouldBeDeletedAfterDeleteSubtaskWithId5() {
		super.subtaskWithId5ShouldBeDeletedAfterDeleteSubtaskWithId5(memoryManager);
	}

	/**
	 * TESTING getSubtasks
	 */

	@Test
	void shouldReturnListOfSubtasks() {
		super.shouldReturnListOfSubtasks(memoryManager);
	}

	/**
	 * TESTING EPIC STATUS
	 */

	@Test
	void epicStatusIsNewWhenEpictaskIsCreated() {
		super.epicStatusIsNewWhenEpictaskIsCreated(memoryManager);
	}

	@Test
	void epicStatusIsNewWhenAllSubtasksAreNew() {
		super.epicStatusIsNewWhenAllSubtasksAreNew(memoryManager);
	}

	@Test
	void epicStatusIsDoneWhenAllSubtasksAreDone() {
		super.epicStatusIsDoneWhenAllSubtasksAreDone(memoryManager);
	}

	@Test
	void epicStatusIsInProgressWhenSubtasksAreNewAndDone() {
		super.epicStatusIsInProgressWhenSubtasksAreNewAndDone(memoryManager);
	}

	@Test
	void epicStatusIsInProgressWhenAllSubtasksAreInProgress() {
		super.epicStatusIsInProgressWhenAllSubtasksAreInProgress(memoryManager);
	}

	/**
	 * TESTING trying to get epictask from subtask (getEpicTaskById)
	 */

	@Test
	void subtaskShouldHaveEpicId3() {
		super.subtaskShouldHaveEpicId3(memoryManager);
	}

	/**
	 * TESTING HISTORY
	 */
	@Test
	void getTaskByIdShouldSaveTaskToHistory() {
		super.getTaskByIdShouldSaveTaskToHistory(memoryManager);
	}

	@Test
	void getEpictaskByIdShouldSaveEpictaskToHistory() {
		super.getEpictaskByIdShouldSaveEpictaskToHistory(memoryManager);
	}

	@Test
	void getSubTaskByIdShouldSaveTaskToHistory() {
		super.getSubTaskByIdShouldSaveTaskToHistory(memoryManager);
	}

	@Test
	void shouldReturnHistoryAsListOfTasksWithId1Id2Id3Id5() {
		super.shouldReturnHistoryAsListOfTasksWithId1Id2Id3Id5(memoryManager);
	}

	@Test
	void taskShouldGoToTheEndOfHistoryAfterGetTaskById() {
		super.taskShouldGoToTheEndOfHistoryAfterGetTaskById(memoryManager);
	}

	/**
	 * TESTING TIME AND DURATION
	 */

	@Test
	void shouldCalculateTaskEndTime() {
		super.shouldCalculateTaskEndTime(memoryManager);
	}

	@Test
	void shouldCalculateEpictaskStartTime() {
		super.shouldCalculateEpictaskStartTime(memoryManager);
	}

	@Test
	void shouldCalculateEpictaskEndTime() {
		super.shouldCalculateEpictaskEndTime(memoryManager);
	}

	@Test
	void shouldCalculateSubtaskEndTime() {
		super.shouldCalculateSubtaskEndTime(memoryManager);
	}

	/**
	 * TESTING time sorting
	 */

	@Test
	void shouldSortFromEarliestToLatestStartTime() {
		super.shouldSortFromEarliestToLatestStartTime(memoryManager);
	}

	@Test
	void testSortByTimeWithNulls() {
		super.testSortByTimeWithNulls(memoryManager);
	}

	/**
	 * TESTING TIME CONFLICT
	 */

	@Test
	void shouldThrowStartEndTimeConflictExceptionWhenStartTimeConflicts() {
		super.shouldThrowStartEndTimeConflictExceptionWhenStartTimeConflicts(memoryManager);
	}

	@Test
	void shouldThrowStartEndTimeConflictExceptionWhenEndTimeConflicts() {
		super.shouldThrowStartEndTimeConflictExceptionWhenEndTimeConflicts(memoryManager);
	}

	@Test
	void whenIncludesInTimeExistingTask() {
		super.whenIncludesInTimeExistingTask(memoryManager);
	}

	@Test
	void whenTaskStartTimeEqualsExistingTaskEndTime() {
		super.whenTaskStartTimeEqualsExistingTaskEndTime(memoryManager);
	}

	@Test
	void whenTaskEndTimeEqualsExistingTaskStartTime(){
		super.whenTaskEndTimeEqualsExistingTaskStartTime(memoryManager);
	}
}