package com.alkemy.challenge.Error.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;

    public ValidationException(String msg) {
        super(msg);
    }
}
