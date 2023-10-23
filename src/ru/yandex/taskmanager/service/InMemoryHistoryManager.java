package ru.yandex.taskmanager.service;

import ru.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
	Map<Integer, Node<Task>> hashMap = new HashMap<>();
	private Node<Task> head;
	private Node<Task> tail;

	private void removeNode(Node<Task> node) {
		if (head == node) {
			head = node.next;
		}
		if (tail == node) {
			tail = node.prev;
		}
		if (node.prev != null) {
			node.prev.next = node.next;
		}
		if (node.next != null) {
			node.next.prev = node.prev;
		}
	}

	private void linkLast(Task task) {
		Node<Task> node = new Node<>(task);
		if (this.head == null) {
			this.head = node;
		}

		if (this.tail != null) {
			node.prev = this.tail;
			this.tail.next = node;
		}
		this.tail = node;

		hashMap.put(task.getId(), node);
	}

	private List<Task> getTasks() {
		List<Task> arrayList = new ArrayList<>();
		Node<Task> node = head;
		if (node != null) {
			arrayList.add(node.task);
			while (node != tail) {
				arrayList.add(node.next.task);
				node = node.next;
			}
		}
		return List.copyOf(arrayList);
	}

	@Override
	public List<Task> getHistory() {
		return getTasks();
	}

	@Override
	public void add(Task task) {
		Node<Task> node = hashMap.get(task.getId());
		if (node != null) {
			removeNode(node);
		}
		linkLast(task);
	}

	@Override
	public void remove(int id) {
		Node<Task> node = hashMap.get(id);
		if (node != null) {
			removeNode(node);
			hashMap.remove(node.task.getId());
		}
	}
}
