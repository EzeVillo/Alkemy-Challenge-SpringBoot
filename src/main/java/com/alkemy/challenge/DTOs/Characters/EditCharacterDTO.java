package com.alkemy.challenge.DTOs.Characters;

import javax.validation.constraints.Min;

import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCharacterDTO {

    @Nullable
    private String name;

    @Nullable
    @Min(value = 0, message = "The value cannot be less than 0")
    private Integer age;

    @Nullable
    @Min(value = 0, message = "The value cannot be less than 0")
    private Float weight;

    @Nullable
    private String history;
}
