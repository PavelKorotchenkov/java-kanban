#**Sprint-3 (Трекер задач)**
JAVA-KANBAN
-
 Программа позволяет добавлять, хранить, обновлять и удалять задачи.
 Задачи могут быть 3-х типов: 
* простая задача, 
* эпик задача,
* подзадача.
 
Эпик задача содержит в себе подзадачи. Статус эпик задачи зависит от статусов её подзадач.   
В классе ru.yandex.taskmanager.Main дан пример работы с программой:
```java
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Покормить кошку утром","30 гр. корма");
        taskManager.createNewTask(task1);
        Task task2 = new Task("Покормить кошку вечером","40 гр. корма");
        taskManager.createNewTask(task2);

        Epictask epictask1 = new Epictask("Epic 1", "Get a new Job");
        taskManager.createNewEpictask(epictask1);
        Subtask subtask1 = new Subtask("Learn Java", "Finish Yandex Practicum", epictask1.getId());
        taskManager.createNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Pass Interview", "Prepare for interviews", epictask1.getId());
        taskManager.createNewSubtask(subtask2);

        Epictask epictask2 = new Epictask("Epic 2", "Buy a house");
        taskManager.createNewEpictask(epictask2);
        Subtask subtask3 = new Subtask("Money", "Get enough money", epictask2.getId());
        taskManager.createNewSubtask(subtask3);

        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getEpictasksList());
        System.out.println(taskManager.getSubtasksList());
        System.out.println();
```

©[Yandex-Practicum](https://practicum.yandex.ru/ "Онлайн курс по Java")