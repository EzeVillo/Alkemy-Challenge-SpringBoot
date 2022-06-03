package com.alkemy.challenge.DTOs.Characters;

import com.alkemy.challenge.Views.View;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterListDTO {

    @JsonView(View.AdminView.class)
    private long id;
    private String name;
    private String image;
}
