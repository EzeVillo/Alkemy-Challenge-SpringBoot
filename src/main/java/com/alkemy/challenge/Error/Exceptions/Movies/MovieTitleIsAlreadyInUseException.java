package com.alkemy.challenge.Error.Exceptions.Movies;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MovieTitleIsAlreadyInUseException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;

    public MovieTitleIsAlreadyInUseException(){
        super("The title is already in use");
    } 
}
