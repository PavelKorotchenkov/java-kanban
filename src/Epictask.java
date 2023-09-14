import java.util.ArrayList;

public class Epictask extends Task {
    protected ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epictask(String name, String description) {
        super(name, description);
    }

    //метод для добавления подзадачи в список подзадач, хранимых в эпикзадаче (применяется в конструкторе класса Subtask)
    protected void add(Subtask subtask) {
        subtasks.add(subtask);
    }

    private ArrayList<Integer> printSubtasksId() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            ids.add(subtask.getId());
        }
        return ids;
    }

    @Override
    public String toString() {
        return "EpicTask: " + getName() + "=\""
                + getDescription() + "\", status=" + getStatus() + ", id=" + getId()
                + ", subtasks=" + printSubtasksId() +
                '}';
    }
}
