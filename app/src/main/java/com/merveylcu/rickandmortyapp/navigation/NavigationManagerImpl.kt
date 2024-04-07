package com.merveylcu.rickandmortyapp.navigation

import com.merveylcu.navigation.NavigationCommand
import com.merveylcu.navigation.NavigationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

internal class NavigationManagerImpl @Inject constructor(
) : CoroutineScope, NavigationManager {

    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()

    private val navigationCommandChannel = Channel<NavigationCommand>()
    override val navigationCommandFlow: Flow<NavigationCommand>
        get() = navigationCommandChannel.receiveAsFlow()

    override fun navigate(navigationCommand: NavigationCommand) {
        launch {
            navigationCommandChannel.send(navigationCommand)
        }
    }
}
