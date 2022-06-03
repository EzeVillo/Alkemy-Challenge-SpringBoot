package com.alkemy.challenge.Error.Exceptions.Genres;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GenreNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;

    public GenreNotFoundException(long id) {
        super("Could not find genre with ID: " + id);
    }

    public GenreNotFoundException(String msg) {
        super(msg);
    }
}
