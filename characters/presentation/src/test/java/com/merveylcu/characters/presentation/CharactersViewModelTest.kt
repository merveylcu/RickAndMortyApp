package com.merveylcu.characters.presentation

import app.cash.turbine.test
import com.merveylcu.characters.domain.model.CharacterDto
import com.merveylcu.characters.domain.model.CharactersDto
import com.merveylcu.characters.domain.usecase.GetCharactersUseCase
import com.merveylcu.characters.presentation.util.BaseTestUtil
import com.merveylcu.contract.RestResult
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.test.advanceUntilIdle
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
internal class CharactersViewModelTest : BaseTestUtil() {

    @MockK
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    private lateinit var viewModel: CharactersViewModel

    @BeforeEach
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainCoroutineExtension.dispatcher)
        viewModel = CharactersViewModel(
            getCharacters = getCharactersUseCase
        )
    }

    @AfterEach
    fun after() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `when loadCharacters call fun, then request getCharacters service is success`() = runTest {
        // Given
        val page = 1
        val response = CharactersDto(
            results = listOf(
                CharacterDto(
                    id = 1,
                    name = "Rick Sanchez"
                ),
                CharacterDto(
                    id = 2,
                    name = "Morty Smith"
                )
            )
        )
        every {
            getCharactersUseCase(page)
        } returns flow<RestResult<CharactersDto>> {
            emit(RestResult.Success(response))
        }.flowOn(mainCoroutineExtension.dispatcher)

        // When
        viewModel.loadCharacters()
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val uiState = awaitItem()
            Assertions.assertTrue(uiState.list.size == response.results?.size)
            Assertions.assertTrue(uiState.list[0].name == response.results?.get(1)?.name)
            Assertions.assertTrue(uiState.list[1].name == response.results?.get(0)?.name)
            Assertions.assertFalse(uiState.showError)
            cancel()
        }
    }

    @Test
    fun `when loadCharacters call fun, then request getCharacters service is error`() = runTest {
        // Given
        val page = 1
        every {
            getCharactersUseCase(page)
        } returns flow<RestResult<CharactersDto>> {
            emit(RestResult.Error(mockNetworkError))
        }.flowOn(mainCoroutineExtension.dispatcher)

        // When
        viewModel.loadCharacters()
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val uiState = awaitItem()
            Assertions.assertTrue(uiState.showError)
            cancel()
        }
    }

    @Test
    fun `when loadCharacters call fun, then request getCharacters service is loading`() = runTest {
        // Given
        val page = 1
        every {
            getCharactersUseCase(page)
        } returns flow<RestResult<CharactersDto>> {
            emit(RestResult.Loading(true))
        }.flowOn(mainCoroutineExtension.dispatcher)

        // When
        viewModel.loadCharacters()
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val uiState = awaitItem()
            Assertions.assertTrue(uiState.isLoading)
            Assertions.assertTrue(uiState.showError.not())
            cancel()
        }
    }

    @Test
    fun `when onSwipeLeft call fun, then removeItem`() = runTest {
        // Given
        val item = CharacterDto(
            id = 1,
            name = "Rick Sanchez"
        )

        // When
        viewModel.onSwipeLeft(item)
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val uiState = awaitItem()
            Assertions.assertTrue(uiState.list.indexOf(item) == -1)
            cancel()
        }
    }

    @Test
    fun `when onSwipeRight call fun, then removeItem`() = runTest {
        // Given
        val item = CharacterDto(
            id = 1,
            name = "Rick Sanchez"
        )

        // When
        viewModel.onSwipeRight(item)
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val uiState = awaitItem()
            Assertions.assertTrue(uiState.list.indexOf(item) == -1)
            cancel()
        }
    }
}