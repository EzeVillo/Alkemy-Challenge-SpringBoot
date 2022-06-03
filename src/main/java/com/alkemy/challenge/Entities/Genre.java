package com.alkemy.challenge.Entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Nullable
    private String image;

    @NonNull
    @Column(unique = true)
    private String name;

    @Builder.Default
    @OneToMany()
    @JoinColumn(name = "movie_id")
    private Set<Movie> movies = new HashSet<>();
}
