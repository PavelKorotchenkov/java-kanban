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

    public ArrayList<Task> getEpictasksList() {
        return new ArrayList<>(epictasks.values());
    }

    public ArrayList<Task> getSubtasksList() {
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
        subtasks.clear();
        return true;
    }

    //А если кто-то попытается в Main вызвать метод у класса-наследника, которого нет в классе-родителе?
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
    //вопрос - а если пользователь будет вызывать, например, createNewTask, а передавать туда эпик или подзадачу, или наоборт? Как этого избежать?
    //Или такой проблемы здесь не должно возникнуть в принципе?
    //доп.вопрос - это ок, что id начинаются с 1, или лучше с 0 начинать?)
    public Task createNewTask(Task task) {
        task.setId(++taskId);
        tasks.put(taskId, task);
        return task;
    }

    public Task createNewEpictask(Epictask task) {
        task.setId(++taskId);
        epictasks.put(taskId, task);
        return task;
    }

    public Task createNewSubtask(Subtask task) {
        task.setId(++taskId);
        subtasks.put(taskId, task);
        checkStatus(task.getEpicTask());
        return task;
    }

    //fixed: разбить метод обновления задачи на 3 метода
    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }

        return task;
    }

    public Task updateEpictask(Epictask task) {
        if (epictasks.containsKey(task.getId())) {
            epictasks.put(task.getId(), task);
        }

        return task;
    }

    public Task updateSubtask(Subtask task) {
        if (subtasks.containsKey(task.getId())) {
            subtasks.put(task.getId(), task);
            checkStatus(task.getEpicTask());
        }

        return task;
    }

    private void checkStatus(Epictask task) {
        int subtasksAmount = getSubtasks(task.getId()).size();
        int countProgress = 0;
        int countDone = 0;
        for (Subtask t : getSubtasks(task.getId())) {
            if (t.getStatus().equals(Status.IN_PROGRESS)) {
                countProgress++;
            } else if (t.getStatus().equals(Status.DONE)) {
                countDone++;
            }
        }

        if (countProgress == 0 && countDone == 0) {
            task.setStatus(Status.NEW);
            return;
        }

        if (countDone == subtasksAmount) {
            task.setStatus(Status.DONE);
            return;
        }

        task.setStatus(Status.IN_PROGRESS);
    }

    //fixed: разбить общий метод удаления на 3 отдельных
    public Task deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.remove(taskId);
        }

        return null;
    }

    public Task deleteEpictaskById(int taskId) {
        if (epictasks.containsKey(taskId)) {
            Epictask epictask = epictasks.remove(taskId);

            for (Subtask subtask : epictask.subtasks) {
                subtasks.remove(subtask.getId());
            }

            return epictask;
        }

        return null;
    }

    public Task deleteSubtaskById(int taskId) {
        if (subtasks.containsKey(taskId)) {
            Subtask subtask = subtasks.remove(taskId);
            checkStatus(subtask.getEpicTask());
            getSubtasks(subtask.getEpicTask().getId()).remove(subtask);

            return subtask;
        }

        return null;
    }

    public ArrayList<Subtask> getSubtasks(int taskId) {
        if (epictasks.containsKey(taskId)) {
            return new ArrayList<>(epictasks.get(taskId).subtasks);
        }

        return null;
    }
}



