package ru.yandex.taskmanager.util;

import ru.yandex.taskmanager.service.InMemoryTaskManager;
import ru.yandex.taskmanager.service.TaskManager;

public class Managers {
	public TaskManager getDefault(){
		return new InMemoryTaskManager();
	}
}
