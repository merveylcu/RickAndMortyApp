package com.merveylcu.characters.domain.model

data class PageInfoDto(
    val count: Int? = null,
    val pages: Int? = null,
    val next: String? = null,
    val prev: String? = null
)
