package com.merveylcu.characters.domain.usecase

import app.cash.turbine.test
import com.merveylcu.characters.domain.model.CharactersDto
import com.merveylcu.characters.domain.repository.CharactersRepository
import com.merveylcu.contract.INetworkError
import com.merveylcu.contract.RestResult
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
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

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class GetRecurringPreciousMetalDetailUseCaseTest {

    @MockK
    lateinit var mockCharactersRepository: CharactersRepository

    private val mockNetworkError = spyk<INetworkError> {
        every { message } returns "test_error_message"
        every { code } returns 401
    }

    private val response = CharactersDto()
    private val page = 1

    private lateinit var useCase: GetCharactersUseCase

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        useCase = GetCharactersUseCase(
            repository = mockCharactersRepository
        )
    }

    @AfterEach
    fun after() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `when GetCharactersUseCase invoke, then return RestResult Success`() =
        runTest {
            coEvery {
                mockCharactersRepository.getCharacters(page)
            } returns RestResult.Success(result = response)

            val result = useCase(page)

            result.test {
                val resultLoadingStart = awaitItem()
                Assertions.assertTrue(resultLoadingStart is RestResult.Loading)
                Assertions.assertTrue((resultLoadingStart as RestResult.Loading).loading)
                val resultSuccess = awaitItem()
                Assertions.assertTrue(resultSuccess is RestResult.Success)
                Assertions.assertEquals((resultSuccess as RestResult.Success).result, response)
                val resultLoadingEnd = awaitItem()
                Assertions.assertTrue(resultLoadingEnd is RestResult.Loading)
                Assertions.assertFalse((resultLoadingEnd as RestResult.Loading).loading)
                coVerify(exactly = 1) {
                    mockCharactersRepository.getCharacters(page)
                }
                awaitComplete()
            }
        }

    @Test
    fun `when GetCharactersUseCase invoke, then return RestResult Error`() =
        runTest {
            coEvery {
                mockCharactersRepository.getCharacters(page)
            } returns RestResult.Error(error = mockNetworkError)

            val result = useCase(page)

            result.test {
                val resultLoadingStart = awaitItem()
                Assertions.assertTrue(resultLoadingStart is RestResult.Loading)
                Assertions.assertTrue((resultLoadingStart as RestResult.Loading).loading)
                val resultError = awaitItem()
                Assertions.assertTrue(resultError is RestResult.Error)
                Assertions.assertEquals(
                    (resultError as RestResult.Error).error.message,
                    "test_error_message"
                )
                val resultLoadingEnd = awaitItem()
                Assertions.assertTrue(resultLoadingEnd is RestResult.Loading)
                Assertions.assertFalse((resultLoadingEnd as RestResult.Loading).loading)
                coVerify(exactly = 1) {
                    mockCharactersRepository.getCharacters(page)
                }
                awaitComplete()
            }
        }
}
