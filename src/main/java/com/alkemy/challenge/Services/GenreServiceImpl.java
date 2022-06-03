package com.alkemy.challenge.Services;

import java.util.Optional;

import com.alkemy.challenge.DTOs.Genres.CreateGenreDTO;
import com.alkemy.challenge.DTOs.Genres.GenreFullDTO;
import com.alkemy.challenge.DTOs.Genres.Converters.GenreConverter;
import com.alkemy.challenge.Entities.Genre;
import com.alkemy.challenge.Error.Exceptions.Genres.GenreNameIsAlreadyInUseException;
import com.alkemy.challenge.Error.Exceptions.Genres.GenreNotFoundException;
import com.alkemy.challenge.Repositories.GenreRepository;
import com.alkemy.challenge.Services.Interfaces.GenreService;
import com.alkemy.challenge.Services.Interfaces.ImageService;
import com.alkemy.challenge.Utils.Validate;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreConverter genreConverter;
    private final ImageService imageService;

    public GenreFullDTO createGenre(CreateGenreDTO createGenreDTO, Optional<MultipartFile> image) {
        Validate<CreateGenreDTO> validacion = new Validate<CreateGenreDTO>();
        validacion.validate(createGenreDTO);

        if(genreRepository.findByName(createGenreDTO.getName()).isPresent()){
            throw new GenreNameIsAlreadyInUseException();
        }
        
        String imageUrl = null;
        if (image.isPresent()) {
            imageUrl = imageService.getUri(imageService.createImage(image.get(), createGenreDTO.getName()));
        }

        Genre genre = genreRepository.save(Genre.builder()
                .name(createGenreDTO.getName())
                .image(imageUrl)
                .build());

        return genreRepository.findById(genre.getId()).stream().map(genreConverter::toGenreFullDTO)
                .findFirst()
                .orElseThrow(() -> new GenreNotFoundException("The created genre could not be consulted"));
    }
}
