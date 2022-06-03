package com.alkemy.challenge.Services.Interfaces;

import java.util.Optional;

import com.alkemy.challenge.DTOs.Movies.CreateMovieDTO;
import com.alkemy.challenge.DTOs.Movies.EditMovieDTO;
import com.alkemy.challenge.DTOs.Movies.MovieFullDTO;
import com.alkemy.challenge.DTOs.Movies.MovieListDTO;
import com.alkemy.challenge.DTOs.Pages.PageDTO;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MovieService {
        public MovieFullDTO getMovieFull(long id);

        public PageDTO<MovieListDTO> getMovies(
                        Optional<String> title,
                        Optional<Long> genreId,
                        Optional<String> order,
                        Pageable pageable);

        public MovieFullDTO createMovie(CreateMovieDTO createMovieDTO, Optional<MultipartFile> image);

        public MovieFullDTO editMovie(long id, EditMovieDTO editMovieDTO,
                        Optional<MultipartFile> image);

        public void deleteMovie(long id);

        public void addCharacter(long idMovie, long idCharacter);

        public void removeCharacter(long idMovie, long idCharacter);
}
