package com.merveylcu.characters.data.di

import com.merveylcu.characters.data.CharactersRepositoryImpl
import com.merveylcu.characters.domain.repository.CharactersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface CharactersRepositoryModule {

    @Binds
    fun bindCharactersRepository(
        repository: CharactersRepositoryImpl
    ): CharactersRepository
}
