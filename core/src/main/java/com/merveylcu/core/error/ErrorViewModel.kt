package com.merveylcu.core.error

import androidx.lifecycle.SavedStateHandle
import com.merveylcu.core.BaseViewModel
import com.merveylcu.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ErrorViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle
) : BaseViewModel() {

    val message by lazy { stateHandle.get<String>(Screens.Error.MESSAGE).orEmpty() }
}
