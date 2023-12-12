package ru.yandex.taskmanager.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
	public static final String URL = "http://localhost:8078";
	private final HttpClient client;
	private final String apiToken;

	public KVTaskClient() throws IOException, InterruptedException {
		URI uri = URI.create(URL + "/register");
		client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
		apiToken = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
	}

	public void put(String key, String json) throws IOException, InterruptedException {
		/*должен сохранять состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN=.*/
		URI url = URI.create(URL + "/save/" + key + "?API_TOKEN=" + apiToken);
		HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json)).uri(url).build();
		client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	public String load(String key) throws IOException, InterruptedException {
		/*должен возвращать состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=.*/
		URI url = URI.create(URL + "/load/" + key + "?API_TOKEN=" + apiToken);
		HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}
}
