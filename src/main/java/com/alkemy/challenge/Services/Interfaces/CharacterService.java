package com.alkemy.challenge.Services.Interfaces;

import java.util.Optional;

import com.alkemy.challenge.DTOs.Characters.CharacterFullDTO;
import com.alkemy.challenge.DTOs.Characters.CharacterListDTO;
import com.alkemy.challenge.DTOs.Characters.CreateCharacterDTO;
import com.alkemy.challenge.DTOs.Characters.EditCharacterDTO;
import com.alkemy.challenge.DTOs.Pages.PageDTO;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface CharacterService {
    public PageDTO<CharacterListDTO> getCharacters(
            Pageable pageable,
            final Optional<String> name,
            final Optional<Integer> age,
            final Optional<Long> characterId);

    public CharacterFullDTO getCharacterFull(long id);

    public CharacterFullDTO createCharacter(CreateCharacterDTO createCharacterDTO,
            Optional<MultipartFile> image);

    public CharacterFullDTO editCharacter(long id, EditCharacterDTO editCharacterDTO,
            Optional<MultipartFile> image);

    public void deleteCharacter(long id);
}
