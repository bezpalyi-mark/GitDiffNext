package com.diffreviewer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchMergeRequestOnGitTea extends ResponseStatusException {
    public NoSuchMergeRequestOnGitTea() {
        super(HttpStatus.BAD_REQUEST, "No such merge request on Git Tea");
    }
}
