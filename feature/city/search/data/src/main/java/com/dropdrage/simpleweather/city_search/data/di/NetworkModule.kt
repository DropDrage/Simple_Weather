package com.dropdrage.simpleweather.city_search.data.di

import com.dropdrage.simpleweather.city_search.data.BuildConfig
import com.dropdrage.simpleweather.city_search.data.remote.SearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @[Provides Singleton]
    fun provideSearchApi(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): SearchApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .baseUrl(BuildConfig.SEARCH_URL)
            .build()
            .create(SearchApi::class.java)

}
