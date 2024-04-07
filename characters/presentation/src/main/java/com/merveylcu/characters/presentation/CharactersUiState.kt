package com.merveylcu.characters.presentation

import com.merveylcu.characters.domain.model.CharacterDto

internal data class CharactersUiState(
    val list: List<CharacterDto> = listOf(),
    val showError: Boolean = false,
    val lastPageIndex: Int = 1,
    val isLoading: Boolean = true
)
