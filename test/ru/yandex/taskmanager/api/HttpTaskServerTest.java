package ru.yandex.taskmanager.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.taskmanager.model.Task;
import ru.yandex.taskmanager.service.HttpTaskManager;
import ru.yandex.taskmanager.util.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
	KVServer server;
	HttpTaskServer taskServer;
	HttpTaskManager manager;

	@BeforeEach
	void setUp() throws IOException, InterruptedException {
		server = Managers.getDefaultKVServer();
		server.start();
		manager = Managers.getDefault();
		taskServer = new HttpTaskServer(8080,manager);
		taskServer.start();


		Task task = new Task("newTask", "description");
		manager.createNewTask(task);
		System.out.println("id = " + task.getId());
	}

	@AfterEach
	void stop() {
		server.stop();
		taskServer.stop();
	}

	/**
	 * Exceptions tests
	 */

	@Test
	@DisplayName("Если задача найдена - должен возвращаться код 200")
	void givenTask_whenTryToGetIt_shouldReturnCode200() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
		HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		int responseCode = response.statusCode();
		assertEquals(200, responseCode, "Unexpected status code");
	}

	@Test
	@DisplayName("Если задача не найдена - должен возвращаться код 404")
	void givenNoTask_whenTryToGetIt_shouldReturnCode404() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/tasks/task/?id=2");
		HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		int responseCode = response.statusCode();
		assertEquals(404, responseCode, "Unexpected status code");
	}

	@Test
	@DisplayName("Если ID введён некорректно - должен возвращаться код 400")
	void givenTask_whenTryToGetItWithIncorrectRequest_shouldReturnCode400() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/tasks/task/?id=asd");
		HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		int responseCode = response.statusCode();
		assertEquals(400, responseCode, "Unexpected status code");
	}
}