package com.dropdrage.simpleweather.presentation.ui.city.weather.current_location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.domain.location.LocationErrorResult
import com.dropdrage.simpleweather.domain.location.LocationResult
import com.dropdrage.simpleweather.domain.location.LocationTracker
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.presentation.model.ViewCity
import com.dropdrage.simpleweather.presentation.ui.city.weather.BaseCityWeatherViewModel
import com.dropdrage.simpleweather.presentation.util.ResourceMessage
import com.dropdrage.simpleweather.presentation.util.TextMessage
import com.dropdrage.simpleweather.presentation.util.model_converter.CurrentDayWeatherConverter
import com.dropdrage.simpleweather.presentation.util.model_converter.DailyWeatherConverter
import com.dropdrage.simpleweather.presentation.util.model_converter.HourWeatherConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrentLocationWeatherViewModel @Inject constructor(
    weatherRepository: WeatherRepository,
    hourWeatherConverter: HourWeatherConverter,
    dailyWeatherConverter: DailyWeatherConverter,
    currentDayWeatherConverter: CurrentDayWeatherConverter,
    private val locationTracker: LocationTracker,
) : BaseCityWeatherViewModel(
    weatherRepository,
    hourWeatherConverter,
    dailyWeatherConverter,
    currentDayWeatherConverter
) {

    private val _locationObtainError = MutableLiveData<LocationErrorResult>()
    val locationObtainingError: LiveData<LocationErrorResult> = _locationObtainError


    override suspend fun getCity(): ViewCity =
        ViewCity(ResourceMessage(R.string.city_name_current_location), TextMessage.EmptyMessage)

    override suspend fun tryLoadWeather() {
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