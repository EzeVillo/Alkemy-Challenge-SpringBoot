package com.alkemy.challenge.Error.Errors;

import com.alkemy.challenge.DTOs.Auth.Jwt.JwtDTO;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NonNull;

public class EmailError extends ApiError {

    @Getter
    @NonNull
    private JwtDTO jwtDTO;

    public EmailError(HttpStatus httpStatus, String msg, JwtDTO jwtDTO) {
        super(httpStatus, msg);
        this.jwtDTO = jwtDTO;
    }
}
