package com.diffreviewer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TaskNotFoundException extends ResponseStatusException {
    public TaskNotFoundException(String taskName) {
        super(HttpStatus.NOT_FOUND, "Task with name " + taskName + " was not found");
    }
}
