package com.alkemy.challenge.Error;

import com.alkemy.challenge.Error.Errors.ApiError;
import com.alkemy.challenge.Error.Errors.EmailError;
import com.alkemy.challenge.Error.Exceptions.ImageException;
import com.alkemy.challenge.Error.Exceptions.ValidationException;
import com.alkemy.challenge.Error.Exceptions.Auth.Jwt.JwtException;
import com.alkemy.challenge.Error.Exceptions.Auth.Users.UserNotFoundException;
import com.alkemy.challenge.Error.Exceptions.Auth.Users.UsernameIsAlreadyInUseException;
import com.alkemy.challenge.Error.Exceptions.Characters.CharacterNameIsAlreadyInUseException;
import com.alkemy.challenge.Error.Exceptions.Characters.CharacterNotFoundException;
import com.alkemy.challenge.Error.Exceptions.Emails.EmailException;
import com.alkemy.challenge.Error.Exceptions.Genres.GenreNameIsAlreadyInUseException;
import com.alkemy.challenge.Error.Exceptions.Genres.GenreNotFoundException;
import com.alkemy.challenge.Error.Exceptions.Movies.MovieNotFoundException;
import com.alkemy.challenge.Error.Exceptions.Movies.MovieTitleIsAlreadyInUseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CharacterNotFoundException.class)
	public ResponseEntity<ApiError> handleCharacterNotFound(CharacterNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(MovieNotFoundException.class)
	public ResponseEntity<ApiError> handleMovieNotFound(MovieNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(GenreNotFoundException.class)
	public ResponseEntity<ApiError> handleGenreNotFound(GenreNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ApiError> handleValidation(ValidationException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(ImageException.class)
	public ResponseEntity<ApiError> handleImage(ImageException ex) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

	@ExceptionHandler(JwtException.class)
	public ResponseEntity<ApiError> handleJwt(JwtException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(UsernameIsAlreadyInUseException.class)
	public ResponseEntity<ApiError> handleUsernameIsAlreadyInUse(UsernameIsAlreadyInUseException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(CharacterNameIsAlreadyInUseException.class)
	public ResponseEntity<ApiError> handleCharacterNameIasAlreadyInUse(CharacterNameIsAlreadyInUseException ex){
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(MovieTitleIsAlreadyInUseException.class)
	public ResponseEntity<ApiError> handleMovieTitleIasAlreadyInUse(MovieTitleIsAlreadyInUseException ex){
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(GenreNameIsAlreadyInUseException.class)
	public ResponseEntity<ApiError> handleGenreNameIasAlreadyInUse(GenreNameIsAlreadyInUseException ex){
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(EmailException.class)
	public ResponseEntity<EmailError> handleEmail(EmailException ex) {
		EmailError emailError = new EmailError(HttpStatus.CREATED, ex.getMessage(), ex.getJwtDTO());
		return ResponseEntity.status(HttpStatus.CREATED).body(emailError);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiError> handleAccess(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleEmail(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}
}
