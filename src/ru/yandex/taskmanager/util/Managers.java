package ru.yandex.taskmanager.util;

import ru.yandex.taskmanager.service.*;

public class Managers {
	public static TaskManager getDefault(){
		return new FileBackedTasksManager("./resources/saved.csv");
	}
	public static HistoryManager getDefaultHistory(){
		return new InMemoryHistoryManager();
	}
}
