package com.merveylcu.navigation

import androidx.navigation.NavOptions

sealed interface NavigationCommand {

    data class OpenScreen(
        val route: String,
        val navOptions: NavOptions? = null
    ) : NavigationCommand

    data object NavigateUp : NavigationCommand

    data class ShowLoading(val isLoading: Boolean) : NavigationCommand

    data class PopBackStack(val route: String, val inclusive: Boolean = false) : NavigationCommand
}