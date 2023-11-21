package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.*;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Status;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

abstract class TaskManagerTest<T extends TaskManager> {
	TaskManager memoryManager = new InMemoryTaskManager();
	FileBackedTasksManager fileManager = new FileBackedTasksManager("./resources/test.csv");

	/**
	 * PREPARING TESTS
	 */

	/*@AfterEach
	void clearAllListsAfterTests(){
		memoryManager.clearTasks();
		memoryManager.clearEpictasks();
		memoryManager.clearSubtasks();

		fileManager.clearTasks();
		fileManager.clearEpictasks();
		fileManager.clearSubtasks();
	}*/

	Task createTask(String name, String description) {
		Task task = new Task(name, description, LocalDateTime.now(), Duration.ofMinutes(20));
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

	Subtask createSubtask(String name, String description, int epictask) {
		Subtask subtask = new Subtask(name, description, LocalDateTime.now(), Duration.ofMinutes(20), epictask);
		memoryManager.createNewSubtask(subtask);
		fileManager.createNewSubtask(subtask);
		return subtask;
	}

	@BeforeEach
	void creatingTasks() {
		createTask("Task", "id1");
		createTask("Task2", "id2");
		Epictask epic = createEpictask("Epictask", "id3");
		createEpictask("Epictask2", "id4");
		createSubtask("Subtask", "id5", epic.getId());
		createSubtask("Subtask2", "id6", epic.getId());
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
		Assertions.assertEquals(0, manager.getTasksList().size());
		Assertions.assertEquals(0, manager.getEpictasksList().size());
		Assertions.assertEquals(0, manager.getSubtasksList().size());
	}

	/**
	 * TESTING CLEAR TASKS
	 */

	void clearTasksShouldOnlyRemoveTasks(TaskManager manager) {
		manager.clearTasks();
		Assertions.assertEquals(0, manager.getTasksList().size());
		Assertions.assertEquals(2, manager.getEpictasksList().size());
		Assertions.assertEquals(2, manager.getSubtasksList().size());
	}

	void clearEpictasksShouldRemoveEpictasksAndSubtasks(TaskManager manager) {
		manager.clearEpictasks();
		Assertions.assertEquals(0, manager.getEpictasksList().size());
		Assertions.assertEquals(0, manager.getSubtasksList().size());
		Assertions.assertEquals(2, manager.getTasksList().size());
	}

	void clearSubtasksShouldOnlyRemoveSubtasks(TaskManager manager) {
		manager.clearSubtasks();
		Assertions.assertEquals(0, manager.getSubtasksList().size());
		Assertions.assertEquals(2, manager.getEpictasksList().size());
		Assertions.assertEquals(2, manager.getTasksList().size());
	}

	/**
	 * TESTING getTaskById
	 */

	void getTaskByIdShouldReturnTaskWithThatId(TaskManager manager) {
		Assertions.assertEquals(1, manager.getTaskById(1).getId());
		Assertions.assertEquals("Task", manager.getTaskById(1).getName());
	}

	void getEpictaskByIdShouldReturnEpictaskWithThatId(TaskManager manager) {
		Assertions.assertEquals(3, manager.getEpictaskById(3).getId());
		Assertions.assertEquals("Epictask", manager.getEpictaskById(3).getName());

	}

	void getSubtaskByIdShouldReturnSubtaskWithThatId(TaskManager manager) {
		Assertions.assertEquals(5, manager.getSubtaskById(5).getId());
		Assertions.assertEquals("Subtask", manager.getSubtaskById(5).getName());

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
		Assertions.assertEquals(1, manager.getTaskById(1).getId());
		Assertions.assertEquals(2, manager.getTaskById(2).getId());
	}

	void createNewEpictaskShouldSetIdAndAddToEpictasksList(TaskManager manager) {
		Assertions.assertEquals(3, manager.getEpictaskById(3).getId());
		Assertions.assertEquals(4, manager.getEpictaskById(4).getId());
	}

	void createNewSubtaskShouldSetIdAndAddToSubtasksList(TaskManager manager) {
		Assertions.assertEquals(5, manager.getSubtaskById(5).getId());
		Assertions.assertEquals(6, manager.getSubtaskById(6).getId());
	}

	/**
	 * TESTING UPDATE TASK
	 */

	void taskShouldHaveNewDescriptionAfterUpdate(TaskManager manager) {
		manager.getTaskById(1).setDescription("after update");
		manager.updateTask(manager.getTaskById(1));
		Assertions.assertEquals("after update", manager.getTaskById(1).getDescription());
	}

	void epictaskShouldHaveNewDescriptionAfterUpdate(TaskManager manager) {
		manager.getEpictaskById(3).setDescription("after update");
		manager.updateEpictask(manager.getEpictaskById(3));
		Assertions.assertEquals("after update", manager.getEpictaskById(3).getDescription());
	}

	void subtaskShouldHaveNewDescriptionAfterUpdate(TaskManager manager) {
		manager.getSubtaskById(5).setDescription("after update");
		manager.updateSubtask(manager.getSubtaskById(5));
		Assertions.assertEquals("after update", manager.getSubtaskById(5).getDescription());
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
		Assertions.assertEquals(Status.NEW, manager.getEpictaskById(4).getStatus());
	}

	void epicStatusIsNewWhenAllSubtasksAreNew(TaskManager manager) {
		Assertions.assertEquals(Status.NEW, manager.getEpictaskById(3).getStatus());
	}

	void epicStatusIsDoneWhenAllSubtasksAreDone(TaskManager manager) {
		manager.getSubtaskById(5).setStatus(Status.DONE);
		manager.getSubtaskById(6).setStatus(Status.DONE);
		manager.updateSubtask(manager.getSubtaskById(5));
		manager.updateSubtask(manager.getSubtaskById(6));
		Assertions.assertEquals(Status.DONE, manager.getEpictaskById(3).getStatus());
	}

	void epicStatusIsInProgressWhenSubtasksAreNewAndDone(TaskManager manager) {
		manager.getSubtaskById(6).setStatus(Status.DONE);
		manager.updateSubtask(manager.getSubtaskById(6));
		Assertions.assertEquals(Status.IN_PROGRESS, manager.getEpictaskById(3).getStatus());
	}

	void epicStatusIsInProgressWhenAllSubtasksAreInProgress(TaskManager manager) {
		manager.getSubtaskById(5).setStatus(Status.IN_PROGRESS);
		manager.getSubtaskById(6).setStatus(Status.IN_PROGRESS);
		manager.updateSubtask(manager.getSubtaskById(5));
		manager.updateSubtask(manager.getSubtaskById(6));
		Assertions.assertEquals(Status.IN_PROGRESS, manager.getEpictaskById(3).getStatus());
	}

	/**
	 * TESTING trying to get epictask from subtask (getEpicTaskId method)
	 */

	void subtaskShouldHaveEpicId3(TaskManager manager) {
		Assertions.assertEquals(3, manager.getSubtaskById(5).getEpicTaskId());
	}

	/**
	 * TESTING HISTORY
	 */

	void getTaskByIdShouldSaveTaskToHistory(TaskManager manager) {
		Assertions.assertEquals(manager.getTaskById(1), manager.getHistory().get(0));
	}

	void getEpictaskByIdShouldSaveEpictaskToHistory(TaskManager manager) {
		Assertions.assertEquals(manager.getEpictaskById(3), manager.getHistory().get(0));
	}

	void getSubTaskByIdShouldSaveTaskToHistory(TaskManager manager) {
		Assertions.assertEquals(manager.getSubtaskById(5), manager.getHistory().get(0));
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
		Assertions.assertEquals(endTime.getMinute(), manager.getTaskById(1).getEndTime().getMinute());
	}

	void shouldCalculateEpictaskEndTime(TaskManager manager) {
		Subtask subtask = new Subtask("Subtask", "for time check", LocalDateTime.now(), Duration.ofMinutes(30), manager.getEpictaskById(3).getId());
		manager.createNewSubtask(subtask);
		LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofMinutes(30));
		Assertions.assertEquals(endTime.getMinute(), manager.getEpictaskById(3).getEndTime().getMinute());
	}

	void shouldCalculateSubtaskEndTime(TaskManager manager) {
		LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofMinutes(20));
		Assertions.assertEquals(endTime.getMinute(), manager.getSubtaskById(5).getEndTime().getMinute());
	}

	/**
	 * TESTING getStartTimeSort
	 */

	void shouldSortFromEarliestToLatestStartTime(TaskManager manager) {
		Task taskTime1 = new Task("test task 1", "should be 1st", LocalDateTime.now().minusMinutes(60), Duration.ofMinutes(20));
		manager.createNewTask(taskTime1);
		Task taskTime2 = new Task("test task 2", "should be last", LocalDateTime.now().plusDays(3), Duration.ofMinutes(20));
		manager.createNewTask(taskTime2);
		List<Task> testList = new ArrayList<>(manager.getTaskSortedByStartTime());
		Assertions.assertEquals(7, testList.get(0).getId());
		Assertions.assertEquals(8, testList.get(5).getId());
	}
}