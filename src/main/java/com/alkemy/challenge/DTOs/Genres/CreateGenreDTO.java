package com.alkemy.challenge.DTOs.Genres;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGenreDTO {
    @NotBlank
    private String name;
}
