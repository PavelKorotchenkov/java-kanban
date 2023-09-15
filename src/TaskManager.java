import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int taskId = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epictask> epictasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    //fixed: разбить общий метод вывода задач на 3 отдельных
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epictask> getEpictasksList() {
        return new ArrayList<>(epictasks.values());
    }

    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    //fixed: разбить общий метод удаления задач на 3 отдельных
    public boolean clearTasks() {
        tasks.clear();
        return true;
    }

    public boolean clearEpictasks() {
        epictasks.clear();
        return true;
    }

    public boolean clearSubtasks() {
        for (Epictask epictask : epictasks.values()) {
            epictask.subtasks.clear();
            //проверка и обновленение статуса эпика
            checkStatus(epictask);
        }

        subtasks.clear();
        return true;
    }

    public Task getTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        }

        if (epictasks.containsKey(taskId)) {
            return epictasks.get(taskId);
        }

        if (subtasks.containsKey(taskId)) {
            return subtasks.get(taskId);
        }

        return null;
    }

    //fixed: разбить общий метод создания задач на 3 отдельных
    //Вопрос - а если пользователь будет вызывать, например, createNewTask, а передавать туда эпик или подзадачу?
    //Идея пропускает, но логика добавения нарушается.
    public Task createNewTask(Task task) {
        task.setId(++taskId);
        tasks.put(taskId, task);
        return task;
    }

    public Epictask createNewEpictask(Epictask task) {
        task.setId(++taskId);
        epictasks.put(taskId, task);
        return task;
    }

    public Subtask createNewSubtask(Subtask task) {
        task.setId(++taskId);
        subtasks.put(taskId, task);
        checkStatus(task.getEpicTask());
        return task;
    }

    //fixed: разбить метод обновления задачи на 3 метода.
    //refactor: поменять возвращаемый тип на boolean
    public boolean updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            return true;
        }

        return false;
    }

    public boolean updateEpictask(Epictask task) {
        if (epictasks.containsKey(task.getId())) {
            epictasks.put(task.getId(), task);
            return true;
        }

        return false;
    }

    public boolean updateSubtask(Subtask task) {
        if (subtasks.containsKey(task.getId())) {
            subtasks.put(task.getId(), task);
            checkStatus(task.getEpicTask());
            return true;
        }

        return false;
    }

    //feat: добавлен метод обновления статуса у эпика + fix
    private void checkStatus(Epictask task) {
        int subtasksAmount = getSubtasks(task.getId()).size();
        int countNew = 0;
        int countDone = 0;
        for (Subtask t : getSubtasks(task.getId())) {
            if (t.getStatus().equals(Status.NEW)) {
                countNew++;
            } else if (t.getStatus().equals(Status.DONE)) {
                countDone++;
            }
        }

        if (countNew == subtasksAmount) {
            task.setStatus(Status.NEW);
            return;
        }

        if (countDone == subtasksAmount) {
            task.setStatus(Status.DONE);
            return;
        }

        task.setStatus(Status.IN_PROGRESS);
    }

    //fixed: разбить общий метод удаления по ID на 3 отдельных
    public Task deleteTaskById(int taskId) {
        return tasks.remove(taskId);
    }

    public Epictask deleteEpictaskById(int taskId) {
        Epictask epictask = epictasks.remove(taskId);

        for (Subtask subtask : epictask.subtasks) {
            subtasks.remove(subtask.getId());
        }

        return epictask;
    }

    public Subtask deleteSubtaskById(int taskId) {
        Subtask subtask = subtasks.remove(taskId);
        Epictask epictask = subtask.getEpicTask();
        checkStatus(epictask);
        epictask.subtasks.remove(subtask);

        return subtask;
    }

    public ArrayList<Subtask> getSubtasks(int taskId) {
        return new ArrayList<>(epictasks.get(taskId).subtasks);
    }
}



