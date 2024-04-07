package com.merveylcu.characters.data.mapper

import com.merveylcu.characters.data.model.Character
import com.merveylcu.characters.data.model.CharactersResponse
import com.merveylcu.characters.data.model.Location
import com.merveylcu.characters.data.model.PageInfo
import com.merveylcu.characters.domain.model.CharacterDto
import com.merveylcu.characters.domain.model.CharactersDto
import com.merveylcu.characters.domain.model.LocationDto
import com.merveylcu.characters.domain.model.PageInfoDto
import javax.inject.Inject

internal class CharactersMapper @Inject constructor() {

    fun map(response: CharactersResponse?) = response?.let {
        CharactersDto(
            info = map(response.info),
            results = map(response.results)
        )
    } ?: CharactersDto()

    private fun map(model: PageInfo?) = model?.let {
        PageInfoDto(
            count = model.count,
            pages = model.pages,
            next = model.next,
            prev = model.prev
        )
    } ?: PageInfoDto()

    private fun map(list: List<Character>?) = list?.map {
        CharacterDto(it.id, it.name, it.status, map(it.location), it.image)
    } ?: listOf()

    private fun map(model: Location?) = model?.let {
        LocationDto(
            name = model.name,
            url = model.url
        )
    } ?: LocationDto()
}
