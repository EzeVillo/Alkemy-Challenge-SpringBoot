package com.alkemy.challenge.DTOs.Movies;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alkemy.challenge.DTOs.Characters.CreateCharacterDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMovieDTO {

    @NotBlank
    private String title;

    @NotNull
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate creationDate;

    @NotNull
    @Min(value = 1, message = "The value must be an integer greater than 0")
    @Max(value = 5, message = "The value must be an integer less than 6")
    private Integer score;

    private Set<CreateCharacterDTO> characters;

    @NotNull
    @Min(value = 1, message = "The value must be an integer greater than 0")
    private Long genreId;
}
