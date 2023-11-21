package ru.yandex.taskmanager.util;

import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.service.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileStringConverter {
	public static String taskToString(Task task) {
		String taskAsString = task.getId() + ","
				+ task.getType() + ","
				+ task.getName() + ","
				+ task.getStatus() + ","
				+ task.getDescription() + ","
				+ task.getStartTime() + ","
				+ task.getDuration() + ",";

		//refactor
		if (task.getType().equals(TaskType.SUBTASK)) {
			int epicId = ((Subtask) task).getEpicTaskId();
			taskAsString = taskAsString + epicId;
		}

		return taskAsString + "\n";
	}

	public static Task taskFromString(String value) {
		String[] attribute = value.split(",");
		Task task = null;

		if (attribute[1].equals(TaskType.TASK.name())) {
			//task = new Task(attribute[2], attribute[4], LocalDateTime.parse(attribute[5]), Duration.parse(attribute[6]));
			task = new Task(attribute[2], attribute[4]);
			if (!attribute[5].equals("null")) {
				task.setStartTime(LocalDateTime.parse(attribute[5]));
				task.setDuration(Duration.parse(attribute[6]));
			}
		}

		if (attribute[1].equals(TaskType.EPIC.name())) {
			task = new Epictask(attribute[2], attribute[4]);

			if (!attribute[5].equals("null")) {
				Epictask epictask = (Epictask) task;
				epictask.setStartTime(LocalDateTime.parse(attribute[5]));
				epictask.setDuration(Duration.parse(attribute[6]));
				epictask.setEndTime(epictask.getStartTime().plus(epictask.getDuration()));
			}
		}

		if (attribute[1].equals(TaskType.SUBTASK.name())) {
			//task = new Subtask(attribute[2], attribute[4], LocalDateTime.parse(attribute[5]), Duration.parse(attribute[6]), Integer.parseInt(attribute[7]));
			task = new Subtask(attribute[2], attribute[4], Integer.parseInt(attribute[7]));
			if (!attribute[5].equals("null")) {
				task.setStartTime(LocalDateTime.parse(attribute[5]));
				task.setDuration(Duration.parse(attribute[6]));
			}
		}

		task.setId(Integer.parseInt(attribute[0]));
		task.setStatus(Status.valueOf(attribute[3]));

		return task;
	}

	public static String historyToString(HistoryManager manager) {
		List<Task> history = manager.getHistory();
		if (history.isEmpty()) {
			return "";
		}
		String[] ids = new String[history.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = String.valueOf(history.get(i).getId());
		}

		return String.join(",", ids);
	}

	public static List<Integer> historyFromString(String value) {
		List<Integer> history = new ArrayList<>();
		String[] array = value.split(",");
		for (String s : array) {
			history.add(Integer.parseInt(s));
		}

		return history;
	}
}
