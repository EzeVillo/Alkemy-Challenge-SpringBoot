package com.alkemy.challenge.Error.Exceptions.Emails;

import com.alkemy.challenge.DTOs.Auth.Jwt.JwtDTO;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EmailException extends RuntimeException {
    private static final long serialVersionUID = 43876691117560211L;
    @Getter
    private JwtDTO jwtDTO;

    public EmailException(JwtDTO jwtDTO) {
        super("the email could not be sent correctly, but the account has been created successfully");
        this.jwtDTO = jwtDTO;
    }
}

