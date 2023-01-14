package com.dropdrage.simpleweather.presentation.ui.cities_weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.domain.city.City
import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.weather.use_case.UpdateAllCitiesWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesWeatherViewModel @Inject constructor(
    private val cityRepository: CityRepository,
    private val updateAllCitiesWeather: UpdateAllCitiesWeatherUseCase,
) : ViewModel() {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: Flow<List<City>> = _cities.asStateFlow()


    fun loadCities() {
        viewModelScope.launch {
            _cities.emit(cityRepository.getAllCitiesOrdered())
        }
    }

    fun updateWeather() {
        viewModelScope.launch { updateAllCitiesWeather.invoke() }
    }

}