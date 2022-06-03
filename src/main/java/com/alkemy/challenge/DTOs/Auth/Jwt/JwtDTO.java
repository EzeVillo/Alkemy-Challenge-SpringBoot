package com.alkemy.challenge.DTOs.Auth.Jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDTO {

	private String token;

	public JwtDTO(String token) {
		this.token = token;
	}

}
