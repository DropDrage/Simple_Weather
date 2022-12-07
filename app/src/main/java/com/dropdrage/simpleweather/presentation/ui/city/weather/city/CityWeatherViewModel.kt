package com.dropdrage.simpleweather.presentation.ui.city.weather.city

import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.presentation.model.ViewCity
import com.dropdrage.simpleweather.presentation.ui.city.weather.BaseCityWeatherViewModel
import com.dropdrage.simpleweather.presentation.util.TextMessage
import com.dropdrage.simpleweather.presentation.util.model_converter.DailyWeatherConverter
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
    dailyWeatherConverter: DailyWeatherConverter,
    private val cityRepository: CityRepository,
) : BaseCityWeatherViewModel(weatherRepository, hourWeatherConverter, dailyWeatherConverter) {

    var order: Int = ORDER_UNSET


    override suspend fun getCity(): ViewCity = when (val result = cityRepository.getCityWithOrder(order)) {
        is Resource.Success -> ViewCity(result.data.name.toTextMessage(), result.data.country.code.toTextMessage())
        is Resource.Error -> ViewCity(TextMessage.UnknownErrorMessage, TextMessage.UnknownErrorMessage)
    }

    override suspend fun tryLoadWeather() {
        when (val result = cityRepository.getCityWithOrder(order)) {
            is Resource.Success -> getWeatherForLocation(result.data.location)
            is Resource.Error -> _error.value = result.message.toTextMessageOrUnknownErrorMessage()
        }
    }
}