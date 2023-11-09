package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.exception.ManagerSaveException;
import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.util.FileStringConverter;
import ru.yandex.taskmanager.util.Managers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

	public static void main(String[] args) {
		String path = "./resources/saved.csv";
		TaskManager manager = Managers.getDefault();
		Task task1 = new Task("Задача №1", "1");
		manager.createNewTask(task1);

		Task task2 = new Task("Задача №2", "2");
		manager.createNewTask(task2);

		Epictask task3 = new Epictask("Эпик Задача №1", "3");
		manager.createNewEpictask(task3);

		Subtask task4 = new Subtask("Подзадача №1", "4", task3.getId());
		manager.createNewSubtask(task4);

		Subtask task5 = new Subtask("Подзадача №2", "5", task3.getId());
		manager.createNewSubtask(task5);

		Epictask task6 = new Epictask("Эпик Задача №2", "6");
		manager.createNewEpictask(task6);

		Task task7 = new Task("Задача №7", "7");
		manager.createNewTask(task7);

		manager.getTaskById(task1.getId());
		manager.getTaskById(task2.getId());
		manager.getEpictaskById(task3.getId());
		manager.getSubtaskById(task4.getId());
		manager.getSubtaskById(task5.getId());
		manager.getEpictaskById(task6.getId());
		manager.getSubtaskById(task5.getId());
		manager.getSubtaskById(task4.getId());
		task1.setStatus(Status.IN_PROGRESS);
		manager.updateTask(task1);
		task5.setStatus(Status.IN_PROGRESS);
		manager.updateSubtask(task5);
		manager.getTaskById(task1.getId());

		//load from file
		TaskManager manager1 = FileBackedTasksManager.load(new File(path));
		System.out.println("FIRST LOAD");
		System.out.println(manager1.getTasksList());
		System.out.println(manager1.getEpictasksList());
		System.out.println(manager1.getSubtasksList());
		System.out.println(manager1.getHistory());
		System.out.println();

		Task task8 = new Task("New task 8", "after load 8");
		manager1.createNewTask(task8);
		Task task9 = new Task("New task 9", "after load 9");
		manager1.createNewTask(task9);
		manager1.getTaskById(task8.getId());
		manager1.getTaskById(task9.getId());

		System.out.println("AFTER LOAD AND CREATING NEW TASKS:");
		System.out.println(manager1.getTasksList());
		System.out.println(manager1.getEpictasksList());
		System.out.println(manager1.getSubtasksList());
		System.out.println("NEW HISTORY");
		System.out.println(manager1.getHistory());

		System.out.println("GET");
		System.out.println(manager1.getTaskById(7));

	}

	private final String path;

	public FileBackedTasksManager(String path) {
		this.path = path;
	}

	private void save() {
		String header = "id,type,name,status,description,epic\n";
		try (Writer writer = new FileWriter(path)) {
			writer.write(header);
			for (Task task : getTasksList()) {
				writer.write(FileStringConverter.taskToString(task));
			}

			for (Epictask epictask : getEpictasksList()) {
				writer.write(FileStringConverter.taskToString(epictask));
			}

			for (Subtask subtask : getSubtasksList()) {
				writer.write(FileStringConverter.taskToString(subtask));
			}

			writer.write("\n");
			String history = FileStringConverter.historyToString(getHistoryManager());
			writer.write(history);

		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка при сохранении");
		}
	}

	public static FileBackedTasksManager load(File file) {
		FileBackedTasksManager manager = new FileBackedTasksManager(file.getAbsolutePath());
		String string;
		try {
			string = Files.readString(Path.of(file.getAbsolutePath()));
		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка при загрузке");
		}

		String[] fileContents = string.split("\n", -1);
		
		if (fileContents.length < 2) {
			return manager;
		}

		int maxId = -1; // refactor
		for (int line = 1; line < fileContents.length - 2; line++) {
			Task task = FileStringConverter.taskFromString(fileContents[line]);
			final int id = task.getId();// refactor
			if (task.getId() > maxId) {// refactor
				maxId = task.getId();
			}

			switch (task.getType()) {
				case TASK:
					manager.tasks.put(id,task);
					break;
				case SUBTASK:
					manager.subtasks.put(id,(Subtask) task);
					break;
				case EPIC:
					manager.epictasks.put(id,(Epictask) task);
					break;
			}
		}
		manager.taskId = maxId;  // refactor

		if (fileContents[fileContents.length - 1].isBlank()) {
			return manager;
		}

		List<Integer> history = FileStringConverter.historyFromString(fileContents[fileContents.length - 1]);

		for (Integer i : history) {
			if (manager.tasks.containsKey(i)) {
				manager.getTask(i, manager.tasks);
			} else if (manager.epictasks.containsKey(i)) {
				manager.getTask(i, manager.epictasks);
			} else if (manager.subtasks.containsKey(i)){
				manager.getTask(i, manager.subtasks);
			}
		}

		return manager;
	}

	@Override
	public void clearTasks() {
		super.clearTasks();
		save();
	}

	@Override
	public void clearEpictasks() {
		super.clearEpictasks();
		save();
	}

	@Override
	public void clearSubtasks() {
		super.clearSubtasks();
		save();
	}

	@Override
	public void createNewTask(Task task) {
		super.createNewTask(task);
		save();
	}

	@Override
	public void createNewEpictask(Epictask task) {
		super.createNewEpictask(task);
		save();
	}

	@Override
	public void createNewSubtask(Subtask subtask) {
		super.createNewSubtask(subtask);
		save();
	}

	@Override
	public void updateTask(Task task) {
		super.updateTask(task);
		save();
	}

	@Override
	public void updateSubtask(Subtask task) {
		super.updateSubtask(task);
		save();
	}

	@Override
	public void updateEpictask(Epictask task) {
		super.updateEpictask(task);
		save();
	}

	@Override
	public void deleteTaskById(int taskId) {
		super.deleteTaskById(taskId);
		save();
	}

	@Override
	public void deleteEpictaskById(int taskId) {
		super.deleteEpictaskById(taskId);
		save();
	}

	@Override
	public void deleteSubtaskById(int taskId) {
		super.deleteSubtaskById(taskId);
		save();
	}

	@Override
	public Task getTaskById(int taskId) {
		Task task = super.getTaskById(taskId);
		save();
		return task;
	}

	@Override
	public Epictask getEpictaskById(int taskId) {
		Epictask task = super.getEpictaskById(taskId);
		save();
		return task;
	}

	@Override
	public Subtask getSubtaskById(int taskId) {
		Subtask task = super.getSubtaskById(taskId);
		save();
		return task;
	}
}
