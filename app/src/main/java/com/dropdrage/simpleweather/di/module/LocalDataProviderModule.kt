package com.dropdrage.simpleweather.di.module

import android.content.Context
import androidx.room.Room
import com.dropdrage.simpleweather.data.source.local.AppDatabase
import com.dropdrage.simpleweather.data.source.local.dao.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataProviderModule {
    private const val APP_DATABASE_NAME = "main-database"


    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME).build()


    @Provides
    @Singleton
    fun provideCityDao(appDatabase: AppDatabase): CityDao = appDatabase.cityDao()
}