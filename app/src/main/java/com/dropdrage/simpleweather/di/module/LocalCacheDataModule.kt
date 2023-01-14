package com.dropdrage.simpleweather.di.module

import android.content.Context
import androidx.room.Room
import com.dropdrage.simpleweather.data.source.local.cache.CacheDatabase
import com.dropdrage.simpleweather.data.source.local.cache.dao.DayWeatherDao
import com.dropdrage.simpleweather.data.source.local.cache.dao.HourWeatherDao
import com.dropdrage.simpleweather.data.source.local.cache.dao.LocationDao
import com.dropdrage.simpleweather.data.source.local.cache.dao.WeatherCacheDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalCacheDataProviderModule {

    @[Provides Singleton]
    fun provideRoom(@ApplicationContext context: Context): CacheDatabase =
        Room.databaseBuilder(context, CacheDatabase::class.java, CacheDatabase.DATABASE_NAME).build()


    @[Provides Singleton]
    fun provideLocationDao(cacheDatabase: CacheDatabase): LocationDao = cacheDatabase.locationDao

    @[Provides Singleton]
    fun provideHourWeatherDao(cacheDatabase: CacheDatabase): HourWeatherDao = cacheDatabase.hourWeatherDao

    @[Provides Singleton]
    fun provideDayWeatherDao(cacheDatabase: CacheDatabase): DayWeatherDao = cacheDatabase.dayWeatherDao

    @[Provides Singleton]
    fun provideCacheDao(cacheDatabase: CacheDatabase): WeatherCacheDao = cacheDatabase.weatherCacheDao

}
