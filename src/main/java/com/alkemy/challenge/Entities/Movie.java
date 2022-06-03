package com.alkemy.challenge.Entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

// 1. Modelado de Base de Datos
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Nullable
    private String image;

    @NonNull
    @Column(unique = true)
    private String title;

    @NonNull
    private LocalDate creationDate;

    @Min(value = 1, message = "The value must be an integer greater than 0")
    @Max(value = 5, message = "The value must be an integer less than 6")
    private int score;

    @ManyToMany
    @JoinTable(name = "movie_character", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "charater_id"))
    @Builder.Default
    private Set<Character> characters = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
}
