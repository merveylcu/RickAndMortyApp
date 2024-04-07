package com.merveylcu.characters.data.repository

import com.google.gson.Gson
import com.merveylcu.characters.data.CharactersRepositoryImpl
import com.merveylcu.characters.data.CharactersService
import com.merveylcu.characters.data.mapper.CharactersMapper
import com.merveylcu.characters.data.model.CharactersResponse
import com.merveylcu.characters.data.util.jsonContent
import com.merveylcu.characters.domain.model.CharactersDto
import com.merveylcu.contract.RestResult
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class CharactersRepositoryImplTest {

    @MockK
    lateinit var mockCharactersService: CharactersService

    @MockK
    lateinit var mockCharactersMapper: CharactersMapper

    private lateinit var charactersRepositoryImpl: CharactersRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    private val page = 1
    private val mappedCharactersDto = CharactersDto()

    @BeforeEach
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        charactersRepositoryImpl =
            CharactersRepositoryImpl(
                service = mockCharactersService,
                mapper = mockCharactersMapper
            )
    }

    @AfterEach
    fun after() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `when call getPortfolio(), then return success result true`() = runTest {
        val response =
            Gson().fromJson(jsonContent("CharactersResponse.json"), CharactersResponse::class.java)
        coEvery {
            mockCharactersService.getCharacters(page)
        } returns Response.success(response)
        every {
            mockCharactersMapper.map(response)
        } returns mappedCharactersDto

        val result = charactersRepositoryImpl.getCharacters(page)

        coVerify(exactly = 1) { mockCharactersService.getCharacters(page) }
        Assertions.assertTrue(result is RestResult.Success)
    }

    @Test
    fun `when call getPortfolio(), then return error result`() = runTest {
        coEvery {
            mockCharactersService.getCharacters(page)
        } returns Response.success(null)
        every {
            mockCharactersMapper.map(null)
        } returns mappedCharactersDto

        val result = charactersRepositoryImpl.getCharacters(page)

        coVerify(exactly = 1) { mockCharactersService.getCharacters(page) }
        Assertions.assertTrue(result is RestResult.Error)
    }
}
