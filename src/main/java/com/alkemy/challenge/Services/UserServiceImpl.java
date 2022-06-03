package com.alkemy.challenge.Services;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.alkemy.challenge.DTOs.Auth.Users.CreateUserDTO;
import com.alkemy.challenge.Entities.User;
import com.alkemy.challenge.Entities.UserRole;
import com.alkemy.challenge.Error.Exceptions.ValidationException;
import com.alkemy.challenge.Error.Exceptions.Auth.Users.UserNotFoundException;
import com.alkemy.challenge.Error.Exceptions.Auth.Users.UsernameIsAlreadyInUseException;
import com.alkemy.challenge.Repositories.UserRepository;
import com.alkemy.challenge.Services.Interfaces.UserService;
import com.alkemy.challenge.Utils.Validate;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void createUser(CreateUserDTO createUserDTO) {

        Validate<CreateUserDTO> validacion = new Validate<CreateUserDTO>();
        validacion.validate(createUserDTO);

        if(userRepository.findByUsername(createUserDTO.getUsername()).isPresent()){
            throw new UsernameIsAlreadyInUseException();
        }

        if (!createUserDTO.getPassword().contentEquals(createUserDTO.getPassword2()))
            throw new ValidationException("Passwords do not match");

        User user = User.builder()
                .username(createUserDTO.getUsername())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .email(createUserDTO.getEmail())
                .roles(Stream.of(UserRole.USER).collect(Collectors.toSet())).build();

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());
    }

}
