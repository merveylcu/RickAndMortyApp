package com.merveylcu.characters.data.model

import com.google.gson.annotations.SerializedName

internal data class Location(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("url")
    val url: String? = null
)
