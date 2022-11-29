package com.dropdrage.simpleweather.di

import android.content.Context
import com.dropdrage.simpleweather.BuildConfig
import com.dropdrage.simpleweather.data.location.DefaultLocationTracker
import com.dropdrage.simpleweather.data.remote.WeatherApi
import com.dropdrage.simpleweather.domain.location.LocationTracker
import com.dropdrage.simpleweather.domain.weather.repository.WeatherRepository
import com.dropdrage.simpleweather.domain.weather.repository.WeatherRepositoryImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataProviderModule {
    private const val OPEN_WEATHER_URL = "https://api.open-meteo.com/"

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
    fun provideWeatherApi(okHttpClient: OkHttpClient, moshi: Moshi): WeatherApi = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(OPEN_WEATHER_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(WeatherApi::class.java)


    @Provides
    fun provideLocationClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindModule {
    @Binds
    @Singleton
    abstract fun bindLocationTracker(defaultTracker: DefaultLocationTracker): LocationTracker

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}