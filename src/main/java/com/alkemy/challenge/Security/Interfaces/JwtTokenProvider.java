package com.alkemy.challenge.Security.Interfaces;

import org.springframework.security.core.Authentication;

public interface JwtTokenProvider {
    
    public String generateToken(Authentication authentication);

    public Long getUserIdFromJWT(String token);

    public boolean validateToken(String authToken);
}
