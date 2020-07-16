package com.diffreviewer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchStatusException extends ResponseStatusException {
    public NoSuchStatusException(String statusName) {
        super(HttpStatus.BAD_REQUEST, "No such status with name " + statusName);
    }
}
