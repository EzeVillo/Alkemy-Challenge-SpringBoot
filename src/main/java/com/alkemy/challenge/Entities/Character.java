package com.alkemy.challenge.Entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

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
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Nullable
    private String image;

    @NonNull
    @Column(unique = true)
    private String name;

    private int age;

    private float weight;

    @NonNull
    @Column(columnDefinition = "TEXT")
    private String history;

    @Builder.Default
    @ManyToMany(mappedBy = "characters")
    private Set<Movie> movies = new HashSet<>();
}
