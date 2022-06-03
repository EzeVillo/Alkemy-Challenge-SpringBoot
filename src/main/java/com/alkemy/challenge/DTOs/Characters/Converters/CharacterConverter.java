package com.alkemy.challenge.DTOs.Characters.Converters;

import com.alkemy.challenge.DTOs.Characters.CharacterFullDTO;
import com.alkemy.challenge.DTOs.Characters.CharacterListDTO;
import com.alkemy.challenge.DTOs.Characters.CreateCharacterDTO;
import com.alkemy.challenge.Entities.Character;

public interface CharacterConverter {
    public CharacterFullDTO toCharacterFullDTO(Character character);

    public CharacterListDTO toCharacterListDTO(Character character);

    public Character toCharacter(CreateCharacterDTO createCharacterDTO);
}
