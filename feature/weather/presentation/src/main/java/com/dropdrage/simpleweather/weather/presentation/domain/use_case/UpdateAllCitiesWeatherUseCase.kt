package com.dropdrage.simpleweather.weather.presentation.domain.use_case

import android.util.Log
import com.dropdrage.simpleweather.city_list.domain.city.CityRepository
import com.dropdrage.simpleweather.core.utils.LogTags
import com.dropdrage.simpleweather.weather.domain.weather.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

internal class UpdateAllCitiesWeatherUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            try {
                cityRepository.getAllCitiesOrdered().asFlow()
                    .collect { weatherRepository.updateWeather(it.location) }
            } catch (e: IOException) {
                Log.e(LogTags.WEATHER, "Update error: ${e.message}", e)
            }
        }
    }
}
