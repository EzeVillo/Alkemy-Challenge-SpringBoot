package com.alkemy.challenge.Error.Exceptions.Movies;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class MovieNoContentException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;

    public MovieNoContentException() {
        super();
    }
}
