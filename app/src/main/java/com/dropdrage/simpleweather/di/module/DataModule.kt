package com.dropdrage.simpleweather.di.module

import android.content.Context
import com.dropdrage.simpleweather.data.location.DefaultLocationTracker
import com.dropdrage.simpleweather.domain.location.LocationTracker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataProviderModule {
    @[Provides Singleton]
    fun provideLocationClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
}

@Module
@InstallIn(SingletonComponent::class)
interface DataBindModule {
    @[Binds Singleton]
    fun bindLocationTracker(defaultTracker: DefaultLocationTracker): LocationTracker
}