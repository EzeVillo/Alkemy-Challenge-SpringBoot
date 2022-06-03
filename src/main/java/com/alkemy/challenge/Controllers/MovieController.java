package com.alkemy.challenge.Controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.alkemy.challenge.DTOs.Movies.CreateMovieDTO;
import com.alkemy.challenge.DTOs.Movies.EditMovieDTO;
import com.alkemy.challenge.DTOs.Movies.MovieFullDTO;
import com.alkemy.challenge.DTOs.Movies.MovieListDTO;
import com.alkemy.challenge.DTOs.Pages.PageDTO;
import com.alkemy.challenge.Error.Errors.ApiError;
import com.alkemy.challenge.Error.Exceptions.Movies.MovieNoContentException;
import com.alkemy.challenge.Services.Interfaces.MovieService;
import com.alkemy.challenge.Utils.Interfaces.PaginationLinks;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("movies")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MovieController {

        private final MovieService movieService;
        private final PaginationLinks paginationLinks;

        /**
         * 6. Detalle de Película / Serie con sus personajes
         * 
         * @param id
         * @return status code 200 and full movie, if it is not found 404
         */
        @ApiOperation(value = "returns a full movie searched for by id")
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "Ok", response = MovieFullDTO.class),
                        @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @GetMapping(value = "/{id}")
        public ResponseEntity<MovieFullDTO> getMovieFull(@PathVariable long id) {
                return ResponseEntity.ok(movieService.getMovieFull(id));
        }

        /**
         * 7. Creación, Edición y Eliminación de Película / Serie.
         * 
         * @param createMovieDTO
         * @param image
         * @return status code 200 and created character
         */
        @ApiOperation(value = "create a movie", notes = "must be administrator")
        @ApiResponses(value = {
                        @ApiResponse(code = 201, message = "Created", response = MovieFullDTO.class),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping(value = "/create")
        public ResponseEntity<MovieFullDTO> createMovie(
                        @RequestPart CreateMovieDTO createMovieDTO,
                        @RequestPart Optional<MultipartFile> image) {
                return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(createMovieDTO, image));
        }

        /**
         * 
         * @param id
         * @param editMovieDTO
         * @param image
         * @return status code 200 and edited movie, if it is not found 404
         */
        @ApiOperation(value = "edit a movie", notes = "must be administrator")
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "Ok", response = MovieFullDTO.class),
                        @ApiResponse(code = 404, message = "Not found", response = ApiError.class),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping(value = "/edit/{id}")
        public ResponseEntity<MovieFullDTO> editMovie(
                        @PathVariable long id,
                        @RequestPart EditMovieDTO editMovieDTO,
                        @RequestPart Optional<MultipartFile> image) {
                return ResponseEntity.ok(movieService.editMovie(id, editMovieDTO, image));
        }

        /**
         * 
         * @param id
         * @return status code 200, if it is not found 404
         */
        @ApiOperation(value = "delete a movie", notes = "must be administrator")
        @ApiResponses(value = {
                        @ApiResponse(code = 204, message = "No Content", response = MovieFullDTO.class),
                        @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping(value = "/delete/{id}")
        public ResponseEntity<Void> deleteMovie(@PathVariable long id) {
                movieService.deleteMovie(id);
                return ResponseEntity.noContent().build();
        }

        /**
         * 8. Búsqueda de Películas o Series
         * 
         * @param title
         * @param genreId
         * @param order
         * @param pageable
         * @return status code 200 and movie list, if it has no content 204
         */
        @ApiOperation(value = "returns a movie list filtered by the data sent")
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "Ok", response = PageDTO.class),
                        @ApiResponse(code = 204, message = "No Content", response = ApiError.class),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @GetMapping
        public ResponseEntity<PageDTO<MovieListDTO>> getMovies(
                        @RequestParam("name") Optional<String> title,
                        @RequestParam("genre") Optional<Long> genreId,
                        @RequestParam Optional<String> order,
                        @PageableDefault(size = 10, page = 0) Pageable pageable,
                        HttpServletRequest request) {
                PageDTO<MovieListDTO> movies = movieService.getMovies(title, genreId, order, pageable);
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

                if (movies.getIsEmpty())
                        throw new MovieNoContentException();

                return ResponseEntity.ok().header("link", paginationLinks.createHeaderLink(movies, uriBuilder))
                                .body(movies);
        }

        /**
         * 9. Agregar/Remover personajes a una película
         * 
         * @param idCharacter
         * @param idMovie
         * @return status code 204, if the user or the movie is not found returns 404
         */
        @ApiOperation(value = "add a character to a movie", notes = "must be administrator")
        @ApiResponses(value = {
                        @ApiResponse(code = 204, message = "No Content"),
                        @ApiResponse(code = 404, message = "Not found"),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping(value = "/{idCharacter}/characters/{idMovie}")
        public ResponseEntity<Void> addCharacter(@PathVariable long idCharacter, @PathVariable long idMovie) {
                movieService.addCharacter(idCharacter, idMovie);
                return ResponseEntity.noContent().build();
        }

        /**
         * 9. Agregar/Remover personajes a una película
         * 
         * @param idCharacter
         * @param idMovie
         * @return status code 204, if the user or the movie is not found returns 404
         */
        @ApiOperation(value = "remove a character to a movie", notes = "must be administrator")
        @ApiResponses(value = {
                        @ApiResponse(code = 204, message = "No Content"),
                        @ApiResponse(code = 404, message = "Not found"),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping(value = "/{idMovie}/characters/{idCharacter}")
        public ResponseEntity<Void> removeCharacter(@PathVariable long idCharacter, @PathVariable long idMovie) {
                movieService.removeCharacter(idMovie, idCharacter);
                return ResponseEntity.noContent().build();
        }

}
