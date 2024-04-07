package com.merveylcu.characters.domain.model

data class CharactersDto(
    val info: PageInfoDto? = null,
    val results: List<CharacterDto>? = null
)
