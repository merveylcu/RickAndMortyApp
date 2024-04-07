package com.merveylcu.characters.domain.usecase

import com.merveylcu.characters.domain.model.CharactersDto
import com.merveylcu.characters.domain.repository.CharactersRepository
import com.merveylcu.characters.domain.util.buildDefaultFlow
import com.merveylcu.contract.RestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharactersRepository
) {

    operator fun invoke(page: Int): Flow<RestResult<CharactersDto>> = flow {
        val response = repository.getCharacters(page)
        emit(response)
    }.buildDefaultFlow(Dispatchers.IO)
}
