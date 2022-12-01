package com.dropdrage.simpleweather.presentation.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.domain.location.Location
import com.dropdrage.simpleweather.domain.location.LocationErrorResult
import com.dropdrage.simpleweather.domain.location.LocationResult
import com.dropdrage.simpleweather.domain.location.LocationTracker
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.Weather
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.util.HourWeatherConverter
import com.dropdrage.simpleweather.presentation.util.ResourceMessage
import com.dropdrage.simpleweather.presentation.util.TextMessage
import com.dropdrage.simpleweather.presentation.util.toTextMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val hourWeatherConverter: HourWeatherConverter,
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _currentWeather = MutableLiveData<ViewHourWeather>()
    val currentWeather: LiveData<ViewHourWeather> = _currentWeather

    private val _hourlyWeather = MutableLiveData<List<ViewHourWeather>>()
    val hourlyWeather: LiveData<List<ViewHourWeather>> = _hourlyWeather

    private val _error = MutableLiveData<TextMessage>()
    val error: LiveData<TextMessage> = _error

    private val _locationObtainError = MutableLiveData<LocationErrorResult>()
    val locationObtainingError: LiveData<LocationErrorResult> = _locationObtainError


    fun loadWeather() {
        performAsyncWithLoadingIndication {
            when (val locationResult = locationTracker.getCurrentLocation()) {
                is LocationResult.Success -> {
                    getWeatherForLocation(locationResult.location)
                }
                is LocationResult.NoPermission -> {
                    _locationObtainError.value = locationResult
                    _error.value = ResourceMessage(R.string.error_location_no_permission)
                }
                is LocationResult.GpsDisabled -> {
                    _locationObtainError.value = locationResult
                    _error.value = ResourceMessage(R.string.error_location_gps_disabled)
                }
                else -> locationTracker.requestLocationUpdate().collect {
                    if (it is LocationResult.Success) {
                        getWeatherForLocation(it.location)
                    } else {
                        _error.value = ResourceMessage(R.string.error_location_no_location)
                    }
                }
            }
        }
    }

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

    private suspend fun getWeatherForLocation(location: Location) {
        when (val result = weatherRepository.getWeather(location.latitude, location.longitude)) {
            is Resource.Success -> updateWeather(result.data)
            is Resource.Error -> _error.value = //ToDo don't pass exception messages to view
                result.message?.toTextMessage() ?: TextMessage.UnknownErrorMessage
        }
    }

    private fun updateWeather(weather: Weather) {
        _currentWeather.value = hourWeatherConverter.convertToView(weather.currentHourWeather)
        _hourlyWeather.value = weather.hourlyWeather.map(hourWeatherConverter::convertToView)
    }
}