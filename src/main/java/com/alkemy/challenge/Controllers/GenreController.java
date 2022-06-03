package com.alkemy.challenge.Controllers;

import java.util.Optional;

import com.alkemy.challenge.DTOs.Genres.CreateGenreDTO;
import com.alkemy.challenge.DTOs.Genres.GenreFullDTO;
import com.alkemy.challenge.Error.Errors.ApiError;
import com.alkemy.challenge.Services.Interfaces.GenreService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("genre")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class GenreController {

    private final GenreService genreService;

    /**
     * 3. Creación de Géneros
     * 
     * @param createGenreDTO
     * @param image
     * @return status code 201 and created genre
     */
    @ApiOperation(value = "create a genre", notes = "must be administrator")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = GenreFullDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create")
    public ResponseEntity<GenreFullDTO> createGenre(
            @RequestPart CreateGenreDTO createGenreDTO,
            @RequestPart Optional<MultipartFile> image) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.createGenre(createGenreDTO, image));
    }

}
