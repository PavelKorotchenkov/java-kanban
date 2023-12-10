package ru.yandex.taskmanager.service;

import com.google.gson.*;
import ru.yandex.taskmanager.api.KVTaskClient;
import ru.yandex.taskmanager.exception.ManagerSaveException;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;
import ru.yandex.taskmanager.util.FileStringConverter;
import ru.yandex.taskmanager.util.LocalDateTimeAdapter;

import java.io.IOException;
import java.time.LocalDateTime;

public class HttpTaskManager extends FileBackedTasksManager {
	private final String URL;
	private static KVTaskClient client;
	private static Gson gson;

	public HttpTaskManager(String url) throws IOException, InterruptedException {
		super(url);
		this.URL = url;
		client = new KVTaskClient(8078);
		gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		/*KVServer server = new KVServer();
		server.start();
		HttpTaskManager manager = new HttpTaskManager("http://localhost:8078/");
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.of(18, 30);

		Task task1 = new Task("Task name", "Task desc", LocalDateTime.of(date, time), Duration.ofMinutes(30));
		manager.createNewTask(task1);
		manager.getTaskById(task1.getId());

		Task task2 = new Task("Task name2", "Task desc2");
		manager.createNewTask(task2);
		manager.getTaskById(task2.getId());

		Epictask epic = new Epictask("Epic", "Epic desc");
		manager.createNewEpictask(epic);
		manager.getEpictaskById(epic.getId());

		Subtask sub = new Subtask("Sub 1", "Sub desc", 3);
		manager.createNewSubtask(sub);
		manager.getSubtaskById(sub.getId());

		manager.deleteTaskById(task1.getId());

		System.out.println("BEFORE LOAD");
		System.out.println(manager.getTasksList());
		System.out.println(manager.getEpictasksList());
		System.out.println(manager.getSubtasksList());

		System.out.println("AFTER LOAD");
		HttpTaskManager manager2 = HttpTaskManager.load("http://localhost:8078/");
		System.out.println(manager2.getTasksList());
		System.out.println(manager2.getEpictasksList());
		System.out.println(manager2.getSubtasksList());
*/
	}

	@Override
	protected void save() {
		try {
			String jsonTasks = gson.toJson(getAllTasksEpictasksSubtasks());
			client.put("alltasks", jsonTasks);

			String jsonHistory = gson.toJson(FileStringConverter.historyToString(getHistoryManager()));
			client.put("history", jsonHistory);

		} catch (IOException | InterruptedException e) {
			throw new ManagerSaveException("Ошибка при сохранении на сервер");
		}
	}

	public static HttpTaskManager load(String url) {
		HttpTaskManager httpTaskManager;
		try {
			httpTaskManager = new HttpTaskManager(url);

			JsonElement jsonTasks = JsonParser.parseString(client.load("alltasks"));
			JsonElement jsonHistory = JsonParser.parseString(client.load("history"));

			if (jsonTasks != null) {
				JsonArray jsonTasksArray = jsonTasks.getAsJsonArray();
				int maxId = 0;

				for (JsonElement task : jsonTasksArray) {
					JsonObject jsonObject = task.getAsJsonObject();
					String type = jsonObject.get("type").getAsString();
					Task newTask;
					switch (type) {
						case "TASK":
							newTask = gson.fromJson(task, Task.class);
							httpTaskManager.tasks.put(newTask.getId(), newTask);
							httpTaskManager.tasksSortedByStartTime.add(newTask);
							break;
						case "EPIC":
							newTask = gson.fromJson(task, Epictask.class);
							httpTaskManager.epictasks.put(newTask.getId(), (Epictask) newTask);
							break;
						case "SUBTASK":
							newTask = gson.fromJson(task, Subtask.class);
							httpTaskManager.subtasks.put(newTask.getId(), (Subtask) newTask);
							httpTaskManager.tasksSortedByStartTime.add(newTask);
							break;
						default:
							throw new ManagerSaveException("Передан некорректный JSON");
					}

					int taskId = newTask.getId();
					if (taskId > maxId) {
						maxId = taskId;
					}
				}
				httpTaskManager.taskId = maxId;
			}

			String string = jsonHistory.getAsString();
			if (!string.isBlank()) {
				String[] array = jsonHistory.getAsString().split(",");
				for (String task : array) {
					int id = Integer.parseInt(task);
					if (httpTaskManager.tasks.containsKey(id)) {
						httpTaskManager.getTask(id, httpTaskManager.tasks);
					} else if (httpTaskManager.epictasks.containsKey(id)) {
						httpTaskManager.getTask(id, httpTaskManager.epictasks);
					} else if (httpTaskManager.subtasks.containsKey(id)) {
						httpTaskManager.getTask(id, httpTaskManager.subtasks);
					}
				}
			}

		} catch (IOException | InterruptedException e) {
			throw new ManagerSaveException("Ошибка при загрузке менеджера с сервера");
		}
		return httpTaskManager;
	}

	@Override
	public void clearTasks() {
		super.clearTasks();
	}

	@Override
	public void clearEpictasks() {
		super.clearEpictasks();
	}

	@Override
	public void clearSubtasks() {
		super.clearSubtasks();
	}

	@Override
	public void createNewTask(Task task) {
		super.createNewTask(task);
	}

	@Override
	public void createNewEpictask(Epictask task) {
		super.createNewEpictask(task);
	}

	@Override
	public void createNewSubtask(Subtask subtask) {
		super.createNewSubtask(subtask);
	}

	@Override
	public void updateTask(Task task) {
		super.updateTask(task);
	}

	@Override
	public void updateSubtask(Subtask task) {
		super.updateSubtask(task);
	}

	@Override
	public void updateEpictask(Epictask task) {
		super.updateEpictask(task);
	}

	@Override
	public void deleteTaskById(int taskId) {
		super.deleteTaskById(taskId);
	}

	@Override
	public void deleteEpictaskById(int taskId) {
		super.deleteEpictaskById(taskId);
	}

	@Override
	public void deleteSubtaskById(int taskId) {
		super.deleteSubtaskById(taskId);
	}

	@Override
	public Task getTaskById(int taskId) {
		return super.getTaskById(taskId);
	}

	@Override
	public Epictask getEpictaskById(int taskId) {
		return super.getEpictaskById(taskId);
	}

	@Override
	public Subtask getSubtaskById(int taskId) {
		return super.getSubtaskById(taskId);
	}
}
