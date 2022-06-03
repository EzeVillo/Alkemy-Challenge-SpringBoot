package com.alkemy.challenge.Services.Interfaces;

import java.util.Optional;

import com.alkemy.challenge.DTOs.Genres.CreateGenreDTO;
import com.alkemy.challenge.DTOs.Genres.GenreFullDTO;

import org.springframework.web.multipart.MultipartFile;

public interface GenreService {
    public GenreFullDTO createGenre(CreateGenreDTO createGenreDTO, Optional<MultipartFile> image);
}
