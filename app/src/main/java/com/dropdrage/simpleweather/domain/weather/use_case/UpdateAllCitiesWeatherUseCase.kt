package com.dropdrage.simpleweather.domain.weather.use_case

import android.util.Log
import com.dropdrage.simpleweather.data.util.LogTags
import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
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
