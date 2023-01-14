package com.dropdrage.simpleweather.domain.weather.use_case

import android.util.Log
import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.weather.current.CityCurrentWeather
import com.dropdrage.simpleweather.domain.weather.current.CurrentWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "ObserveWeather"

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveCitiesWithWeatherUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    private val currentWeatherRepository: CurrentWeatherRepository,
) {
    operator fun invoke(): Flow<List<CityCurrentWeather>> = channelFlow {
        withContext(Dispatchers.IO) {
            cityRepository.orderedCities
                .catch {
                    Log.e(TAG, it.message, it)
                    channel.close()
                }
                .map { cities -> cities.map { CityCurrentWeather(it, null) } }
                .flatMapLatest { allCitiesNoWeather ->
                    val allLocations = allCitiesNoWeather.map { it.city.location }
                    val currentWeatherFlow = currentWeatherRepository.getCurrentWeather(allLocations)
                    currentWeatherFlow.take(2)
                        .catch {
                            Log.e(TAG, it.message, it)
                            channel.close()
                        }
                        .map {
                            it.mapIndexed { i, weather ->
                                if (weather != null) allCitiesNoWeather[i].copy(weather = weather)
                                else allCitiesNoWeather[i]
                            }
                        }
                }
                .collect { allCitiesNoWeather -> channel.send(allCitiesNoWeather) }
        }
    }
}
