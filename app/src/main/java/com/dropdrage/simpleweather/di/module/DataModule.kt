package com.dropdrage.simpleweather.di.module

import com.dropdrage.simpleweather.city_list.data.data.repository.CityRepositoryImpl
import com.dropdrage.simpleweather.city_list.domain.city.CityRepository
import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeatherRepository
import com.dropdrage.simpleweather.city_search.data.repository.CitySearchRepositoryImpl
import com.dropdrage.simpleweather.city_search.domain.CitySearchRepository
import com.dropdrage.simpleweather.data.location.DefaultLocationTracker
import com.dropdrage.simpleweather.weather.data.repository.CurrentWeatherRepositoryImpl
import com.dropdrage.simpleweather.weather.data.repository.WeatherRepositoryImpl
import com.dropdrage.simpleweather.weather.domain.location.LocationTracker
import com.dropdrage.simpleweather.weather.domain.weather.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindCityRepository(cityRepository: CityRepositoryImpl): CityRepository

    @Binds
    @Singleton
    fun bindWeatherRepository(weatherRepository: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    fun bindCurrentWeatherRepository(weatherRepository: CurrentWeatherRepositoryImpl): CurrentWeatherRepository

    @Binds
    @Singleton
    fun bindCitySearchRepository(weatherRepository: CitySearchRepositoryImpl): CitySearchRepository

    @Binds
    @Singleton
    fun bindLocationTracker(weatherRepository: DefaultLocationTracker): LocationTracker

}