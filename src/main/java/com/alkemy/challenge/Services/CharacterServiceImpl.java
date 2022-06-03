package com.alkemy.challenge.Services;

import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.alkemy.challenge.DTOs.Characters.CharacterFullDTO;
import com.alkemy.challenge.DTOs.Characters.CharacterListDTO;
import com.alkemy.challenge.DTOs.Characters.CreateCharacterDTO;
import com.alkemy.challenge.DTOs.Characters.EditCharacterDTO;
import com.alkemy.challenge.DTOs.Characters.Converters.CharacterConverter;
import com.alkemy.challenge.DTOs.Pages.PageDTO;
import com.alkemy.challenge.DTOs.Pages.Converters.PageConverter;
import com.alkemy.challenge.Entities.Character;
import com.alkemy.challenge.Error.Exceptions.Characters.CharacterNameIsAlreadyInUseException;
import com.alkemy.challenge.Error.Exceptions.Characters.CharacterNotFoundException;
import com.alkemy.challenge.Repositories.CharacterRepository;
import com.alkemy.challenge.Services.Interfaces.CharacterService;
import com.alkemy.challenge.Services.Interfaces.ImageService;
import com.alkemy.challenge.Utils.Validate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

        private final CharacterRepository characterRepository;
        private final ImageService imageService;
        private final CharacterConverter characterConverter;
        private final PageConverter<CharacterListDTO> pageConverter;

        public PageDTO<CharacterListDTO> getCharacters(
                        Pageable paginacion,
                        Optional<String> name,
                        Optional<Integer> age,
                        Optional<Long> movieId) {

                Specification<Character> nameFilter = new Specification<Character>() {
                        @Override
                        public Predicate toPredicate(Root<Character> root, CriteriaQuery<?> query,
                                        CriteriaBuilder criteriaBuilder) {
                                if (name.isPresent()) {
                                        return criteriaBuilder.equal(root.get("name"), name.get());
                                }
                                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                        }
                };

                Specification<Character> ageFilter = new Specification<Character>() {
                        @Override
                        public Predicate toPredicate(Root<Character> root, CriteriaQuery<?> query,
                                        CriteriaBuilder criteriaBuilder) {
                                if (age.isPresent()) {
                                        return criteriaBuilder.equal(root.get("age"), age.get());
                                }
                                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                        }
                };

                Specification<Character> movieFilter = new Specification<Character>() {
                        @Override
                        public Predicate toPredicate(Root<Character> root, CriteriaQuery<?> query,
                                        CriteriaBuilder criteriaBuilder) {
                                if (movieId.isPresent()) {
                                        return criteriaBuilder.equal(root.join("movies").get("id"),
                                                        movieId.get());
                                }
                                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                        }
                };

                Specification<Character> filters = nameFilter.and(ageFilter).and(movieFilter);
                return pageConverter.toPageDTO(characterRepository.findAll(filters, paginacion)
                                .map(characterConverter::toCharacterListDTO));
        }

        public CharacterFullDTO getCharacterFull(long id) {
                return characterRepository.findById(id).map(characterConverter::toCharacterFullDTO)
                                .orElseThrow(() -> new CharacterNotFoundException(id));
        }

        public CharacterFullDTO createCharacter(CreateCharacterDTO createCharacterDTO,
                        Optional<MultipartFile> image) {
                Validate<CreateCharacterDTO> validacion = new Validate<CreateCharacterDTO>();
                validacion.validate(createCharacterDTO);

                if (characterRepository.findByName(createCharacterDTO.getName()).isPresent()) {
                        throw new CharacterNameIsAlreadyInUseException();
                }

                String imageUrl = null;
                if (image.isPresent()) {
                        imageUrl = imageService
                                        .getUri(imageService.createImage(image.get(), createCharacterDTO.getName()));
                }

                Character character = characterRepository.save(Character.builder()
                                .age(createCharacterDTO.getAge())
                                .history(createCharacterDTO.getHistory())
                                .image(imageUrl)
                                .name(createCharacterDTO.getName())
                                .weight(createCharacterDTO.getWeight())
                                .build());

                return characterRepository.findById(character.getId()).stream()
                                .map(characterConverter::toCharacterFullDTO)
                                .findAny()
                                .orElseThrow(() -> new CharacterNotFoundException(character.getId()));
        }

        public CharacterFullDTO editCharacter(long id, EditCharacterDTO editCharacterDTO,
                        Optional<MultipartFile> image) {
                Validate<EditCharacterDTO> validacion = new Validate<EditCharacterDTO>();
                validacion.validate(editCharacterDTO);
                Character characterBase = characterRepository.findById(id).orElseThrow(
                                () -> new CharacterNotFoundException(id));

                if (characterRepository.findByName(editCharacterDTO.getName()).isPresent()) {
                        throw new CharacterNameIsAlreadyInUseException();
                }

                if (editCharacterDTO.getName() != null) {
                        characterBase.setName(editCharacterDTO.getName());
                        if (characterBase.getImage() != null) {
                                String imageName = imageService.copyImageWithNewName(editCharacterDTO.getName(),
                                                imageService.getImageNameFromUri(characterBase.getImage()));
                                imageService.deleteImage(imageService.getImageNameFromUri(characterBase.getImage()));
                                characterBase.setImage(imageService.getUri(imageName));
                        }
                }

                if (image.isPresent()) {
                        if (characterBase.getImage() != null)
                                imageService.deleteImage(imageService.getImageNameFromUri(characterBase.getImage()));
                        String imageName = imageService.createImage(image.get(), characterBase.getName());
                        characterBase.setImage(imageService.getUri(imageName));
                }

                if (editCharacterDTO.getAge() != null)
                        characterBase.setAge(editCharacterDTO.getAge());
                if (editCharacterDTO.getHistory() != null)
                        characterBase.setHistory(editCharacterDTO.getHistory());
                if (editCharacterDTO.getWeight() != null)
                        characterBase.setWeight(editCharacterDTO.getWeight());

                characterBase = characterRepository.save(characterBase);

                return characterRepository.findById(characterBase.getId()).stream()
                                .map(characterConverter::toCharacterFullDTO).findAny()
                                .orElseThrow(() -> new CharacterNotFoundException(id));
        }

        public void deleteCharacter(long id) {
                Character characterBase = characterRepository.findById(id)
                                .orElseThrow(() -> new CharacterNotFoundException(id));
                if (characterBase.getImage() != null) {
                        imageService.deleteImage(imageService.getImageNameFromUri(characterBase.getImage()));
                }
                characterBase.getMovies().forEach(x -> x.getCharacters().remove(characterBase));
                // te odio many to many :(
                characterRepository.deleteById(id);
        }
}
