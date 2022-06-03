package com.alkemy.challenge.DTOs.Characters;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CharacterFullDTO {

    private long id;
    private String image;
    private String name;
    private int age;
    private float weight;
    private String history;
    private Set<String> movies;
}
