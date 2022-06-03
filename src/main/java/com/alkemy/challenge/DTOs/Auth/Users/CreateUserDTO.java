package com.alkemy.challenge.DTOs.Auth.Users;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserDTO {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String password2;

	@NotBlank
	@Email
	private String email;
}
