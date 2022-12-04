package com.dropdrage.simpleweather.domain.weather.use_case

import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.CityCurrentWeather
import com.dropdrage.simpleweather.domain.weather.CurrentWeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class GetCitiesWithWeatherUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    private val currentWeatherRepository: CurrentWeatherRepository,
) {
    suspend operator fun invoke() = channelFlow {
        val allCities = cityRepository.getAllCitiesOrdered()
        val allCitiesNoWeather = allCities.map { CityCurrentWeather(it, null) }
        channel.send(allCitiesNoWeather)

        val allCitiesProbablyWithWeather = allCitiesNoWeather.map {
            async {
                val weatherResult = currentWeatherRepository.getCurrentWeather(it.city.location)

                if (weatherResult is Resource.Success) it.copy(weather = weatherResult.data)
                else it
            }
        }.awaitAll()
        channel.send(allCitiesProbablyWithWeather)

        channel.close()
    }
}
