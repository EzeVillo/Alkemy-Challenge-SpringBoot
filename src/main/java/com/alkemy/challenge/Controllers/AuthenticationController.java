package com.alkemy.challenge.Controllers;

import com.alkemy.challenge.DTOs.Auth.Jwt.JwtDTO;
import com.alkemy.challenge.DTOs.Auth.Users.CreateUserDTO;
import com.alkemy.challenge.DTOs.Auth.Users.LoginUserDTO;
import com.alkemy.challenge.Error.Errors.ApiError;
import com.alkemy.challenge.Error.Errors.EmailError;
import com.alkemy.challenge.Error.Exceptions.Emails.EmailException;
import com.alkemy.challenge.Security.Interfaces.JwtTokenProvider;
import com.alkemy.challenge.Services.Interfaces.EmailService;
import com.alkemy.challenge.Services.Interfaces.UserService;
import com.alkemy.challenge.Utils.Validate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;
	private final EmailService emailSender;
	@Value("${email}")
	private String email;

	/**
	 * 10. Autenticación de Usuarios
	 * 
	 * @param loginUserDTO
	 * @return status code 200 and token
	 */
	@ApiOperation(value = "login a user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = JwtDTO.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
	})
	@PostMapping("login")
	public ResponseEntity<JwtDTO> login(@RequestBody LoginUserDTO loginUserDTO) {
		return ResponseEntity.ok(getToken(loginUserDTO));
	}

	/**
	 * 10. Autenticación de Usuarios
	 * 
	 * @param createUserDTO
	 * @return status code 201 and token
	 */
	@ApiOperation(value = "register a user and send an email")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = JwtDTO.class),
			@ApiResponse(code = 201, message = "Created", response = EmailError.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
	})
	@PostMapping("register")
	public ResponseEntity<JwtDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
		userService.createUser(createUserDTO);
		JwtDTO jwtDTO = getToken(new LoginUserDTO(createUserDTO.getUsername(), createUserDTO.getPassword()));

		// 11. Envío de emails
		try {
			emailSender.send(createUserDTO.getEmail(), email);
		} catch (Exception ex) {
			throw new EmailException(jwtDTO);
		}

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(jwtDTO);
	}

	private JwtDTO getToken(LoginUserDTO loginUserDTO) {
		Validate<LoginUserDTO> validacion = new Validate<LoginUserDTO>();
		validacion.validate(loginUserDTO);

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginUserDTO.getUsername(),
						loginUserDTO.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwtDTO = tokenProvider.generateToken(authentication);

		return new JwtDTO(jwtDTO);
	}
}
