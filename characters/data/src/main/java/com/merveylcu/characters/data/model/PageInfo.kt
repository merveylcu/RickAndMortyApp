package com.merveylcu.characters.data.model

import com.google.gson.annotations.SerializedName

internal data class PageInfo(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("pages")
    val pages: Int? = null,
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("prev")
    val prev: String? = null
)
