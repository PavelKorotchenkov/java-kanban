package ru.yandex.taskmanager.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.taskmanager.exception.StartEndTimeConflictException;
import ru.yandex.taskmanager.model.Epictask;
import ru.yandex.taskmanager.model.Subtask;
import ru.yandex.taskmanager.model.Task;

import ru.yandex.taskmanager.service.TaskManager;
import ru.yandex.taskmanager.util.DurationDeserializer;
import ru.yandex.taskmanager.util.LocalDateTimeAdapter;
import ru.yandex.taskmanager.util.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class HttpTaskServer implements HttpHandler {
	private final int port;
	private final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	private final Gson gson;
	private final TaskManager manager;
	private final HttpServer server;

	public HttpTaskServer(int port, TaskManager manager) throws IOException {
		this.port = port;
		gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.registerTypeAdapter(Duration.class, new DurationDeserializer())
				.create();
		this.manager = manager;
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/tasks", this);
	}

	public void start() {
		System.out.println("Сервер начал работу на порту " + port);
		server.start();
	}

	public void stop() {
		System.out.println("Сервер остановил работу на порту " + port);
		server.stop(0);
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Endpoint endpoint;

		if (exchange.getRequestURI().getQuery() != null) {
			String url = exchange.getRequestURI().getPath() + exchange.getRequestURI().getQuery();
			endpoint = getEndpoint(url, exchange.getRequestMethod());
		} else {
			endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
		}

		switch (endpoint) {
			case GET_ALL_TASKS:
				handleGetAllTasks(exchange);
				break;
			case GET_ALL_EPICTASKS:
				handleGetAllEpictasks(exchange);
				break;
			case GET_ALL_SUBTASKS:
				handleGetAllSubtasks(exchange);
				break;
			case GET_TASK_BY_ID:
				handleGetTaskByID(exchange);
				break;
			case GET_EPICTASK_BY_ID:
				handleGetEpictaskById(exchange);
				break;
			case GET_SUBTASK_BY_ID:
				handleGetSubtaskById(exchange);
				break;
			case GET_SUBTASKS_FROM_EPIC:
				handleGetSubtasksFromEpic(exchange);
				break;
			case GET_HISTORY:
				handleGetHistory(exchange);
				break;
			case GET_PRIORITIZED_TASKS:
				handleGetPrioritizedTasks(exchange);
				break;
			case GET_ALL_TASKS_EPICTASKS_SUBTASKS:
				handleGetAllTasksEpictasksSubtasks(exchange);
				break;
			case ADD_UPDATE_TASK:
				handleAddUpdateTask(exchange);
				break;
			case ADD_UPDATE_EPICTASK:
				handleAddUpdateEpictask(exchange);
				break;
			case ADD_UPDATE_SUBTASK:
				handleAddUpdateSubtask(exchange);
				break;
			case DELETE_TASK_BY_ID:
				handleDeleteTaskById(exchange);
				break;
			case DELETE_EPICTASK_BY_ID:
				handleDeleteEpictaskById(exchange);
				break;
			case DELETE_SUBTASK_BY_ID:
				handleDeleteSubtaskById(exchange);
				break;
			case DELETE_ALL_TASKS:
				handleDeleteAllTasks(exchange);
				break;
			case DELETE_ALL_EPICTASKS:
				handleDeleteAllEpictasks(exchange);
				break;
			case DELETE_ALL_SUBTASKS:
				handleDeleteAllSubtasks(exchange);
				break;
			case UNKNOWN:
				writeResponse(exchange, "Такого эндпоинта не существует", 404);
				break;
		}
	}

	private void handleGetAllTasks(HttpExchange exchange) throws IOException {
		writeResponse(exchange, gson.toJson(manager.getTasksList()), 200);
	}

	private void handleGetAllEpictasks(HttpExchange exchange) throws IOException {
		writeResponse(exchange, gson.toJson(manager.getEpictasksList()), 200);
	}

	private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
		writeResponse(exchange, gson.toJson(manager.getSubtasksList()), 200);
	}

	private void handleGetTaskByID(HttpExchange exchange) throws IOException {
		Optional<? extends Task> task = findTask(exchange, manager.getTasksList());
		if (task.isEmpty()) {
			writeResponse(exchange, "Задача с таким ID не найдена", 404);
		} else {
			manager.getTaskById(task.get().getId());
			writeResponse(exchange, gson.toJson(task.get()), 200);
		}
	}

	private void handleGetEpictaskById(HttpExchange exchange) throws IOException {
		Optional<? extends Task> task = findTask(exchange, manager.getEpictasksList());
		if (task.isEmpty()) {
			writeResponse(exchange, "Эпик с таким ID не найден", 404);
		} else {
			manager.getEpictaskById(task.get().getId());
			writeResponse(exchange, gson.toJson(task.get()), 200);
		}
	}

	private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
		Optional<? extends Task> task = findTask(exchange, manager.getSubtasksList());
		if (task.isEmpty()) {
			writeResponse(exchange, "Подзадача с таким ID не найдена", 404);
		} else {
			manager.getSubtaskById(task.get().getId());
			writeResponse(exchange, gson.toJson(task.get()), 200);
		}
	}

	private void handleGetSubtasksFromEpic(HttpExchange exchange) throws IOException {
		Optional<? extends Task> task = findTask(exchange, manager.getEpictasksList());
		if (task.isEmpty()) {
			writeResponse(exchange, "Эпик с таким ID не найден", 404);
		} else {
			writeResponse(exchange, gson.toJson(manager.getSubtasks(task.get().getId())), 200);
		}
	}

	private void handleGetHistory(HttpExchange exchange) throws IOException {
		writeResponse(exchange, gson.toJson(manager.getHistory()), 200);
	}

	private void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
		writeResponse(exchange, gson.toJson(manager.getPrioritizedTasks()), 200);
	}

	private void handleGetAllTasksEpictasksSubtasks(HttpExchange exchange) throws IOException {
		writeResponse(exchange, gson.toJson(manager.getAllTasksEpictasksSubtasks()), 200);
	}

	private void handleAddUpdateTask(HttpExchange exchange) throws IOException {
		try {
			String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
			Task newTask = gson.fromJson(body, Task.class);
			if (newTask.getId() == 0) {
				manager.createNewTask(newTask);
				writeResponse(exchange, "Задача добавлена", 200);
			} else {
				if (taskFound(newTask.getId(), manager.getEpictasksList())) {
					manager.updateTask(newTask);
					writeResponse(exchange, "Задача обновлена", 200);
				} else {
					writeResponse(exchange, "В вашем запросе указан несуществующий ID", 404);
				}
			}
		} catch (StartEndTimeConflictException s) {
			writeResponse(exchange, "На это время уже запланирована другая задача", 400);
		} catch (Exception e) {
			writeResponse(exchange, "Получен некорректный JSON", 400);
		}
	}

	private void handleAddUpdateEpictask(HttpExchange exchange) throws IOException {
		try {
			String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
			Epictask newTask = gson.fromJson(body, Epictask.class);
			if (newTask.getId() == 0) {
				manager.createNewEpictask(newTask);
				writeResponse(exchange, "Эпик добавлен", 200);
			} else {
				if (taskFound(newTask.getId(), manager.getEpictasksList())) {
					manager.updateEpictask(newTask);
					writeResponse(exchange, "Эпик обновлен", 200);
				} else {
					writeResponse(exchange, "В вашем запросе указан несуществующий ID", 404);
				}
			}
		} catch (Exception e) {
			writeResponse(exchange, "Получен некорректный JSON", 400);
		}
	}

	private void handleAddUpdateSubtask(HttpExchange exchange) throws IOException {
		try {
			String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
			Subtask newTask = gson.fromJson(body, Subtask.class);
			int epicId = newTask.getEpicTaskId();
			if (!taskFound(epicId, manager.getEpictasksList())) {
				writeResponse(exchange, "Эпика с переданным ID не существует", 404);
			} else {
				if (newTask.getId() == 0) {
					manager.createNewSubtask(newTask);
					writeResponse(exchange, "Подзадача добавлена", 200);
				} else {
					if (taskFound(newTask.getId(), manager.getSubtasksList())) {
						manager.updateSubtask(newTask);
						writeResponse(exchange, "Задача обновлена", 200);
					} else {
						writeResponse(exchange, "В вашем запросе указан несуществующий ID", 404);
					}
				}
			}
		} catch (StartEndTimeConflictException s) {
			writeResponse(exchange, "На это время уже запланирована другая подзадача", 400);
		} catch (Exception e) {
			writeResponse(exchange, "Получен некорректный JSON", 400);
		}
	}

	private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
		Optional<? extends Task> task = findTask(exchange, manager.getTasksList());
		if (task.isEmpty()) {
			writeResponse(exchange, "Задача с таким ID не найдена", 404);
		} else {
			manager.deleteTaskById(task.get().getId());
			writeResponse(exchange, "Задача удалена", 200);
		}
	}

	private void handleDeleteEpictaskById(HttpExchange exchange) throws IOException {
		Optional<? extends Task> task = findTask(exchange, manager.getSubtasksList());
		if (task.isEmpty()) {
			writeResponse(exchange, "Эпик с таким ID не найден", 404);
		} else {
			manager.deleteEpictaskById(task.get().getId());
			writeResponse(exchange, "Эпик удален", 200);
		}
	}

	private void handleDeleteSubtaskById(HttpExchange exchange) throws IOException {
		Optional<? extends Task> task = findTask(exchange, manager.getSubtasksList());
		if (task.isEmpty()) {
			writeResponse(exchange, "Подзадача с таким ID не найдена", 404);
		} else {
			manager.deleteSubtaskById(task.get().getId());
			writeResponse(exchange, "Подзадача удалена", 200);
		}
	}

	private void handleDeleteAllTasks(HttpExchange exchange) throws IOException {
		manager.clearTasks();
		writeResponse(exchange, "Все задачи удалены", 200);
	}

	private void handleDeleteAllEpictasks(HttpExchange exchange) throws IOException {
		manager.clearEpictasks();
		writeResponse(exchange, "Все эпики удалены", 200);
	}

	private void handleDeleteAllSubtasks(HttpExchange exchange) throws IOException {
		manager.clearSubtasks();
		writeResponse(exchange, "Все подзадачи удалены", 200);
	}

	private Endpoint getEndpoint(String path, String requestMethod) {
		String[] pathParts = path.split("/"); // localhost:8080/tasks/task/?id=1
		switch (requestMethod) {
			case "GET":

				/*             * GET TASKS			 * */
				if (pathParts.length == 3 && pathParts[2].equals("task")) {
					return Endpoint.GET_ALL_TASKS;
				}

				if (pathParts.length == 3 && pathParts[2].equals("epictask")) {
					return Endpoint.GET_ALL_EPICTASKS;
				}

				if (pathParts.length == 3 && pathParts[2].equals("subtask")) {
					return Endpoint.GET_ALL_SUBTASKS;
				}

				/*             * GET TASK BY ID			 * */
				if (pathParts.length == 4 && pathParts[2].equals("task") && pathParts[3].startsWith("id=")) {
					return Endpoint.GET_TASK_BY_ID;
				}

				if (pathParts.length == 4 && pathParts[2].equals("epictask") && pathParts[3].startsWith("id=")) {
					return Endpoint.GET_EPICTASK_BY_ID;
				}

				if (pathParts.length == 4 && pathParts[2].equals("subtask") && pathParts[3].startsWith("id=")) {
					return Endpoint.GET_SUBTASK_BY_ID;
				}

				/*             * GET SUBTASKS FROM EPIC			 * */ //localhost:8080/tasks/subtask/epic/?id=1
				if (pathParts.length == 5 && pathParts[2].equals("subtask") && pathParts[3].equals("epic") && pathParts[4].startsWith("id=")) {
					return Endpoint.GET_SUBTASKS_FROM_EPIC;
				}

				/*             * GET HISTORY			 * */ //localhost:8080/tasks/history
				if (pathParts.length == 3 && pathParts[2].equals("history")) {
					return Endpoint.GET_HISTORY;
				}

				/*             * GET PRIORITIZED TASKS			 * */ //localhost:8080/tasks
				if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
					return Endpoint.GET_PRIORITIZED_TASKS;
				}

				/*             * GET PRIORITIZED TASKS			 * */ //localhost:8080/alltasks
				if (pathParts.length == 2 && pathParts[1].equals("alltasks")) {
					return Endpoint.GET_ALL_TASKS_EPICTASKS_SUBTASKS;
				}

				break;

			case "POST":
				/*             * ADD || UPDATE TASK			 * */ // localhost:8080/tasks/task/Body:{task...}
				if (pathParts.length == 3 && pathParts[2].equals("task")) {
					return Endpoint.ADD_UPDATE_TASK;
				}

				if (pathParts.length == 3 && pathParts[2].equals("epictask")) {
					return Endpoint.ADD_UPDATE_EPICTASK;
				}

				if (pathParts.length == 3 && pathParts[2].equals("subtask")) {
					return Endpoint.ADD_UPDATE_SUBTASK;
				}
				break;

			case "DELETE":
				/*             * DELETE TASK BY ID			 * */ //localhost:8080/tasks/task/?id=1
				if (pathParts.length == 4 && pathParts[2].equals("task") && pathParts[3].startsWith("id=")) {
					return Endpoint.DELETE_TASK_BY_ID;
				}

				if (pathParts.length == 4 && pathParts[2].equals("epictask") && pathParts[3].startsWith("id=")) {
					return Endpoint.DELETE_EPICTASK_BY_ID;
				}

				if (pathParts.length == 4 && pathParts[2].equals("subtask") && pathParts[3].startsWith("id=")) {
					return Endpoint.DELETE_SUBTASK_BY_ID;
				}

				/*             * DELETE ALL TASKS			 * */ //localhost:8080/tasks/task
				if (pathParts.length == 3 && pathParts[2].equals("task")) {
					return Endpoint.DELETE_ALL_TASKS;
				}

				if (pathParts.length == 3 && pathParts[2].equals("epictask")) {
					return Endpoint.DELETE_ALL_EPICTASKS;
				}

				if (pathParts.length == 3 && pathParts[2].equals("subtask")) {
					return Endpoint.DELETE_ALL_SUBTASKS;
				}
		}

		return Endpoint.UNKNOWN;
	}

	private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
		if (responseString.isBlank()) {
			exchange.sendResponseHeaders(responseCode, 0);
		} else {
			byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
			exchange.sendResponseHeaders(responseCode, bytes.length);
			try (OutputStream os = exchange.getResponseBody()) {
				os.write(bytes);
			}
		}
		exchange.close();
	}

	private Optional<? extends Task> findTask(HttpExchange exchange, List<? extends Task> taskList) throws IOException {
		String pathId = exchange.getRequestURI().getQuery().replace("id=", "");
		Optional<? extends Task> optionalTask = Optional.empty();
		try {
			int id = Integer.parseInt(pathId);
			optionalTask = taskList.stream().filter(task -> task.getId() == id).findFirst();
		} catch (NumberFormatException exception) {
			writeResponse(exchange, "Передан некорректный ID.", 400);
		}

		return optionalTask;
	}

	private boolean taskFound(int id, List<? extends Task> taskList) {
		for (Task task : taskList) {
			if (task.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		KVServer kvServer = new KVServer();
		kvServer.start();
		TaskManager httpManager = Managers.getDefault();
		HttpTaskServer taskServer = new HttpTaskServer(8080, httpManager);
		taskServer.start();
		Thread.sleep(5000);
		taskServer.stop();
	}
}

