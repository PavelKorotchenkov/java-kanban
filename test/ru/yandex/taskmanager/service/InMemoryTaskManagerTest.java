package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/*
NullPointerException ex = assertThrows(
				NullPointerException.class,
				() -> memoryManager.getTaskById(0)
		);
*/

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
	@Test
	void tasksListShouldContainTaskAfterCreated() {
		super.tasksListShouldContainTaskAfterCreated();
		Assertions.assertArrayEquals(List.of(memoryManager.getTaskById(1)).toArray(), memoryManager.getTasksList().toArray());
		Assertions.assertEquals(1, memoryManager.getTasksList().size());
	}

	@Test
	void epictasksListShouldContainEpicTaskAfterCreated() {
		super.epictasksListShouldContainEpicTaskAfterCreated();
		Assertions.assertArrayEquals(List.of(memoryManager.getEpictaskById(1)).toArray(), memoryManager.getEpictasksList().toArray());
		Assertions.assertEquals(1, memoryManager.getEpictasksList().size());
	}

	@Test
	void subtasksListShouldContainSubTaskAfterCreated() {
		super.subtasksListShouldContainSubTaskAfterCreated();
		Assertions.assertArrayEquals(List.of(memoryManager.getSubtaskById(2)).toArray(), memoryManager.getSubtasksList().toArray());
		Assertions.assertEquals(1, memoryManager.getSubtasksList().size());
	}

	@Test
	void tasksListsAreEmptyShouldReturnEmptyLists() {
		Assertions.assertEquals(0, memoryManager.getTasksList().size());
		Assertions.assertEquals(0, memoryManager.getEpictasksList().size());
		Assertions.assertEquals(0, memoryManager.getSubtasksList().size());
	}

	@Test
	void taskManagerShouldBeEmptyAfterClearTasksList() {
		super.taskManagerShouldBeEmptyAfterClearTasksList();
		memoryManager.clearTasks();
		Assertions.assertEquals(0, memoryManager.getTasksList().size());
	}

	@Test
	void taskManagerShouldBeEmptyAfterClearEpicsList() {
		super.taskManagerShouldBeEmptyAfterClearEpicsList();
		memoryManager.clearEpictasks();
		Assertions.assertEquals(0, memoryManager.getEpictasksList().size());
	}

	@Test
	void taskManagerShouldHave1EpicAnd0SubAfterClearSubsList() {
		super.taskManagerShouldHave1EpicAnd0SubAfterClearSubsList();
		memoryManager.clearSubtasks();
		Assertions.assertEquals(0, memoryManager.getSubtasksList().size());
		Assertions.assertEquals(1, memoryManager.getEpictasksList().size());
	}

	@Test
	void shouldWorkFineIfWasEmptyToBeginWith() {
		memoryManager.clearTasks();
		Assertions.assertEquals(0, memoryManager.getTasksList().size());
		Assertions.assertEquals(0, memoryManager.getEpictasksList().size());
		Assertions.assertEquals(0, memoryManager.getSubtasksList().size());
	}

	@Test
	void getTaskByIdShouldReturnTask() {
		super.getTaskByIdShouldReturnTask();
		Assertions.assertEquals(memoryManager.getTaskById(1), memoryManager.getHistory().get(0));
	}

	@Test
	void getEpicaskByIdShouldReturnEpictask() {
		super.getEpicaskByIdShouldReturnEpictask();
		Assertions.assertEquals(memoryManager.getEpictaskById(1), memoryManager.getHistory().get(0));
	}

	@Test
	void getSubtaskByIdShouldReturnSubtask() {
		super.getSubtaskByIdShouldReturnSubtask();
		Assertions.assertEquals(memoryManager.getSubtaskById(2), memoryManager.getHistory().get(0));
	}

	@Test
	void getTaskByIdShouldReturnNullIfListTaskIsEmpty() {
		Assertions.assertNull(memoryManager.getTaskById(1));
		Assertions.assertNull(memoryManager.getEpictaskById(1));
		Assertions.assertNull(memoryManager.getSubtaskById(1));
	}

	@Test
	void createNewTaskShouldSetIdAndAddToTasksList() {
		super.createNewTaskShouldSetIdAndAddToTasksList();
		Assertions.assertEquals(1, memoryManager.getTaskById(1).getId());
	}

	@Test
	void createNewEpicTaskShouldSetIdAndAddToEpicTasksList() {
		super.createNewEpicTaskShouldSetIdAndAddToEpicTasksList();
		Assertions.assertEquals(1, memoryManager.getEpictaskById(1).getId());
	}

	@Test
	void createNewSubTaskShouldSetIdAndAddToEpicTasksList() {
		super.createNewSubTaskShouldSetIdAndAddToEpicTasksList();
		Assertions.assertEquals(2, memoryManager.getSubtaskById(2).getId());
	}

	@Test
	void taskShouldHaveNewDescriptionAfterUpdate() {
		super.taskShouldHaveNewDescriptionAfterUpdate();
		memoryManager.updateTask(memoryManager.getTaskById(1));
		Assertions.assertEquals("after update", memoryManager.getTaskById(1).getDescription());
	}

	@Test
	void epictaskShouldHaveNewDescriptionAfterUpdate() {
		super.epictaskShouldHaveNewDescriptionAfterUpdate();
		memoryManager.updateEpictask(memoryManager.getEpictaskById(1));
		Assertions.assertEquals("after update", memoryManager.getEpictaskById(1).getDescription());
	}

	@Test
	void subtaskShouldHaveNewDescriptionAfterUpdate() {
		super.subtaskShouldHaveNewDescriptionAfterUpdate();
		memoryManager.updateSubtask(memoryManager.getSubtaskById(2));
		Assertions.assertEquals("after update", memoryManager.getSubtaskById(2).getDescription());
	}

	@Test
	void taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1() {
		super.taskWithId1ShouldBeDeletedAfterDeleteTaskWithId1();
		memoryManager.deleteTaskById(1);
		Assertions.assertArrayEquals(List.of(memoryManager.getTaskById(2)).toArray(), memoryManager.getTasksList().toArray());
	}

	@Test
	void epictaskWithId1ShouldBeDeletedAfterDeleteEpictaskWithId1() {
		super.epictaskWithId1ShouldBeDeletedAfterDeleteEpictaskWithId1();
		memoryManager.deleteEpictaskById(1);
		Assertions.assertArrayEquals(List.of(memoryManager.getEpictaskById(2)).toArray(), memoryManager.getEpictasksList().toArray());
	}

	@Test
	void subtaskWithId2ShouldBeDeletedAfterDeleteSubtaskWithId2() {
		super.subtaskWithId2ShouldBeDeletedAfterDeleteSubtaskWithId2();
		memoryManager.deleteSubtaskById(2);
		Assertions.assertArrayEquals(List.of(memoryManager.getSubtaskById(3)).toArray(), memoryManager.getSubtasksList().toArray());
	}

	@Test
	void shouldReturnListOfSubtasks() {
		super.shouldReturnListOfSubtasks();
		Collection<Subtask> subtasks = memoryManager.getEpictaskById(1).getSubtasks();
		Assertions.assertArrayEquals(List.of(memoryManager.getSubtaskById(2), memoryManager.getSubtaskById(3)).toArray(), subtasks.toArray());
	}

	@Test
	void shouldReturnListOfTasksWithId1Id2Id3Id4() {
		super.shouldReturnListOfTasksWithId1Id2Id3Id4();
		List<Task> expected = new ArrayList<>();
		expected.add(memoryManager.getTaskById(1));
		expected.add(memoryManager.getTaskById(2));
		expected.add(memoryManager.getEpictaskById(3));
		expected.add(memoryManager.getSubtaskById(4));
		Assertions.assertArrayEquals(expected.toArray(), memoryManager.getHistory().toArray());
	}
}