package com.merveylcu.characters.presentation.extension

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.merveylcu.characters.presentation.CharactersScreen
import com.merveylcu.navigation.Screens


fun NavGraphBuilder.composableCharactersScreen() {
    composable(
        route = Screens.CharacterList.route,
    ) {
        CharactersScreen()
    }
}
