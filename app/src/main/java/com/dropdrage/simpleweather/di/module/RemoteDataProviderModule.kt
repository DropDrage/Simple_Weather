package com.dropdrage.simpleweather.di.module

import com.dropdrage.simpleweather.BuildConfig
import com.dropdrage.simpleweather.data.repository.CitySearchRepositoryImpl
import com.dropdrage.simpleweather.data.repository.WeatherRepositoryImpl
import com.dropdrage.simpleweather.data.source.remote.SearchApi
import com.dropdrage.simpleweather.data.source.remote.WeatherApi
import com.dropdrage.simpleweather.di.adapter.LocalDateTimeAdapter
import com.dropdrage.simpleweather.domain.search.CitySearchRepository
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataProviderModule {
    private const val WEATHER_URL = "https://api.open-meteo.com/"
    private const val SEARCH_URL = "https://geocoding-api.open-meteo.com/"


    @Provides
    @Singleton
    fun provideLoggingHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi() = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(LocalDateTime::class.java, LocalDateTimeAdapter().nullSafe())
        .build()

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): Converter.Factory = MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    fun provideWeatherApi(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): WeatherApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .baseUrl(WEATHER_URL)
            .build()
            .create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideSearchApi(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): SearchApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .baseUrl(SEARCH_URL)
            .build()
            .create(SearchApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteBindModule {
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(weatherRepository: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindCitiesRepository(citySearchRepository: CitySearchRepositoryImpl): CitySearchRepository
}