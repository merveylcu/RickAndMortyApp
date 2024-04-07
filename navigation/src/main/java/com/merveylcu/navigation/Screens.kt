package com.merveylcu.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screens(open val route: String) {

    data object Error : Screens("error/{message}") {

        const val MESSAGE = "message"

        val arguments = listOf(navArgument(MESSAGE) {
            type =  NavType.StringType
            nullable = false
        })

        fun route(message: String) = route.replace("{$MESSAGE}", message)
    }

    data object CharacterList: Screens("character/list")
}
