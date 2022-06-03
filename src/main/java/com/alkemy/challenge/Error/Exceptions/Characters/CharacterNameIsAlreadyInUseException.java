package com.alkemy.challenge.Error.Exceptions.Characters;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CharacterNameIsAlreadyInUseException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;

    public CharacterNameIsAlreadyInUseException(){
        super("The name is already in use");
    } 
}
