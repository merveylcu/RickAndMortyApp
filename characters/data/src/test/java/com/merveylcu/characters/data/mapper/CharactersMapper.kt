package com.merveylcu.characters.data.mapper

import com.google.gson.Gson
import com.merveylcu.characters.data.model.CharactersResponse
import com.merveylcu.characters.data.util.jsonContent
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CharactersMapperTest {

    private lateinit var mapper: CharactersMapper

    @BeforeEach
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mapper = CharactersMapper()
    }

    @AfterEach
    fun after() {
        clearAllMocks()
    }

    @Test
    fun `given CharactersResponse when call map(), then return mapped CharactersDto`() = runTest {
        //Given
        val response =
            Gson().fromJson(jsonContent("CharactersResponse.json"), CharactersResponse::class.java)

        //When
        val dto = mapper.map(response)

        //Then
        Assertions.assertEquals(dto.results?.get(0)?.name, "Rick Sanchez")
    }
}
