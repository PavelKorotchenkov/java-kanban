package ru.yandex.taskmanager.exception;

public class StartEndTimeConflictException extends RuntimeException {
	public StartEndTimeConflictException(String message) {
		super(message);
	}
}
