package com.merveylcu.characters.data

import com.merveylcu.characters.data.mapper.CharactersMapper
import com.merveylcu.characters.domain.model.CharactersDto
import com.merveylcu.characters.domain.repository.CharactersRepository
import com.merveylcu.contract.RestResult
import com.merveylcu.contract.mapOnSuccess
import com.merveylcu.network.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class CharactersRepositoryImpl @Inject constructor(
    private val service: CharactersService,
    private val mapper: CharactersMapper
) : BaseRepository(), CharactersRepository {

    override suspend fun getCharacters(page: Int): RestResult<CharactersDto> =
        withContext(Dispatchers.IO) {
            return@withContext request {
                service.getCharacters(page)
            }.mapOnSuccess {
                mapper.map(it)
            }
        }
}
