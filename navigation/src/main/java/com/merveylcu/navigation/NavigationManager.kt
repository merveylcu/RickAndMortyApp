package com.merveylcu.navigation

import kotlinx.coroutines.flow.Flow

interface NavigationManager {

    val navigationCommandFlow: Flow<NavigationCommand>

    fun navigate(navigationCommand: NavigationCommand)
}