package com.alkemy.challenge.DTOs.Movies.Converters;

import java.util.stream.Collectors;

import com.alkemy.challenge.DTOs.Movies.MovieFullDTO;
import com.alkemy.challenge.DTOs.Movies.MovieListDTO;
import com.alkemy.challenge.Entities.Movie;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MovieConverterImpl implements MovieConverter {

    private final ModelMapper modelMapper;

    public MovieFullDTO toMovieFullDTO(Movie movie) {
        return MovieFullDTO.builder()
                .score(movie.getScore())
                .creationDate(movie.getCreationDate())
                .genre(movie.getGenre().getName())
                .id(movie.getId())
                .image(movie.getImage())
                .characters(movie.getCharacters().stream().map(x -> x.getName()).collect(Collectors.toSet()))
                .title(movie.getTitle())
                .build();
    }

    public MovieListDTO toMovieListDTO(Movie movie) {
        return modelMapper.map(movie, MovieListDTO.class);
    }
}
