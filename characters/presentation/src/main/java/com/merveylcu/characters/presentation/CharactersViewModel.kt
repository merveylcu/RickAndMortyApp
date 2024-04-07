package com.merveylcu.characters.presentation

import androidx.lifecycle.viewModelScope
import com.merveylcu.characters.domain.model.CharacterDto
import com.merveylcu.characters.domain.usecase.GetCharactersUseCase
import com.merveylcu.contract.network.ErrorType
import com.merveylcu.contract.network.LoadingType
import com.merveylcu.contract.onError
import com.merveylcu.contract.onLoading
import com.merveylcu.contract.onSuccess
import com.merveylcu.core.BaseViewModel
import com.merveylcu.swipecard.SwipeCardActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class CharactersViewModel @Inject constructor(
    private val getCharacters: GetCharactersUseCase
) : BaseViewModel(), SwipeCardActions<CharacterDto> {

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState = _uiState.asStateFlow()

    fun loadCharacters() {
        val page = _uiState.value.lastPageIndex
        request(
            loadingType = LoadingType.Custom,
            errorType = ErrorType.Custom,
            call = { getCharacters(page = page) }
        ).onLoading { isLoading ->
            _uiState.update { uiState ->
                uiState.copy(
                    isLoading = isLoading,
                    showError = if (isLoading) false else uiState.showError
                )
            }
        }.onSuccess { characters ->
            _uiState.update { uiState ->
                uiState.copy(
                    showError = false,
                    list = characters.results.orEmpty().reversed() + uiState.list,
                    lastPageIndex = page + 1
                )
            }
        }.onError {
            _uiState.update { uiState ->
                uiState.copy(showError = true)
            }
        }.launchIn(viewModelScope)
    }

    override fun onSwipeLeft(item: CharacterDto) {
        loadNewPage()
        removeItem(item)
    }

    override fun onSwipeRight(item: CharacterDto) {
        loadNewPage()
        removeItem(item)
    }

    private fun removeItem(item: CharacterDto) {
        _uiState.update { uiState ->
            uiState.copy(
                list = uiState.list.toMutableList().apply {
                    remove(item)
                }
            )
        }
    }

    private fun loadNewPage() {
        if (_uiState.value.isLoading || _uiState.value.list.size > 5) {
            return
        }
        loadCharacters()
    }

    fun checkListIsEmpty() = with(_uiState.value) {
        isLoading.not() && showError.not() && list.isEmpty()
    }
}
