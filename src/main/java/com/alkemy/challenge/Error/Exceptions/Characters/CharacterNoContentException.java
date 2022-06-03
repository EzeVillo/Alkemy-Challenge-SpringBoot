package com.alkemy.challenge.Error.Exceptions.Characters;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class CharacterNoContentException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;

    public CharacterNoContentException() {
        super();
    }
}
