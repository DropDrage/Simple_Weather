package com.dropdrage.simpleweather.data.city_search.di

import com.dropdrage.simpleweather.data.city_search.BuildConfig
import com.dropdrage.simpleweather.data.city_search.remote.SearchApi
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
internal object DatabaseModule {

    @[Provides Singleton]
    fun provideSearchApi(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): SearchApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .baseUrl(BuildConfig.SEARCH_URL)
            .build()
            .create(SearchApi::class.java)

}
