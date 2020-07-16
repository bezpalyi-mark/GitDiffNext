package com.diffreviewer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MergeRequestNotFoundException extends ResponseStatusException {

    public MergeRequestNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Merge request with id " + id + " was not found");
    }
}
