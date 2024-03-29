package com.dropdrage.simpleweather.feature.weather.presentation.ui.city.city

import com.dropdrage.common.domain.Resource
import com.dropdrage.common.presentation.util.TextMessage
import com.dropdrage.common.presentation.util.toTextMessage
import com.dropdrage.common.presentation.util.toTextMessageOrUnknownErrorMessage
import com.dropdrage.simpleweather.feature.city.domain.CityRepository
import com.dropdrage.simpleweather.feature.weather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.feature.weather.presentation.ui.city.BaseCityWeatherViewModel
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.CurrentDayWeatherConverter
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.CurrentHourWeatherConverter
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.DailyWeatherConverter
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.HourWeatherConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val ORDER_UNSET = -1

@HiltViewModel
internal class CityWeatherViewModel @Inject constructor(
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
        is Resource.Success -> ViewCityTitle(
            result.data.name.toTextMessage(),
            result.data.country.code.toTextMessage()
        )
        is Resource.Error -> ViewCityTitle(
            TextMessage.UnknownErrorMessage,
            TextMessage.UnknownErrorMessage
        )
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
