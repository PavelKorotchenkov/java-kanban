package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.taskmanager.exception.ManagerSaveException;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;
import ru.yandex.taskmanager.model.TaskType;
import ru.yandex.taskmanager.util.FileStringConverter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
	File file = new File("./resources/test.csv");

	private String[] readContents() {
		String string = "";
		try {
			string = Files.readString(Path.of(file.getAbsolutePath()));
		} catch (IOException e) {
			e.getMessage();
		}
		String[] tasks = string.split("\n");
		return tasks;
	}

	@Test
	void afterLoadShouldHaveSameListsSameHistoryAndIdShouldBeMaxIdCreated() {
		Task task = new Task("task", "easy");
		fileManager.createNewTask(task);
		Epictask epictask = new Epictask("epic", "hard");
		fileManager.createNewEpictask(epictask);
		Subtask subtask = new Subtask("subtask", "medium", epictask.getId());
		fileManager.createNewSubtask(subtask);
		fileManager.getTaskById(1);
		fileManager.getEpictaskById(2);
		fileManager.getSubtaskById(3);

		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);

		Assertions.assertEquals(fileManager.taskId, fileManager2.taskId);
		Assertions.assertArrayEquals(fileManager.getTasksList().toArray(), fileManager2.getTasksList().toArray());
		Assertions.assertArrayEquals(fileManager.getEpictasksList().toArray(), fileManager2.getEpictasksList().toArray());
		Assertions.assertArrayEquals(fileManager.getSubtasksList().toArray(), fileManager2.getSubtasksList().toArray());
		Assertions.assertArrayEquals(List.of(task, epictask, subtask).toArray(), fileManager.getHistory().toArray());
	}

	@Test
	void afterLoadWithNoHistoryShouldHaveSameListsAndEmptyHistory() {
		Task task = new Task("task", "easy");
		fileManager.createNewTask(task);
		Epictask epictask = new Epictask("epic", "hard");
		fileManager.createNewEpictask(epictask);
		Subtask subtask = new Subtask("subtask", "medium", epictask.getId());
		fileManager.createNewSubtask(subtask);

		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);

		Assertions.assertArrayEquals(fileManager.getTasksList().toArray(), fileManager2.getTasksList().toArray());
		Assertions.assertArrayEquals(fileManager.getEpictasksList().toArray(), fileManager2.getEpictasksList().toArray());
		Assertions.assertArrayEquals(fileManager.getSubtasksList().toArray(), fileManager2.getSubtasksList().toArray());
		Assertions.assertEquals(0, fileManager.getHistory().size());
	}

	@Test
	void afterLoadEpictaskWithoutSubtasksShouldCreateEpicWithoutSubtasks() {
		Epictask epictask = new Epictask("epic", "hard");
		fileManager.createNewEpictask(epictask);

		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);

		Assertions.assertEquals(0, fileManager2.getEpictaskById(1).getSubtasks().size());
	}

	@Test
	void afterLoadEmptyFileShouldHaveEmptyListsAndEmptyHistory() {
		Task task = new Task("task", "easy");
		fileManager.createNewTask(task);
		fileManager.clearTasks();
		FileBackedTasksManager fileManager2 = FileBackedTasksManager.load(file);

		Assertions.assertEquals(0, fileManager2.getTasksList().size());
		Assertions.assertEquals(0, fileManager2.getEpictasksList().size());
		Assertions.assertEquals(0, fileManager2.getSubtasksList().size());
		Assertions.assertEquals(0, fileManager.getHistory().size());
	}

	@Test
	void taskManagerShouldBeEmptyAfterClearTasksList() {
		super.taskManagerShouldBeEmptyAfterClearTasksList();
		fileManager.clearTasks();
		int length = readContents().length;
		Assertions.assertEquals(1, length);
	}

	@Test
	void taskManagerShouldBeEmptyAfterClearEpicsList() {
		super.taskManagerShouldBeEmptyAfterClearEpicsList();
		fileManager.clearEpictasks();
		int length = readContents().length;
		Assertions.assertEquals(1, length);
	}

	@Test
	void taskManagerShouldHave1EpicAnd0SubAfterClearSubsList() {
		super.taskManagerShouldHave1EpicAnd0SubAfterClearSubsList();
		fileManager.clearSubtasks();
		String[] tasks = readContents();
		int length = tasks.length;

		Task epictask = FileStringConverter.taskFromString(tasks[1]);
		Assertions.assertEquals(2, length);
		Assertions.assertEquals(TaskType.EPIC, epictask.getType());
		fileManager.clearEpictasks();
	}

	@Test
	void getTaskByIdShouldSaveTaskToFile() {
		super.getTaskByIdShouldReturnTask();
		int taskID = fileManager.getTaskById(1).getId();
		String[] tasks = readContents();
		List<Integer> history = FileStringConverter.historyFromString(tasks[tasks.length - 1]);
		Assertions.assertEquals(taskID, history.get(0));
	}

	@Test
	void getEpicTaskByIdShouldSaveTaskToFile() {
		super.getEpicaskByIdShouldReturnEpictask();
		int taskID = fileManager.getEpictaskById(1).getId();
		String[] tasks = readContents();
		List<Integer> history = FileStringConverter.historyFromString(tasks[tasks.length - 1]);
		Assertions.assertEquals(taskID, history.get(0));
	}

	@Test
	void getSubTaskByIdShouldSaveTaskToFile() {
		super.getSubtaskByIdShouldReturnSubtask();
		int taskID = fileManager.getSubtaskById(2).getId();
		String[] tasks = readContents();
		List<Integer> history = FileStringConverter.historyFromString(tasks[tasks.length - 1]);
		Assertions.assertEquals(taskID, history.get(0));
	}

	@Test
	void createNewTaskShouldSetIdAndAddToTasksListAndSaveToFile() {
		super.createNewTaskShouldSetIdAndAddToTasksList();
		int length = readContents().length;
		String[] attr = readContents()[1].split(",");
		int taskId = Integer.parseInt(attr[0]);
		Assertions.assertEquals(2, length);
		Assertions.assertEquals(1, taskId);
	}

	@Test
	void createNewEpicTaskShouldSetIdAndAddToEpicTasksListAndSaveToFile() {
		super.createNewEpicTaskShouldSetIdAndAddToEpicTasksList();
		int length = readContents().length;
		String[] attr = readContents()[1].split(",");
		int taskId = Integer.parseInt(attr[0]);
		Assertions.assertEquals(2, length);
		Assertions.assertEquals(1, taskId);
	}

	@Test
	void createNewSubTaskShouldSetIdAndAddToEpicTasksListAndSaveToFile() {
		super.createNewSubTaskShouldSetIdAndAddToEpicTasksList();
		int length = readContents().length;
		String[] attr = readContents()[2].split(",");
		int taskId = Integer.parseInt(attr[0]);
		Assertions.assertEquals(3, length);
		Assertions.assertEquals(2, taskId);
	}

	@Test
	void taskShouldHaveNewDescriptionAfterUpdateAndSaveToFile() {
		super.taskShouldHaveNewDescriptionAfterUpdate();
		fileManager.updateTask(fileManager.getTaskById(1));
		String[] content = readContents()[1].split(",");
		String description = content[4];
		Assertions.assertEquals("after update", description);
	}

	@Test
	void epictaskShouldHaveNewDescriptionAfterUpdateAndSaveToFile() {
		super.epictaskShouldHaveNewDescriptionAfterUpdate();
		fileManager.updateEpictask(fileManager.getEpictaskById(1));
		String[] content = readContents()[1].split(",");
		String description = content[4];
		Assertions.assertEquals("after update", description);
	}

	@Test
	void subtaskShouldHaveNewDescriptionAfterUpdateAndSaveToFile() {
		super.subtaskShouldHaveNewDescriptionAfterUpdate();
		fileManager.updateSubtask(fileManager.getSubtaskById(2));
		String[] content = readContents()[2].split(",");
		String description = content[4];
		Assertions.assertEquals("after update", description);
	}

	@Test
	void taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1AndSaveToFile() {
		super.taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1();
		fileManager.deleteTaskById(1);
		int length = readContents().length;
		String[] content = readContents()[1].split(",");
		int taskId = Integer.parseInt(content[0]);
		Assertions.assertEquals(2, taskId);
		Assertions.assertEquals(2, length);
	}

	@Test
	void epictaskWithId1ShouldBeDeletedAfterDeleteEpictaskWithId1() {
		super.epictaskWithId1ShouldBeDeletedAfterDeleteEpictaskWithId1();
		fileManager.deleteEpictaskById(1);
		int length = readContents().length;
		String[] content = readContents()[1].split(",");
		int taskId = Integer.parseInt(content[0]);
		Assertions.assertEquals(2, taskId);
		Assertions.assertEquals(2, length);
	}

	@Test
	void subtaskWithId2ShouldBeDeletedAfterDeleteSubtaskWithId2() {
		super.subtaskWithId2ShouldBeDeletedAfterDeleteSubtaskWithId2();
		fileManager.deleteSubtaskById(2);
		int length = readContents().length;
		String[] content = readContents()[2].split(",");
		int taskId = Integer.parseInt(content[0]);
		Assertions.assertEquals(3, taskId);
		Assertions.assertEquals(3, length);
	}
}