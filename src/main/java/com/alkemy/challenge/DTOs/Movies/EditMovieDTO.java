package com.alkemy.challenge.DTOs.Movies;

import java.time.LocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMovieDTO {

    @Nullable
    private String title;

    @Nullable
    private LocalDate creationDate;

    @Nullable
    @Min(value = 1, message = "The value must be an integer greater than 0")
    @Max(value = 5, message = "The value must be an integer less than 6")
    private Integer score;

    @Nullable
    @Min(value = 1, message = "The value must be an integer greater than 0")
    private Long genreId;
}
