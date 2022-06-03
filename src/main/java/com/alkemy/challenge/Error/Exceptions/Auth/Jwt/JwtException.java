package com.alkemy.challenge.Error.Exceptions.Auth.Jwt;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JwtException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;

    public JwtException(String msg) {
        super(msg);
    }
}
