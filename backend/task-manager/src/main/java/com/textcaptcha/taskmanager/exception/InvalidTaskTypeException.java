package com.textcaptcha.taskmanager.exception;

import com.textcaptcha.data.model.task.TaskType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

public class InvalidTaskTypeException extends ResponseStatusException {

    private static final String reason = "Invalid task type. Valid task types are: " + Arrays.toString(TaskType.values());

    public InvalidTaskTypeException(Exception e) {
        super(HttpStatus.BAD_REQUEST, reason, e);
    }

    public InvalidTaskTypeException() {
        super(HttpStatus.BAD_REQUEST, reason);
    }

}
