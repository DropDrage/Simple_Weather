package com.dropdrage.simpleweather.presentation.ui.cities_weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.data.city.data.repository.CityRepository
import com.dropdrage.simpleweather.data.city.domain.City
import com.dropdrage.simpleweather.domain.weather.use_case.UpdateAllCitiesWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesWeatherViewModel @Inject constructor(
    cityRepository: CityRepository,
    private val updateAllCitiesWeather: UpdateAllCitiesWeatherUseCase,
) : ViewModel() {

    val cities: Flow<List<City>> =
        cityRepository.orderedCities.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun updateWeather() {
        viewModelScope.launch { updateAllCitiesWeather.invoke() }
    }

}