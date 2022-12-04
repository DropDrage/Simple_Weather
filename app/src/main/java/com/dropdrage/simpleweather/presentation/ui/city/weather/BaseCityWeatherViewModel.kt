package com.dropdrage.simpleweather.presentation.ui.city.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.domain.location.Location
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.Weather
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.util.TextMessage
import com.dropdrage.simpleweather.presentation.util.model_converter.HourWeatherConverter
import com.dropdrage.simpleweather.presentation.util.toTextMessageOrUnknownErrorMessage
import kotlinx.coroutines.launch

abstract class BaseCityWeatherViewModel constructor(
    protected val weatherRepository: WeatherRepository,
    protected val hourWeatherConverter: HourWeatherConverter,
) : ViewModel() {

    protected val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _cityName = MutableLiveData<TextMessage>()
    val cityName: LiveData<TextMessage> = _cityName

    protected val _currentWeather = MutableLiveData<ViewHourWeather>()
    val currentWeather: LiveData<ViewHourWeather> = _currentWeather

    protected val _hourlyWeather = MutableLiveData<List<ViewHourWeather>>()
    val hourlyWeather: LiveData<List<ViewHourWeather>> = _hourlyWeather

    protected val _error = MutableLiveData<TextMessage>()
    val error: LiveData<TextMessage> = _error


    fun loadData() {
        performAsyncWithLoadingIndication {
            _cityName.value = getCityName()
            tryLoadWeather()
        }
    }

    protected abstract suspend fun getCityName(): TextMessage

    protected abstract suspend fun tryLoadWeather()

    protected fun performAsyncWithLoadingIndication(action: suspend () -> Unit) {
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

    protected fun updateWeather(weather: Weather) {
        _currentWeather.value = hourWeatherConverter.convertToView(weather.currentHourWeather)
        _hourlyWeather.value = weather.hourlyWeather.map(hourWeatherConverter::convertToView)
    }
}