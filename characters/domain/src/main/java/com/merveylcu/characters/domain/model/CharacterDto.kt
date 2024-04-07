package com.merveylcu.characters.domain.model

data class CharacterDto(
    val id: Long? = null,
    val name: String? = null,
    val status: String? = null,
    val locationDto: LocationDto? = null,
    val image: String? = null
)
