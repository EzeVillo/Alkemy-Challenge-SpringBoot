package com.alkemy.challenge.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.alkemy.challenge.DTOs.Characters.CreateCharacterDTO;
import com.alkemy.challenge.DTOs.Characters.Converters.CharacterConverter;
import com.alkemy.challenge.DTOs.Movies.CreateMovieDTO;
import com.alkemy.challenge.DTOs.Movies.EditMovieDTO;
import com.alkemy.challenge.DTOs.Movies.MovieFullDTO;
import com.alkemy.challenge.DTOs.Movies.MovieListDTO;
import com.alkemy.challenge.DTOs.Movies.Converters.MovieConverter;
import com.alkemy.challenge.DTOs.Pages.PageDTO;
import com.alkemy.challenge.DTOs.Pages.Converters.PageConverter;
import com.alkemy.challenge.Entities.Character;
import com.alkemy.challenge.Entities.Genre;
import com.alkemy.challenge.Entities.Movie;
import com.alkemy.challenge.Error.Exceptions.Characters.CharacterNameIsAlreadyInUseException;
import com.alkemy.challenge.Error.Exceptions.Characters.CharacterNotFoundException;
import com.alkemy.challenge.Error.Exceptions.Genres.GenreNotFoundException;
import com.alkemy.challenge.Error.Exceptions.Movies.MovieNotFoundException;
import com.alkemy.challenge.Error.Exceptions.Movies.MovieTitleIsAlreadyInUseException;
import com.alkemy.challenge.Repositories.CharacterRepository;
import com.alkemy.challenge.Repositories.GenreRepository;
import com.alkemy.challenge.Repositories.MovieRepository;
import com.alkemy.challenge.Services.Interfaces.ImageService;
import com.alkemy.challenge.Services.Interfaces.MovieService;
import com.alkemy.challenge.Utils.Validate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieConverter movieConverter;
    private final ImageService imageService;
    private final GenreRepository genreRepository;
    private final CharacterRepository characterRepository;
    private final CharacterConverter characterConverter;
    private final PageConverter<MovieListDTO> pageConverter;
    private final static String ASCENDENTE = "ASC";

    public MovieFullDTO getMovieFull(long id) {
        return movieRepository.findById(id).map(movieConverter::toMovieFullDTO).orElseThrow(
                () -> new MovieNotFoundException(id));
    }

    public PageDTO<MovieListDTO> getMovies(
            Optional<String> title,
            Optional<Long> genreId,
            Optional<String> order,
            Pageable pageable) {
        Specification<Movie> titleFilter = new Specification<Movie>() {

            @Override
            public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query,
                    CriteriaBuilder criteriaBuilder) {
                if (title.isPresent()) {
                    return criteriaBuilder.equal(root.get("title"), title.get());
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }
            }
        };

        Specification<Movie> genreFilter = new Specification<Movie>() {

            @Override
            public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (genreId.isPresent()) {
                    return criteriaBuilder.equal(root.join("genre").get("id"), genreId.get());
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }
            }
        };

        Specification<Movie> orderFilter = new Specification<Movie>() {

            @Override
            public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (order.isPresent() && order.get().equals(ASCENDENTE)) {
                    query.orderBy(criteriaBuilder.asc(root.get("title")));
                } else {
                    query.orderBy(criteriaBuilder.desc(root.get("title")));
                }
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
        };
        Specification<Movie> filters = titleFilter.and(genreFilter).and(orderFilter);
        return pageConverter.toPageDTO(movieRepository.findAll(filters, pageable)
                .map(movieConverter::toMovieListDTO));
        // NO HAY UN METODO FIND ALL QUE ME DEJE PAGINAR, MANDAR FILTROS Y ORDENAR :(((
        // List<T> findAll(@Nullable Specification<T> spec, Sort sort);
        // Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable);
        // :((((
    }

    public MovieFullDTO createMovie(CreateMovieDTO createMovieDTO, Optional<MultipartFile> image) {
        Validate<CreateMovieDTO> validateMovie = new Validate<CreateMovieDTO>();
        validateMovie.validate(createMovieDTO);

        if (movieRepository.findByTitle(createMovieDTO.getTitle()).isPresent()) {
            throw new MovieTitleIsAlreadyInUseException();
        }

        // no me valida la lista de personajes :(((((
        Validate<CreateCharacterDTO> validateCharacters = new Validate<CreateCharacterDTO>();
        createMovieDTO.getCharacters().forEach(validateCharacters::validate);

        createMovieDTO.getCharacters().forEach(x -> {
            if (characterRepository.findByName(x.getName()).isPresent())
                throw new CharacterNameIsAlreadyInUseException();
        });

        Genre genre = genreRepository.findById(createMovieDTO.getGenreId()).orElseThrow(
                () -> new GenreNotFoundException(createMovieDTO.getGenreId()));

        String imageUrl = null;
        if (image.isPresent()) {
            imageUrl = imageService.getUri(imageService.createImage(image.get(), createMovieDTO.getTitle()));
        }

        List<Character> characters = characterRepository.saveAll(createMovieDTO.getCharacters()
                .stream()
                .map(characterConverter::toCharacter)
                .collect(Collectors.toList()));

        Movie movie = Movie.builder()
                .score(createMovieDTO.getScore())
                .creationDate(createMovieDTO.getCreationDate())
                .genre(genre)
                .image(imageUrl)
                .characters(characters.stream().collect(Collectors.toSet()))
                .title(createMovieDTO.getTitle())
                .build();
        movie = movieRepository.save(movie);

        return movieRepository.findById(movie.getId()).map(movieConverter::toMovieFullDTO)
                .orElseThrow(() -> new MovieNotFoundException("Could not query created movie"));
    }

    public MovieFullDTO editMovie(long id, EditMovieDTO editMovieDTO,
            Optional<MultipartFile> image) {
        Validate<EditMovieDTO> validacion = new Validate<EditMovieDTO>();
        validacion.validate(editMovieDTO);

        Movie movieBase = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        if (movieRepository.findByTitle(editMovieDTO.getTitle()).isPresent()) {
            throw new MovieTitleIsAlreadyInUseException();
        }

        if (editMovieDTO.getTitle() != null) {
            movieBase.setTitle(editMovieDTO.getTitle());
            if (movieBase.getImage() != null) {
                String imageName = imageService.copyImageWithNewName(editMovieDTO.getTitle(),
                        imageService.getImageNameFromUri(movieBase.getImage()));
                imageService.deleteImage(imageService.getImageNameFromUri(movieBase.getImage()));
                movieBase.setImage(imageService.getUri(imageName));
            }
        }

        if (image.isPresent()) {
            if (movieBase.getImage() != null)
                imageService.deleteImage(imageService.getImageNameFromUri(movieBase.getImage()));
            String imageName = imageService.createImage(image.get(), movieBase.getTitle());
            movieBase.setImage(imageService.getUri(imageName));
        }

        if (editMovieDTO.getScore() != null)
            movieBase.setScore(editMovieDTO.getScore());
        if (editMovieDTO.getCreationDate() != null)
            movieBase.setCreationDate(editMovieDTO.getCreationDate());
        if (editMovieDTO.getGenreId() != null) {
            Genre genre = genreRepository.findById(editMovieDTO.getGenreId()).orElseThrow(
                    () -> new GenreNotFoundException(editMovieDTO.getGenreId()));
            if (genre != null)
                movieBase.setGenre(genre);
        }

        movieRepository.save(movieBase);
        return movieRepository.findById(id).map(movieConverter::toMovieFullDTO)
                .orElseThrow(() -> new MovieNotFoundException("The edited movie could not be consulted"));
    }

    public void deleteMovie(long id) {
        Movie movieBase = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
        if (movieBase.getImage() != null) {
            imageService.deleteImage(imageService.getImageNameFromUri(movieBase.getImage()));
        }
        movieRepository.delete(movieBase);
    }

    public void addCharacter(long movieId, long characterId) {
        Movie movieBase = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(movieId));
        Character characterBase = characterRepository.findById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId));
        movieBase.getCharacters().add(characterBase);
        movieRepository.save(movieBase);
    }

    public void removeCharacter(long movieId, long characterId) {
        Movie movieBase = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(movieId));
        Character characterBase = characterRepository.findById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId));
        movieBase.getCharacters().remove(characterBase);
        movieRepository.save(movieBase);
    }
}
