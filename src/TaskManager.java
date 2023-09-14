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

    public boolean clearAllTasks() {
        tasks.clear();
        epictasks.clear();
        subtasks.clear();
        return true;
    }

    //Я так понял, этот метод должен применяться внутри других методов, но честно говоря не очень понял, как правильно это здесь реализовать.
    //Если делать свой отдельный метод под каждый тип задачи, то более-менее понятно.
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

    public Task createNewTask(Task task) {
        task.setId(++taskId);
        if (task instanceof Epictask) {
            epictasks.put(taskId, (Epictask) task);
        } else if (task instanceof Subtask) {
            subtasks.put(taskId, (Subtask) task);
        } else {
            tasks.put(taskId, task);
        }

        return task;
    }

    public Task updateTask(int taskId, Task task) {
        if (tasks.containsKey(taskId)) {
            task.setId(taskId);
            tasks.put(taskId, task);
        }

        if (epictasks.containsKey(taskId)) {
            task.setId(taskId);
            epictasks.put(taskId, (Epictask) task);
        }

        if (subtasks.containsKey(taskId)) {
            task.setId(taskId);
            subtasks.put(taskId, (Subtask) task);

            Subtask subtask = (Subtask) task;
            Epictask epictask = subtask.getEpicTask();
            int subtasksAmount = getSubtasks(epictask).size();
            int count = 0;
            for (Subtask t : getSubtasks(epictask)) {
                if (t.getStatus().equals(Status.NEW)) {
                    count++;
                }
            }

            if (count > 0 && count < subtasksAmount) {
                epictask.setStatus(Status.IN_PROGRESS);
            } else if (count == 0 && subtasksAmount != 0) {
                epictask.setStatus(Status.DONE);
            }
        }

        return task;
    }

    public Task deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.remove(taskId);
        }

        if (epictasks.containsKey(taskId)) {
            Epictask epictask = epictasks.remove(taskId);

            for (Subtask subtask : epictask.subtasks) {
                subtasks.remove(subtask.getId());
            }

            return epictask;
        }

        if (subtasks.containsKey(taskId)) {
            Subtask subtask = subtasks.remove(taskId);
            getSubtasks(subtask.getEpicTask()).remove(subtask);

            return subtask;
        }

        return null;
    }

    public ArrayList<Subtask> getSubtasks(Epictask epicTask) {
        if (epictasks.containsKey(epicTask.getId())) {
            return epictasks.get(epicTask.getId()).subtasks;
        }

        return null;
    }
}



