package com.dropdrage.simpleweather.presentation.ui.city.weather.current_location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.domain.location.LocationErrorResult
import com.dropdrage.simpleweather.domain.location.LocationResult
import com.dropdrage.simpleweather.domain.location.use_case.GetLocationUseCase
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.presentation.model.ViewCityTitle
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
    private val getLocation: GetLocationUseCase,
) : BaseCityWeatherViewModel(
    weatherRepository,
    hourWeatherConverter,
    dailyWeatherConverter,
    currentDayWeatherConverter
) {

    private val _locationObtainError = MutableLiveData<LocationErrorResult?>()
    val locationObtainingError: LiveData<LocationErrorResult?> = _locationObtainError


    override suspend fun getCity(): ViewCityTitle =
        ViewCityTitle(ResourceMessage(R.string.city_name_current_location), TextMessage.EmptyMessage)

    override suspend fun tryLoadWeather() {
        getLocation().collect {
            when (it) {
                is LocationResult.Success -> getWeatherForLocation(it.location)
                is LocationResult.NoPermission -> {
                    _locationObtainError.value = it
                    _error.value = ResourceMessage(R.string.error_location_no_permission)
                }
                is LocationResult.GpsDisabled -> {
                    _locationObtainError.value = it
                    _error.value = ResourceMessage(R.string.error_location_gps_disabled)
                }
                else -> _error.value = ResourceMessage(R.string.error_location_no_location)
            }
        }
    }


    override fun clearErrors() {
        super.clearErrors()
        _locationObtainError.value = null
    }

}
