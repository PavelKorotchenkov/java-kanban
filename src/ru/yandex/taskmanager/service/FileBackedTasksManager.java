package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.exception.ManagerSaveException;
import ru.yandex.taskmanager.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

	public static void main(String[] args) {
		// save to file
		FileBackedTasksManager manager = new FileBackedTasksManager("D:\\kanban.txt");
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

		manager.getTaskById(task1.getId());
		manager.getTaskById(task2.getId());
		manager.getEpictaskById(task3.getId());
		manager.getSubtaskById(task4.getId());
		manager.getSubtaskById(task5.getId());
		manager.getEpictaskById(task6.getId());
		manager.getSubtaskById(task5.getId());
		manager.getSubtaskById(task4.getId());
		manager.deleteEpictaskById(6);
		task1.setStatus(Status.IN_PROGRESS);
		manager.updateTask(task1);
		task5.setStatus(Status.IN_PROGRESS);
		manager.updateSubtask(task5);

		//load from file
		/*FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("D:\\kanban.txt"));
		System.out.println(manager.getTasksList());
		System.out.println(manager.getEpictasksList());
		System.out.println(manager.getSubtasksList());
		System.out.println(manager.getHistory());*/
	}

	private final String path;

	public FileBackedTasksManager(String path) {
		this.path = path;
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

	private void save() {
		String header = "id,type,name,status,description,epic\n";
		try (Writer writer = new FileWriter(path)) {
			writer.write(header);
			for (Task task : getTasksList()) {
				writer.write(toString(task));
			}

			for (Epictask epictask : getEpictasksList()) {
				writer.write(toString(epictask));
			}

			for (Subtask subtask : getSubtasksList()) {
				writer.write(toString(subtask));
			}

			writer.write("\n");
			String history = historyToString(getHistoryManager());
			writer.write(history);

		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка при сохранении");
		}
	}

	static String historyToString(HistoryManager manager) {
		List<Task> tasks = manager.getHistory();
		if (tasks.isEmpty()) {
			return "no history";
		}
		String[] ids = new String[tasks.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = String.valueOf(tasks.get(i).getId());
		}

		return String.join(",", ids);
	}

	private String toString(Task task) {
		String result = task.getId() + ","
				+ task.getType() + ","
				+ task.getName() + ","
				+ task.getStatus() + ","
				+ task.getDescription() + ",";

		if (task instanceof Subtask) {
			int epicId = ((Subtask) task).getEpicId();
			result = result + epicId;
		}

		return result + "\n";
	}

	static List<Integer> historyFromString(String value) {
		List<Integer> list = new ArrayList<>();
		String[] array = value.split(",");
		for (String s : array) {
			list.add(Integer.parseInt(s));
		}

		return list;
	}

	private Task taskFromString(String value) {
		String[] att = value.split(",");
		Task task = null;
		if (att[1].equals(TaskType.TASK.name())) {
			task = new Task(att[2], att[4]);
		}

		if (att[1].equals(TaskType.EPIC.name())) {
			task = new Epictask(att[2], att[4]);
		}

		if (att[1].equals(TaskType.SUBTASK.name())) {
			task = new Subtask(att[2], att[4], Integer.parseInt(att[5]));
		}

		task.setId(Integer.parseInt(att[0]));
		task.setStatus(Status.valueOf(att[3]));

		return task;
	}

	public static FileBackedTasksManager loadFromFile(File file) {
		FileBackedTasksManager manager = new FileBackedTasksManager(file.getAbsolutePath());
		String string;
		try {
			string = Files.readString(Path.of(file.getAbsolutePath()));
		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка при загрузке");
		}

		String[] array = string.split("\n");
		if (array.length < 2) {
			return manager;
		}

		for (int i = 1; i < array.length - 2; i++) {
			String[] list = array[i].split(",");
			Task task = manager.taskFromString(array[i]);
			manager.setTaskId(task.getId() - 1);
			if (task instanceof Epictask) {
				manager.createNewEpictask((Epictask) task);
			} else if (task instanceof Subtask) {
				manager.createNewSubtask((Subtask) task);
			} else {
				manager.createNewTask(task);
			}

			task.setId(Integer.parseInt(list[0]));
		}

		if (array[array.length - 1].equals("no history")) {
			return manager;
		}

		List<Integer> history = historyFromString(array[array.length - 1]);
		for (Integer i : history) {
			Task task = manager.getTaskById(i);
			if (task != null) {
				continue;
			}

			task = manager.getEpictaskById(i);
			if (task != null) {
				continue;
			}

			task = manager.getSubtaskById(i);
		}

		return manager;
	}
}
