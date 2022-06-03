package com.alkemy.challenge.DTOs.Characters;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
public class CreateCharacterDTO {

    @NotBlank
    private String name;

    @NotNull
    @Min(value = 0, message = "The value cannot be less than 0")
    private Integer age;

    @NotNull
    @Min(value = 0, message = "The value cannot be less than 0")
    private Float weight;

    @NotBlank
    private String history;
}
