package com.alkemy.challenge.Error.Exceptions.Characters;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CharacterNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;

    public CharacterNotFoundException(long id) {
        super("Could not find character with ID: " + id);
    }

    public CharacterNotFoundException(String msg) {
        super(msg);
    }
}
