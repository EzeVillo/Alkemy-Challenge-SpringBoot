package com.alkemy.challenge.Security;

import java.util.Date;
import java.util.stream.Collectors;

import com.alkemy.challenge.Entities.User;
import com.alkemy.challenge.Entities.UserRole;
import com.alkemy.challenge.Error.Exceptions.Auth.Jwt.JwtException;
import com.alkemy.challenge.Security.Interfaces.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider{

	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String TOKEN_TYPE = "JWT";

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.token-expiration}")
	private int jwtDurationTokenInSeconds;

	public String generateToken(Authentication authentication) {

		User user = (User) authentication.getPrincipal();

		Date tokenExpirationDate = new Date(System.currentTimeMillis() + (jwtDurationTokenInSeconds * 1000));

		return Jwts.builder()
				.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
				.setHeaderParam("typ", TOKEN_TYPE)
				.setSubject(Long.toString(user.getId()))
				.setIssuedAt(new Date())
				.setExpiration(tokenExpirationDate)
				.claim("username", user.getUsername())
				.claim("roles", user.getRoles().stream()
						.map(UserRole::name)
						.collect(Collectors.joining(", ")))
				.compact();
	}

	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
				.parseClaimsJws(token)
				.getBody();

		return Long.parseLong(claims.getSubject());

	}

	public boolean validateToken(String authToken) {

		try {
			Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			throw new JwtException("JWT token signing failed: " + ex.getMessage());
		} catch (MalformedJwtException ex) {
			throw new JwtException("Malformed token: " + ex.getMessage());
		} catch (ExpiredJwtException ex) {
			throw new JwtException("The token has expired: " + ex.getMessage());
		} catch (UnsupportedJwtException ex) {
			throw new JwtException("JWT token not supported: " + ex.getMessage());
		} catch (IllegalArgumentException ex) {
			throw new JwtException("Empty JWT claims");
		}
	}

}
