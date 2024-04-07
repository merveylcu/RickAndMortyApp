package com.merveylcu.rickandmortyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.merveylcu.characters.presentation.extension.composableCharactersScreen
import com.merveylcu.core.error.composableErrorScreen
import com.merveylcu.core.loading.LoadingDialog
import com.merveylcu.navigation.NavigationCommand
import com.merveylcu.navigation.NavigationManager
import com.merveylcu.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var isLoading by remember { mutableStateOf(false) }

            if (isLoading) {
                LoadingDialog()
            }

            NavHost(
                navController = navController,
                startDestination = Screens.CharacterList.route
            ) {
                composableErrorScreen()
                composableCharactersScreen()
            }

            LaunchedEffect(key1 = true) {
                navigationManager.navigationCommandFlow.collect { command ->
                    when (command) {
                        NavigationCommand.NavigateUp -> navController.navigateUp()
                        is NavigationCommand.ShowLoading -> isLoading = command.isLoading
                        is NavigationCommand.OpenScreen -> navController.navigate(
                            route = command.route,
                            navOptions = command.navOptions
                        )

                        is NavigationCommand.PopBackStack -> navController.popBackStack(
                            route = command.route,
                            inclusive = command.inclusive
                        )
                    }
                }
            }
        }
    }
}
