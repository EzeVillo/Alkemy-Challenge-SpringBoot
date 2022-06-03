package com.alkemy.challenge.DTOs.Genres.Converters;

import com.alkemy.challenge.DTOs.Genres.GenreFullDTO;
import com.alkemy.challenge.Entities.Genre;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenreConverterImpl implements GenreConverter {
    private final ModelMapper modelMapper;

    public GenreFullDTO toGenreFullDTO(Genre genre) {
        return modelMapper.map(genre, GenreFullDTO.class);
    }
}
