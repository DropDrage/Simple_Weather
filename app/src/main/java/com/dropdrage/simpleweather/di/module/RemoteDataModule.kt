package com.dropdrage.simpleweather.di.module

import android.content.Context
import com.dropdrage.simpleweather.BuildConfig
import com.dropdrage.simpleweather.data.repository.CitySearchRepositoryImpl
import com.dropdrage.simpleweather.data.repository.CurrentWeatherRepositoryImpl
import com.dropdrage.simpleweather.data.repository.WeatherRepositoryImpl
import com.dropdrage.simpleweather.data.source.remote.SearchApi
import com.dropdrage.simpleweather.data.source.remote.WeatherApi
import com.dropdrage.simpleweather.di.CacheInterceptor
import com.dropdrage.simpleweather.di.adapter.ApiSupportedParamFactory
import com.dropdrage.simpleweather.di.adapter.LocalDateAdapter
import com.dropdrage.simpleweather.di.adapter.LocalDateTimeAdapter
import com.dropdrage.simpleweather.domain.city.search.CitySearchRepository
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.domain.weather.current.CurrentWeatherRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataProviderModule {

    private const val CACHE_SIZE = 6 * 1024 * 1024L


    @Provides
    @Singleton
    fun provideLoggingHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cache = Cache(context.cacheDir, CACHE_SIZE)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(CacheInterceptor())
//            .addInterceptor(ForceCacheInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi() = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(LocalDate::class.java, LocalDateAdapter().nullSafe())
        .add(LocalDateTime::class.java, LocalDateTimeAdapter().nullSafe())
        .build()

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): Converter.Factory = MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    fun provideWeatherApi(okHttpClient: OkHttpClient, jsonConverterFactory: Converter.Factory): WeatherApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(ApiSupportedParamFactory())
            .addConverterFactory(jsonConverterFactory)
            .baseUrl(BuildConfig.WEATHER_URL)
            .build()
            .create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideSearchApi(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): SearchApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .baseUrl(BuildConfig.SEARCH_URL)
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
    abstract fun bindCurrentWeatherRepository(weatherRepository: CurrentWeatherRepositoryImpl): CurrentWeatherRepository

    @Binds
    @Singleton
    abstract fun bindCitySearchRepository(citySearchRepository: CitySearchRepositoryImpl): CitySearchRepository
}
