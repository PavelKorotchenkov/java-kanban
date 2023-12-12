package ru.yandex.taskmanager.util;

import ru.yandex.taskmanager.api.KVServer;
import ru.yandex.taskmanager.service.*;

import java.io.IOException;

public class Managers {
	public static HttpTaskManager getDefault() throws IOException, InterruptedException {
		return new HttpTaskManager("http://localhost:8078/");
	}

	public static HttpTaskManager getDefault(String path) throws IOException, InterruptedException {
		return new HttpTaskManager(path);
	}

	public static InMemoryTaskManager getInMemoryTaskManager() {
		return new InMemoryTaskManager();
	}

	public static FileBackedTasksManager getFileBackedTaskManager(String path) {
		return new FileBackedTasksManager(path);
	}

	public static HistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();
	}

	public static KVServer getDefaultKVServer() throws IOException {
		return new KVServer();
	}
}
