package com.dropdrage.simpleweather.di.module

import android.content.Context
import androidx.room.Room
import com.dropdrage.simpleweather.data.city.data.local.CityDatabase
import com.dropdrage.simpleweather.data.city.data.local.dao.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalDataProviderModule {

    @[Provides Singleton]
    fun provideRoom(@ApplicationContext context: Context): CityDatabase =
        Room.databaseBuilder(context, CityDatabase::class.java, CityDatabase.DATABASE_NAME).build()


    @[Provides Singleton]
    fun provideCityDao(cityDatabase: CityDatabase): CityDao = cityDatabase.cityDao

}
