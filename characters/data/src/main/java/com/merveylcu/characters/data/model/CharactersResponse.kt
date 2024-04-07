package com.merveylcu.characters.data.model

import com.google.gson.annotations.SerializedName

internal data class CharactersResponse(
    @SerializedName("info")
    val info: PageInfo? = null,
    @SerializedName("results")
    val results: List<Character>? = null
)
