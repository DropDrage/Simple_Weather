package com.dropdrage.simpleweather.domain.weather.use_case

import android.util.Log
import com.dropdrage.simpleweather.core.data.LogTags
import com.dropdrage.simpleweather.data.city.data.repository.CityRepository
import com.dropdrage.simpleweather.data.weather.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class UpdateAllCitiesWeatherUseCase @Inject constructor(
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
