package com.dropdrage.simpleweather.feature.weather.presentation.ui.cities_weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.common.presentation.utils.Constants.FLOW_SUBSCRIPTION_TIME
import com.dropdrage.simpleweather.feature.city.domain.City
import com.dropdrage.simpleweather.feature.city.domain.CityRepository
import com.dropdrage.simpleweather.feature.weather.domain.use_case.UpdateAllCitiesWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CitiesWeatherViewModel @Inject constructor(
    cityRepository: CityRepository,
    private val updateAllCitiesWeather: UpdateAllCitiesWeatherUseCase,
) : ViewModel() {

    val cities: Flow<List<City>> = cityRepository.orderedCities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIME), emptyList())


    fun updateWeather() {
        viewModelScope.launch { updateAllCitiesWeather() }
    }

}