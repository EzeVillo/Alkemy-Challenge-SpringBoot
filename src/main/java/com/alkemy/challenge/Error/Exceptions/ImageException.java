package com.alkemy.challenge.Error.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImageException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;

    public ImageException(String msg) {
        super(msg);
    }
}
