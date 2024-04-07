package com.merveylcu.core.error

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.merveylcu.navigation.Screens

fun NavGraphBuilder.composableErrorScreen() {
    composable(
        route = Screens.Error.route,
        arguments = Screens.Error.arguments
    ) {
        ErrorScreen()
    }
}
