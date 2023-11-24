package ru.yandex.taskmanager.util;

import ru.yandex.taskmanager.service.*;

public class Managers {
	public static TaskManager getDefault(){
		return new FileBackedTasksManager("./resources/saved.csv");
	}

	public static TaskManager getDefault(String path){
		return new FileBackedTasksManager(path);
	}

	public static TaskManager getInMemoryTaskManager(){
		return new InMemoryTaskManager();
	}

	public static HistoryManager getDefaultHistory(){
		return new InMemoryHistoryManager();
	}
}
