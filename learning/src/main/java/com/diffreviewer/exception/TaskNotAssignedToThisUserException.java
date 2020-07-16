package com.diffreviewer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TaskNotAssignedToThisUserException extends ResponseStatusException {
    public TaskNotAssignedToThisUserException(String taskName, String username) {
        super(HttpStatus.BAD_REQUEST, "Task " + taskName + " was not assigned to user with name " + username);
    }
}
