package ru.yandex.taskmanager.util;

import ru.yandex.taskmanager.service.HistoryManager;
import ru.yandex.taskmanager.service.InMemoryHistoryManager;
import ru.yandex.taskmanager.service.InMemoryTaskManager;
import ru.yandex.taskmanager.service.TaskManager;

public class Managers {
	public static TaskManager getDefault(){
		return new InMemoryTaskManager();
	}

	public static HistoryManager getDefaultHistory(){
		return new InMemoryHistoryManager();
	}
}
