package com.alkemy.challenge.Repositories;

import java.util.Optional;

import com.alkemy.challenge.Entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
