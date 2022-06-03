package com.alkemy.challenge.Error.Exceptions.Movies;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;

    public MovieNotFoundException(long id) {
        super("Could not find character with ID: " + id);
    }

    public MovieNotFoundException(String msg) {
        super(msg);
    }
}
