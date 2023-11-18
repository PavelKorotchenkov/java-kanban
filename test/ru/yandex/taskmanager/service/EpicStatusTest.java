package ru.yandex.taskmanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Status;
import ru.yandex.taskmanager.model.Subtask;

class EpicStatusTest {

	TaskManager mtm = new InMemoryTaskManager();
	TaskManager fbtm = new FileBackedTasksManager("./resources/saved.csv");

	@Test
	void epicStatusIsNewWhenEpictaskIsCreated() {
		Epictask epictask = new Epictask("Epic", "1");
		mtm.createNewEpictask(epictask);
		Assertions.assertEquals(Status.NEW, epictask.getStatus());
	}

	@Test
	void epicStatusIsNewWhenAllSubtasksAreNew() {
		Epictask epictask = new Epictask("Epic", "1");
		mtm.createNewEpictask(epictask);
		Subtask subtask = new Subtask("Subtask1", "1.1", epictask.getId());
		Subtask subtask2 = new Subtask("Subtask2", "1.2", epictask.getId());
		mtm.createNewSubtask(subtask);
		mtm.createNewSubtask(subtask2);
		Assertions.assertEquals(Status.NEW, mtm.getEpictaskById(epictask.getId()).getStatus());
	}

	@Test
	void epicStatusIsDoneWhenAllSubtasksAreDone() {
		Epictask epictask = new Epictask("Epic", "1");
		mtm.createNewEpictask(epictask);
		Subtask subtask = new Subtask("Subtask1", "1.1", epictask.getId());
		Subtask subtask2 = new Subtask("Subtask2", "1.2", epictask.getId());
		mtm.createNewSubtask(subtask);
		mtm.createNewSubtask(subtask2);
		subtask.setStatus(Status.DONE);
		mtm.updateSubtask(subtask);
		subtask2.setStatus(Status.DONE);
		mtm.updateSubtask(subtask2);
		Assertions.assertEquals(Status.DONE, mtm.getEpictaskById(epictask.getId()).getStatus());
	}

	@Test
	void epicStatusIsInProgressWhenSubtasksAreNewAndDone() {
		Epictask epictask = new Epictask("Epic", "1");
		mtm.createNewEpictask(epictask);
		Subtask subtask = new Subtask("Subtask1", "1.1", epictask.getId());
		Subtask subtask2 = new Subtask("Subtask2", "1.2", epictask.getId());
		mtm.createNewSubtask(subtask);
		mtm.createNewSubtask(subtask2);
		subtask.setStatus(Status.DONE);
		mtm.updateSubtask(subtask);
		Assertions.assertEquals(Status.IN_PROGRESS, mtm.getEpictaskById(epictask.getId()).getStatus());
	}

	@Test
	void epicStatusIsIsInProgressWhenAllSubtasksAreInProgress() {
		Epictask epictask = new Epictask("Epic", "1");
		mtm.createNewEpictask(epictask);
		Subtask subtask = new Subtask("Subtask1", "1.1", epictask.getId());
		Subtask subtask2 = new Subtask("Subtask2", "1.2", epictask.getId());
		mtm.createNewSubtask(subtask);
		mtm.createNewSubtask(subtask2);
		subtask.setStatus(Status.IN_PROGRESS);
		mtm.updateSubtask(subtask);
		subtask2.setStatus(Status.IN_PROGRESS);
		mtm.updateSubtask(subtask2);
		Assertions.assertEquals(Status.IN_PROGRESS, mtm.getEpictaskById(epictask.getId()).getStatus());
	}
}