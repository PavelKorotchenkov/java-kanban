package ru.yandex.taskmanager;

import ru.yandex.taskmanager.api.KVServer;
import ru.yandex.taskmanager.api.KVTaskClient;
import ru.yandex.taskmanager.model.*;
import ru.yandex.taskmanager.service.TaskManager;
import ru.yandex.taskmanager.util.Managers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Sprint 8
 * author: Pavel Korotchenkov
 * created 27.09.2023
 * upd 09.12.2023
 * ver. 1.6
 */

public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		new KVServer().start();
		KVTaskClient client = new KVTaskClient(8078);
		/*client.put("mykey1", "some stuff");
		Thread.sleep(1000);
		String value = client.load("mykey1");
		System.out.println(value);*/

	}
}
