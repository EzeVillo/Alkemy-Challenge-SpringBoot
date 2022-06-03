package com.alkemy.challenge.DTOs.Movies;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MovieFullDTO {
    private long id;
    private String image;
    private String title;
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate creationDate;
    private int score;
    private Set<String> characters;
    private String genre;
}
