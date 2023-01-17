package com.dropdrage.simpleweather.presentation.ui.city.weather

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.common.presentation.util.TextMessage
import com.dropdrage.common.presentation.util.toTextMessageOrUnknownErrorMessage
import com.dropdrage.simpleweather.common.domain.CantObtainResourceException
import com.dropdrage.simpleweather.common.domain.Resource
import com.dropdrage.simpleweather.data.weather.domain.Weather
import com.dropdrage.simpleweather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.presentation.model.ViewCurrentDayWeather
import com.dropdrage.simpleweather.presentation.model.ViewCurrentHourWeather
import com.dropdrage.simpleweather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.util.model_converter.CurrentDayWeatherConverter
import com.dropdrage.simpleweather.presentation.util.model_converter.CurrentHourWeatherConverter
import com.dropdrage.simpleweather.presentation.util.model_converter.DailyWeatherConverter
import com.dropdrage.simpleweather.presentation.util.model_converter.HourWeatherConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.time.LocalDate
import java.time.LocalDateTime

abstract class BaseCityWeatherViewModel constructor(
    private val currentHourWeatherConverter: CurrentHourWeatherConverter,
    private val currentDayWeatherConverter: CurrentDayWeatherConverter,
    private val hourWeatherConverter: HourWeatherConverter,
    private val dailyWeatherConverter: DailyWeatherConverter,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading.asStateFlow()

    private val _cityTitle = MutableSharedFlow<ViewCityTitle>()
    val cityTitle: Flow<ViewCityTitle> = _cityTitle.asSharedFlow()

    private val _currentDayWeather = MutableSharedFlow<ViewCurrentDayWeather>()
    val currentDayWeather: Flow<ViewCurrentDayWeather> = _currentDayWeather.asSharedFlow()

    private val _currentHourWeather = MutableSharedFlow<ViewCurrentHourWeather>()
    val currentHourWeather: Flow<ViewCurrentHourWeather> = _currentHourWeather.asSharedFlow()

    private val _hourlyWeather = MutableSharedFlow<List<ViewHourWeather>>()
    val hourlyWeather: Flow<List<ViewHourWeather>> = _hourlyWeather.asSharedFlow()

    private val _dailyWeather = MutableSharedFlow<List<ViewDayWeather>>()
    val dailyWeather: Flow<List<ViewDayWeather>> = _dailyWeather.asSharedFlow()

    protected val _error = MutableStateFlow<TextMessage?>(null)
    val error: Flow<TextMessage?> = _error.asStateFlow()


    fun updateCityName() {
        viewModelScope.launch {
            _cityTitle.emit(getCity())
        }
    }

    fun loadWeather() {
        performAsyncWithLoadingIndication {
            tryLoadWeather()
        }
    }

    protected abstract suspend fun getCity(): ViewCityTitle

    protected abstract suspend fun tryLoadWeather()

    private inline fun performAsyncWithLoadingIndication(crossinline action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.emit(true)
                action()
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    protected suspend fun processWeatherResult(result: Resource<Weather>) {
        when (result) {
            is Resource.Success -> updateWeather(result.data)
            //ToDo don't pass exception messages to view
            is Resource.Error -> _error.emit(when (result.exception) {
                is CantObtainResourceException -> TextMessage.NoDataAvailableErrorMessage
                else -> result.message.toTextMessageOrUnknownErrorMessage()
            })
        }
    }

    private suspend fun updateWeather(weather: Weather) {
        val defaultViewModelScope = viewModelScope + Dispatchers.Default
        val viewDailyWeather = defaultViewModelScope.async {
            val now = LocalDate.now()
            weather.dailyWeather.map { dailyWeatherConverter.convertToView(it, now) }
        }
        val viewHourlyWeather = defaultViewModelScope.async {
            val now = LocalDateTime.now()
            weather.hourlyWeather.map { hourWeatherConverter.convertToView(it, now) }
        }

        _currentDayWeather.emit(currentDayWeatherConverter.convertToView(weather.currentDayWeather))
        _currentHourWeather.emit(currentHourWeatherConverter.convertToView(weather.currentHourWeather))

        _dailyWeather.emit(viewDailyWeather.await())
        _hourlyWeather.emit(viewHourlyWeather.await())
    }


    @CallSuper
    open fun clearErrors() {
        viewModelScope.launch {
            _error.emit(null)
        }
    }

}
