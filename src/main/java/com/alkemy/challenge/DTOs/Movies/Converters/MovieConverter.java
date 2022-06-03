package com.alkemy.challenge.DTOs.Movies.Converters;

import com.alkemy.challenge.DTOs.Movies.MovieFullDTO;
import com.alkemy.challenge.DTOs.Movies.MovieListDTO;
import com.alkemy.challenge.Entities.Movie;

public interface MovieConverter {
    public MovieFullDTO toMovieFullDTO(Movie movie);

    public MovieListDTO toMovieListDTO(Movie movie);
}
