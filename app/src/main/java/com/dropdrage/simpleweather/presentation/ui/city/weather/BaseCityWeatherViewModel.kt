package com.dropdrage.simpleweather.presentation.ui.city.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.domain.location.Location
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.Weather
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.presentation.model.ViewCity
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.util.TextMessage
import com.dropdrage.simpleweather.presentation.util.model_converter.HourWeatherConverter
import com.dropdrage.simpleweather.presentation.util.toTextMessageOrUnknownErrorMessage
import kotlinx.coroutines.launch

abstract class BaseCityWeatherViewModel constructor(
    private val weatherRepository: WeatherRepository,
    private val hourWeatherConverter: HourWeatherConverter,
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _city = MutableLiveData<ViewCity>()
    val city: LiveData<ViewCity> = _city

    private val _currentWeather = MutableLiveData<ViewHourWeather>()
    val currentWeather: LiveData<ViewHourWeather> = _currentWeather

    private val _hourlyWeather = MutableLiveData<List<ViewHourWeather>>()
    val hourlyWeather: LiveData<List<ViewHourWeather>> = _hourlyWeather

    protected val _error = MutableLiveData<TextMessage>()
    val error: LiveData<TextMessage> = _error


    fun updateCityName() {
        viewModelScope.launch {
            _city.value = getCity()
        }
    }

    fun loadWeather() {
        performAsyncWithLoadingIndication {
            tryLoadWeather()
        }
    }

    protected abstract suspend fun getCity(): ViewCity

    protected abstract suspend fun tryLoadWeather()

    private fun performAsyncWithLoadingIndication(action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                action()
            } finally {
                _isLoading.value = false
            }
        }
    }

    protected suspend fun getWeatherForLocation(location: Location) {
        when (val result = weatherRepository.getWeather(location)) {
            is Resource.Success -> updateWeather(result.data)
            //ToDo don't pass exception messages to view
            is Resource.Error -> _error.value = result.message.toTextMessageOrUnknownErrorMessage()
        }
    }

    private fun updateWeather(weather: Weather) {
        _currentWeather.value = hourWeatherConverter.convertToView(weather.currentHourWeather)
        _hourlyWeather.value = weather.hourlyWeather.map(hourWeatherConverter::convertToView)
    }
}