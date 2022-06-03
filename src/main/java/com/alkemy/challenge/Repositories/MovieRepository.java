package com.alkemy.challenge.Repositories;

import java.util.Optional;

import com.alkemy.challenge.Entities.Movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    Optional<Movie> findByTitle(String title);
}
