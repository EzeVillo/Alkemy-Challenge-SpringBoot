package com.alkemy.challenge.DTOs.Characters.Converters;

import java.util.stream.Collectors;

import com.alkemy.challenge.DTOs.Characters.CharacterFullDTO;
import com.alkemy.challenge.DTOs.Characters.CharacterListDTO;
import com.alkemy.challenge.DTOs.Characters.CreateCharacterDTO;
import com.alkemy.challenge.Entities.Character;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CharacterConverterImpl implements CharacterConverter {

    private final ModelMapper modelMapper;

    public CharacterFullDTO toCharacterFullDTO(Character character) {
        return CharacterFullDTO.builder()
                .age(character.getAge())
                .history(character.getHistory())
                .id(character.getId())
                .image(character.getImage())
                .name(character.getName())
                .movies(character.getMovies().stream().map(x -> x.getTitle()).collect(Collectors.toSet()))
                .weight(character.getWeight())
                .build();
    }

    public CharacterListDTO toCharacterListDTO(Character character) {
        return modelMapper.map(character, CharacterListDTO.class);
    }

    public Character toCharacter(CreateCharacterDTO createCharacterDTO) {
        return Character.builder()
                .age(createCharacterDTO.getAge())
                .history(createCharacterDTO.getHistory())
                .name(createCharacterDTO.getName())
                .weight(createCharacterDTO.getWeight())
                .build();
    }
}
