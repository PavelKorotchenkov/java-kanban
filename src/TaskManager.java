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
    //Или такой проблемы в принципе здесь не должно возникнуть?
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



