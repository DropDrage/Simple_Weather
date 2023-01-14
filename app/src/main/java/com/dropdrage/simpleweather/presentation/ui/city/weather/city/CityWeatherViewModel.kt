package com.dropdrage.simpleweather.presentation.ui.city.weather.city

import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.presentation.ui.city.weather.BaseCityWeatherViewModel
import com.dropdrage.simpleweather.presentation.util.TextMessage
import com.dropdrage.simpleweather.presentation.util.model_converter.CurrentDayWeatherConverter
import com.dropdrage.simpleweather.presentation.util.model_converter.CurrentHourWeatherConverter
import com.dropdrage.simpleweather.presentation.util.model_converter.DailyWeatherConverter
import com.dropdrage.simpleweather.presentation.util.model_converter.HourWeatherConverter
import com.dropdrage.simpleweather.presentation.util.toTextMessage
import com.dropdrage.simpleweather.presentation.util.toTextMessageOrUnknownErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val ORDER_UNSET = -1

@HiltViewModel
class CityWeatherViewModel @Inject constructor(
    currentHourWeatherConverter: CurrentHourWeatherConverter,
    currentDayWeatherConverter: CurrentDayWeatherConverter,
    hourWeatherConverter: HourWeatherConverter,
    dailyWeatherConverter: DailyWeatherConverter,
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository,
) : BaseCityWeatherViewModel(
    currentHourWeatherConverter,
    currentDayWeatherConverter,
    hourWeatherConverter,
    dailyWeatherConverter
) {

    var order: Int = ORDER_UNSET


    override suspend fun getCity(): ViewCityTitle = when (val result = cityRepository.getCityWithOrder(order)) {
        is Resource.Success -> ViewCityTitle(result.data.name.toTextMessage(), result.data.country.code.toTextMessage())
        is Resource.Error -> ViewCityTitle(TextMessage.UnknownErrorMessage, TextMessage.UnknownErrorMessage)
    }

    override suspend fun tryLoadWeather() {
        when (val result = cityRepository.getCityWithOrder(order)) {
            is Resource.Success -> {
                val weatherResult = weatherRepository.getWeatherFromNow(result.data.location)
                processWeatherResult(weatherResult)
            }
            is Resource.Error -> _error.emit(result.message.toTextMessageOrUnknownErrorMessage())
        }
    }

}
