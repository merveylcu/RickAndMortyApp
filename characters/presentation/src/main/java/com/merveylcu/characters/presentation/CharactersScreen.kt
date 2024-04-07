package com.merveylcu.characters.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.merveylcu.swipecard.SwipeCard

@Composable
internal fun CharactersScreen(viewModel: CharactersViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadCharacters()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
        if (uiState.showError) {
            Button(onClick = { viewModel.loadCharacters() }) {
                Text(text = stringResource(id = R.string.retry))
            }
        }

        uiState.list.forEach { item ->
            SwipeCard(
                item = item,
                actions = viewModel,
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.8f)
                            .background(Color.LightGray, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(25.dp),
                            model = item.image,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = item.image,
                            contentDescription = null
                        )
                        Text(
                            text = item.name.orEmpty(),
                            fontSize = 55.sp
                        )
                    }
                }
            )
        }

        if (viewModel.checkListIsEmpty()) {
            Text(
                text = stringResource(id = R.string.all_cards_swiped),
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
