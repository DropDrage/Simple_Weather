package com.dropdrage.simpleweather.di.module

import com.dropdrage.simpleweather.data.city.repository.CityRepositoryImpl
import com.dropdrage.simpleweather.data.city.repository.CitySearchRepositoryImpl
import com.dropdrage.simpleweather.data.location.DefaultLocationTracker
import com.dropdrage.simpleweather.data.weather.repository.CurrentWeatherRepositoryImpl
import com.dropdrage.simpleweather.data.weather.repository.WeatherRepositoryImpl
import com.dropdrage.simpleweather.feature.city.domain.CityRepository
import com.dropdrage.simpleweather.feature.city.list.domain.weather.CurrentWeatherRepository
import com.dropdrage.simpleweather.feature.city.search.domain.CitySearchRepository
import com.dropdrage.simpleweather.feature.weather.domain.location.LocationTracker
import com.dropdrage.simpleweather.feature.weather.domain.weather.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @[Binds Singleton]
    fun bindCityRepository(cityRepository: CityRepositoryImpl): CityRepository

    @[Binds Singleton]
    fun bindWeatherRepository(weatherRepository: WeatherRepositoryImpl): WeatherRepository

    @[Binds Singleton]
    fun bindCurrentWeatherRepository(weatherRepository: CurrentWeatherRepositoryImpl): CurrentWeatherRepository

    @[Binds Singleton]
    fun bindCitySearchRepository(weatherRepository: CitySearchRepositoryImpl): CitySearchRepository

    @[Binds Singleton]
    fun bindLocationTracker(weatherRepository: DefaultLocationTracker): LocationTracker

}
