package com.alkemy.challenge.DTOs.Auth.Users;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO {

	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
