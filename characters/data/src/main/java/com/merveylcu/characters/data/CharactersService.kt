package com.merveylcu.characters.data

import com.merveylcu.characters.data.model.CharactersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CharactersService {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<CharactersResponse>
}
