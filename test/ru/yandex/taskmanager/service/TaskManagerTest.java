package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;

import java.io.FileNotFoundException;

abstract class TaskManagerTest<T extends TaskManager> {
	TaskManager memoryManager = new InMemoryTaskManager();
	FileBackedTasksManager fileManager = new FileBackedTasksManager("./resources/test.csv");

	/*@AfterEach
	void clearAllListsAfterTests(){
		memoryManager.clearTasks();
		memoryManager.clearEpictasks();
		memoryManager.clearSubtasks();

		fileManager.clearTasks();
		fileManager.clearEpictasks();
		fileManager.clearSubtasks();
	}*/

	private Task createTask(String name, String description) {
		Task task = new Task(name, description);
		memoryManager.createNewTask(task);
		fileManager.createNewTask(task);
		return task;
	}

	private Epictask createEpictask(String name, String description) {
		Epictask epictask = new Epictask(name, description);
		memoryManager.createNewEpictask(epictask);
		fileManager.createNewEpictask(epictask);
		return epictask;
	}

	private Subtask createSubtask(String name, String description, int epictask) {
		Subtask subtask = new Subtask(name, description, epictask);
		memoryManager.createNewSubtask(subtask);
		fileManager.createNewSubtask(subtask);
		return subtask;
	}

	@Test
	void tasksListShouldContainTaskAfterCreated() {
		createTask("Task", "t1");
	}

	@Test
	void epictasksListShouldContainEpicTaskAfterCreated() {
		createEpictask("Epictask", "e1");
	}

	@Test
	void subtasksListShouldContainSubTaskAfterCreated() {
		Epictask epictask = createEpictask("Epictask", "e1");
		createSubtask("Subtask", "s1", epictask.getId());
	}

	@Test
	void subtaskShouldHaveEpicId1() {
		Epictask epictask = createEpictask("Epictask", "e1");
		Subtask subtask = createSubtask("Subtask", "s1", epictask.getId());

		Assertions.assertEquals(1, subtask.getEpicTaskId());
	}

	@Test
	void taskManagerShouldBeEmptyAfterClearTasksList() {
		createTask("Task", "t1");
		createTask("Task2", "t2");
	}

	@Test
	void taskManagerShouldBeEmptyAfterClearEpicsList() {
		createEpictask("Epictask", "e1");
		createEpictask("Epictask2", "e2");
	}

	@Test
	void taskManagerShouldHave1EpicAnd0SubAfterClearSubsList() {
		Epictask epictask = createEpictask("Epictask", "e1");
		createSubtask("Subtask", "s1", epictask.getId());
		createSubtask("Subtask2", "s2", epictask.getId());
	}

	@Test
	void getTaskByIdShouldReturnTask() {
		createTask("Task", "t1");
	}

	@Test
	void getEpicaskByIdShouldReturnEpictask() {
		createEpictask("Epictask", "e1");
	}

	@Test
	void getSubtaskByIdShouldReturnSubtask() {
		Epictask epictask = createEpictask("Epictask", "e1");
		createSubtask("Subtask", "s1", epictask.getId());
	}

	@Test
	void createNewTaskShouldSetIdAndAddToTasksList() {
		createTask("Task", "t1");
	}

	@Test
	void createNewEpicTaskShouldSetIdAndAddToEpicTasksList() {
		createEpictask("Epictask", "e1");
	}

	@Test
	void createNewSubTaskShouldSetIdAndAddToEpicTasksList() {
		Epictask epictask = createEpictask("Epictask", "e1");
		createSubtask("Subtask", "s1", epictask.getId());
	}

	@Test
	void taskShouldHaveNewDescriptionAfterUpdate() {
		Task task = createTask("Task", "before update");
		task.setDescription("after update");
	}

	@Test
	void epictaskShouldHaveNewDescriptionAfterUpdate() {
		Epictask epictask = createEpictask("epic", "before update");
		epictask.setDescription("after update");
	}

	@Test
	void subtaskShouldHaveNewDescriptionAfterUpdate() {
		Epictask epictask = createEpictask("epic", "test");
		Subtask subtask = createSubtask("subtask", "before update", epictask.getId());
		subtask.setDescription("after update");
	}

	@Test
	void taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1() {
		Task task = createTask("task", "t1");
		Task task2 = createTask("task2", "t2");
	}

	@Test
	void epictaskWithId1ShouldBeDeletedAfterDeleteEpictaskWithId1() {
		Epictask epictask = createEpictask("epic", "e1");
		Epictask epictask2 = createEpictask("epic2", "e2");
	}

	@Test
	void subtaskWithId2ShouldBeDeletedAfterDeleteSubtaskWithId2() {
		Epictask epictask = createEpictask("epic", "test");
		Subtask subtask = createSubtask("subtask", "sub test", epictask.getId());
		Subtask subtask2 = createSubtask("subtask2", "sub test 2", epictask.getId());
	}

	@Test
	void shouldReturnListOfSubtasks() {
		Epictask epictask = createEpictask("epic", "e1");
		Subtask subtask = createSubtask("subtask", "s1", epictask.getId());
		Subtask subtask2 = createSubtask("subtask2", "s2", epictask.getId());
	}

	@Test
	void shouldReturnListOfTasksWithId1Id2Id3Id4() {
		Task task = createTask("task", "t1");
		memoryManager.getTaskById(task.getId());
		Task task2 = createTask("task2", "t1");
		memoryManager.getTaskById(task2.getId());
		Epictask epictask = createEpictask("epic", "e1");
		memoryManager.getEpictaskById(epictask.getId());
		Subtask subtask = createSubtask("sub", "s1", epictask.getId());
		memoryManager.getSubtaskById(subtask.getId());
	}


}