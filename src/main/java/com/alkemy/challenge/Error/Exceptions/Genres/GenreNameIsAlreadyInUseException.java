package com.alkemy.challenge.Error.Exceptions.Genres;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GenreNameIsAlreadyInUseException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;

    public GenreNameIsAlreadyInUseException(){
        super("The name of genre is already in use");
    } 
}
