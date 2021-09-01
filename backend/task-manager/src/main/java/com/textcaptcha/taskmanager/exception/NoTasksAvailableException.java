package com.textcaptcha.taskmanager.exception;

public class NoTasksAvailableException extends TaskSelectionException {
    public NoTasksAvailableException() {
        super("No tasks available");
    }
}
