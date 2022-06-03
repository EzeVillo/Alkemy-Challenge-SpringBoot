package com.alkemy.challenge.Controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.alkemy.challenge.DTOs.Characters.CharacterFullDTO;
import com.alkemy.challenge.DTOs.Characters.CharacterListDTO;
import com.alkemy.challenge.DTOs.Characters.CreateCharacterDTO;
import com.alkemy.challenge.DTOs.Characters.EditCharacterDTO;
import com.alkemy.challenge.DTOs.Pages.PageDTO;
import com.alkemy.challenge.Error.Errors.ApiError;
import com.alkemy.challenge.Error.Exceptions.Characters.CharacterNoContentException;
import com.alkemy.challenge.Services.Interfaces.CharacterService;
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
@RequestMapping("characters")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CharacterController {

        private final CharacterService characterService;
        private final PaginationLinks paginationLinks;

        /**
         * 2. Creación, Edición y Eliminación de Personajes (CRUD)
         * 
         * @param createCharacterDTO
         * @param image
         * @return status code 201 and created character
         */
        @ApiOperation(value = "create a character", notes = "must be administrator")
        @ApiResponses(value = {
                        @ApiResponse(code = 201, message = "Created", response = CharacterFullDTO.class),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping(value = "/create")
        public ResponseEntity<CharacterFullDTO> createCharacter(
                        @RequestPart CreateCharacterDTO createCharacterDTO,
                        @RequestPart Optional<MultipartFile> image) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(characterService.createCharacter(createCharacterDTO, image));
        }

        /**
         * 
         * @param id
         * @param editCharacterDTO
         * @param image
         * @return status code 200 and edited character, if it is not found 404
         */
        @ApiOperation(value = "edit a character", notes = "must be administrator")
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "OK", response = CharacterFullDTO.class),
                        @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping(value = "/edit/{id}")
        public ResponseEntity<CharacterFullDTO> editCharacter(
                        @PathVariable long id,
                        @RequestPart EditCharacterDTO editCharacterDTO,
                        @RequestPart Optional<MultipartFile> image) {
                return ResponseEntity.ok(characterService.editCharacter(id, editCharacterDTO, image));
        }

        /**
         * 
         * @param id
         * @return status code 204, if it is not found 404
         */
        @ApiOperation(value = "delete a character", notes = "must be administrator")
        @ApiResponses(value = {
                        @ApiResponse(code = 204, message = "No Content"),
                        @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping(value = "/delete/{id}")
        public ResponseEntity<Void> deleteCharacter(@PathVariable long id) {
                characterService.deleteCharacter(id);
                return ResponseEntity.noContent().build();
        }

        /**
         * 4. Detalle de Personaje
         * 
         * @param id
         * @return status code 200 and full character, if it is not found 404
         */
        @ApiOperation(value = "returns a full character searched for by id")
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "Ok", response = CharacterFullDTO.class),
                        @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @GetMapping(value = "/{id}")
        public ResponseEntity<CharacterFullDTO> getCharacterFull(@PathVariable long id) {
                return ResponseEntity.ok(characterService.getCharacterFull(id));
        }

        /**
         * 5. Búsqueda de Personajes
         * 
         * @param name
         * @param age
         * @param movieId
         * @param pageable
         * @return status code 200 and character list, if it has no content 204
         */
        @ApiOperation(value = "returns a character list filtered by the data sent")
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "Ok", response = PageDTO.class),
                        @ApiResponse(code = 204, message = "No Content", response = ApiError.class),
                        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
        })
        @GetMapping()
        public ResponseEntity<PageDTO<CharacterListDTO>> getCharacters(
                        @RequestParam Optional<String> name,
                        @RequestParam Optional<Integer> age,
                        @RequestParam("movie") Optional<Long> movieId,
                        @PageableDefault(size = 10, page = 0) Pageable pageable,
                        HttpServletRequest request) {
                PageDTO<CharacterListDTO> characters = characterService.getCharacters(pageable, name, age, movieId);
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

                if (characters.getIsEmpty())
                        throw new CharacterNoContentException();

                return ResponseEntity.ok().header("link", paginationLinks.createHeaderLink(characters, uriBuilder))
                                .body(characters);
        }

}
