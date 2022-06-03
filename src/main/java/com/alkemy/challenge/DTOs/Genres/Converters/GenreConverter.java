package com.alkemy.challenge.DTOs.Genres.Converters;

import com.alkemy.challenge.DTOs.Genres.GenreFullDTO;
import com.alkemy.challenge.Entities.Genre;

public interface GenreConverter {
    public GenreFullDTO toGenreFullDTO(Genre genre);
}
