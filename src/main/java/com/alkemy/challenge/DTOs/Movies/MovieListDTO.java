package com.alkemy.challenge.DTOs.Movies;

import java.time.LocalDate;

import com.alkemy.challenge.Views.View;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieListDTO {

    @JsonView(View.AdminView.class)
    private long id;
    private String image;
    private String title;
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate creationDate;
}
