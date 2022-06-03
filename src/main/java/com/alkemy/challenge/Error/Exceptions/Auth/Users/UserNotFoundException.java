package com.alkemy.challenge.Error.Exceptions.Auth.Users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;
}