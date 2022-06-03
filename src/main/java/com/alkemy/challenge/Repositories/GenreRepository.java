package com.alkemy.challenge.Repositories;

import java.util.Optional;

import com.alkemy.challenge.Entities.Genre;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
