#**Sprint-3 (Трекер задач)**
JAVA-KANBAN
-
 Программа позволяет добавлять, хранить, обновлять и удалять задачи.
 Задачи могут быть 3-х типов: 
* простая задача, 
* эпик задача,
* подзадача.
 
Эпик задача содержит в себе подзадачи. Статус эпик задачи зависит от статусов её подзадач.   
В классе Main дан пример работы с программой:
```java
TaskManager taskManager = new TaskManager();

		Epictask epictask1 = new Epictask("Epic 1", "Get a new Job");
		Subtask subtask1 = new Subtask("Learn Java", "Finish Yandex Practicum", epictask1);
		Subtask subtask2 = new Subtask("Pass Interview", "Prepare for interviews", epictask1);

		Epictask epictask2 = new Epictask("Epic 2", "Buy a house");
		Subtask subtask3 = new Subtask("Money", "Get enough money", epictask2);

        System.out.println("Создаём и выводим на экран все задачи:");
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        taskManager.createNewEpictask(epictask1);
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        taskManager.createNewEpictask(epictask2);
        taskManager.createNewSubtask(subtask3);

        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getEpictasksList());
        System.out.println(taskManager.getSubtasksList());
```

©[Yandex-Practicum](https://practicum.yandex.ru/ "Онлайн курс по Java")