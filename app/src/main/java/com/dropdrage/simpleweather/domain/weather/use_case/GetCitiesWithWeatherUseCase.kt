package com.dropdrage.simpleweather.domain.weather.use_case

import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.weather.current.CityCurrentWeather
import com.dropdrage.simpleweather.domain.weather.current.CurrentWeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class GetCitiesWithWeatherUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    private val currentWeatherRepository: CurrentWeatherRepository,
) {
    suspend operator fun invoke(): Flow<List<CityCurrentWeather>> = channelFlow {
        val allCities = cityRepository.getAllCitiesOrdered()
        val allCitiesNoWeather = allCities.map { CityCurrentWeather(it, null) }
        channel.send(allCitiesNoWeather)

        val allLocations = allCitiesNoWeather.map { it.city.location }
        val currentWeatherFlow = currentWeatherRepository.getCurrentWeather(allLocations)
        currentWeatherFlow.catch { channel.close() }
            .collect {
                val allCitiesProbablyWithWeather = it.mapIndexed { i, weather ->
                    if (weather != null) allCitiesNoWeather[i].copy(weather = weather)
                    else allCitiesNoWeather[i]
                }
                channel.send(allCitiesProbablyWithWeather)
            }

        channel.close()
    }
}
