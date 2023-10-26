package ru.yandex.taskmanager.service;

public class Node<Task> {
	Task task;
	Node<Task> next;
	Node<Task> prev;

	public Node(Node prev, Task task) {
		this.task = task;
		this.prev = prev;
		this.next = null;
	}
}
