package com.merveylcu.characters.domain.repository

import com.merveylcu.characters.domain.model.CharactersDto
import com.merveylcu.contract.RestResult

interface CharactersRepository {

    suspend fun getCharacters(page: Int): RestResult<CharactersDto>
}
