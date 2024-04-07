package com.merveylcu.rickandmortyapp.navigation

import com.merveylcu.navigation.NavigationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface NavigationManagerModule {

    @ActivityRetainedScoped
    @Binds
    fun bindNavigationManager(impl: NavigationManagerImpl): NavigationManager
}
