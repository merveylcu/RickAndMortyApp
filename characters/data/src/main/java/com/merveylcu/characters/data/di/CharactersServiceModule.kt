package com.merveylcu.characters.data.di

import com.merveylcu.characters.data.CharactersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
internal object CharactersServiceModule {

    @Provides
    fun provideCharactersService(retrofit: Retrofit): CharactersService =
        retrofit.create(CharactersService::class.java)
}
