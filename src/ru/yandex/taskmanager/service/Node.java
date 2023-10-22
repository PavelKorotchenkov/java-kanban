package ru.yandex.taskmanager.service;

//feat: добавить класс Node - узел списка
public class Node<Task> {
	Task task;
	Node<Task> next;
	Node<Task> prev;

	public Node(Task task) {
		this.task = task;
		this.next = null;
		this.prev = null;
	}
}
