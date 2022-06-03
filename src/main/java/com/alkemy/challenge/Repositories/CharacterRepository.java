package com.alkemy.challenge.Repositories;

import java.util.Optional;

import com.alkemy.challenge.Entities.Character;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long>, JpaSpecificationExecutor<Character> {
    Optional<Character> findByName(String name);
}
