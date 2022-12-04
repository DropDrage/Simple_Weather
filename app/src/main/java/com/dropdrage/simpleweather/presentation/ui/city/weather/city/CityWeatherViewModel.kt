package com.dropdrage.simpleweather.presentation.ui.city.weather.city

import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.presentation.ui.city.weather.BaseCityWeatherViewModel
import com.dropdrage.simpleweather.presentation.util.TextMessage
import com.dropdrage.simpleweather.presentation.util.model_converter.HourWeatherConverter
import com.dropdrage.simpleweather.presentation.util.toTextMessage
import com.dropdrage.simpleweather.presentation.util.toTextMessageOrUnknownErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val ORDER_UNSET = -1

@HiltViewModel
class CityWeatherViewModel @Inject constructor(
    weatherRepository: WeatherRepository,
    hourWeatherConverter: HourWeatherConverter,
    private val cityRepository: CityRepository,
) : BaseCityWeatherViewModel(weatherRepository, hourWeatherConverter) {

    var order: Int = ORDER_UNSET


    override suspend fun getCityName(): TextMessage = when (val result = cityRepository.getCityWithOrder(order)) {
        is Resource.Success -> result.data.name.toTextMessage()
        is Resource.Error -> TextMessage.UnknownErrorMessage
    }

    override suspend fun tryLoadWeather() {
        when (val result = cityRepository.getCityWithOrder(order)) {
            is Resource.Success -> getWeatherForLocation(result.data.location)
            is Resource.Error -> _error.value = result.message.toTextMessageOrUnknownErrorMessage()
        }
    }
}