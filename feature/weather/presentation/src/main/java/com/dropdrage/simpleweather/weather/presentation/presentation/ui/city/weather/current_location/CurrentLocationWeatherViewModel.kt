package com.dropdrage.simpleweather.weather.presentation.ui.city.weather.current_location

import com.dropdrage.common.presentation.util.ResourceMessage
import com.dropdrage.common.presentation.util.TextMessage
import com.dropdrage.simpleweather.weather.domain.location.LocationErrorResult
import com.dropdrage.simpleweather.weather.domain.location.LocationResult
import com.dropdrage.simpleweather.weather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.weather.presentation.R
import com.dropdrage.simpleweather.weather.presentation.domain.use_case.GetLocationUseCase
import com.dropdrage.simpleweather.weather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.weather.presentation.ui.city.weather.BaseCityWeatherViewModel
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.CurrentDayWeatherConverter
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.CurrentHourWeatherConverter
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.DailyWeatherConverter
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.HourWeatherConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
internal class CurrentLocationWeatherViewModel @Inject constructor(
    currentHourWeatherConverter: CurrentHourWeatherConverter,
    currentDayWeatherConverter: CurrentDayWeatherConverter,
    hourWeatherConverter: HourWeatherConverter,
    dailyWeatherConverter: DailyWeatherConverter,
    private val getLocation: GetLocationUseCase,
    private val weatherRepository: WeatherRepository,
) : BaseCityWeatherViewModel(
    currentHourWeatherConverter,
    currentDayWeatherConverter,
    hourWeatherConverter,
    dailyWeatherConverter
) {

    private val _locationObtainError = MutableSharedFlow<LocationErrorResult?>(replay = 1)
    val locationObtainingError: Flow<LocationErrorResult?> = _locationObtainError.asSharedFlow()


    override suspend fun getCity(): ViewCityTitle =
        ViewCityTitle(ResourceMessage(R.string.city_name_current_location), TextMessage.EmptyMessage)

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun tryLoadWeather() {
        getLocation().collect {
            when (it) {
                is LocationResult.Success ->
                    weatherRepository.getUpdatedWeatherFromNow(it.location).collect(::processWeatherResult)
                is LocationResult.NoPermission -> {
                    _locationObtainError.emit(it)
                    _error.emit(ResourceMessage(R.string.error_location_no_permission))
                }
                is LocationResult.GpsDisabled -> {
                    _locationObtainError.emit(it)
                    _error.emit(ResourceMessage(R.string.error_location_gps_disabled))
                }
                else -> _error.emit(ResourceMessage(R.string.error_location_no_location))
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun clearError() {
        _locationObtainError.resetReplayCache()
    }

}
