package com.merveylcu.core.error

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun ErrorScreen(viewModel: ErrorViewModel = hiltViewModel()) {
    Box(Modifier.fillMaxSize()) {
        Text(text = viewModel.message)
    }
}
