package ru.yandex.taskmanager.model;

import java.util.ArrayList;

public class Epictask extends Task {

    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epictask(String name, String description) {
        super(name, description);
    }

    public void add(Subtask subtask) {
        subtasks.add(subtask);
    }

    //refactor: поменять видимость поля на private, доступ осуществляется через геттер
    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
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
