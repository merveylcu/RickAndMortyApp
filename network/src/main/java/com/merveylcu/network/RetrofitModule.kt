package com.merveylcu.network

import android.content.Context
import com.google.gson.Gson
import com.merveylcu.network.exception.NoInternetException
import com.merveylcu.network.interceptor.NoConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RetrofitModule {

    @Provides
    fun provideNoInternetInterceptor(
        @ApplicationContext context: Context
    ): NoConnectionInterceptor {
        return NoConnectionInterceptor(context)
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        return interceptor
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        noConnectionInterceptor: NoConnectionInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(TimeoutType.DEFAULT_CON_TIMEOUT.timeout, TimeUnit.SECONDS)
            .readTimeout(TimeoutType.DEFAULT_TIMEOUT.timeout, TimeUnit.SECONDS)
            .writeTimeout(TimeoutType.DEFAULT_TIMEOUT.timeout, TimeUnit.SECONDS)
            .followSslRedirects(true)
            .addInterceptor(noConnectionInterceptor)
            .addInterceptor(loggingInterceptor)

        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        val baseUrl = context.getString(R.string.base_url)

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    fun provideGsonConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create(Gson())
    }
}
