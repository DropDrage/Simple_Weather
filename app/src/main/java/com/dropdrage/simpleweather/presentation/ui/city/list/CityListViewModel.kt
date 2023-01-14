package com.dropdrage.simpleweather.presentation.ui.city.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.weather.use_case.GetCitiesWithWeatherUseCase
import com.dropdrage.simpleweather.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.presentation.util.model_converter.CityCurrentWeatherConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val cityCurrentWeatherConverter: CityCurrentWeatherConverter,
    private val getCitiesWithWeather: GetCitiesWithWeatherUseCase,
    private val cityRepository: CityRepository,
) : ViewModel() {

    private val _citiesCurrentWeathers = MutableStateFlow<List<ViewCityCurrentWeather>>(emptyList())
    val citiesCurrentWeathers: Flow<List<ViewCityCurrentWeather>> = _citiesCurrentWeathers.asStateFlow()


    fun loadCities() {
        viewModelScope.launch {
            _citiesCurrentWeathers.emitAll(
                getCitiesWithWeather().map { it.map(cityCurrentWeatherConverter::convertToView) }
            )
        }
    }

    fun saveOrder(orderedCities: List<ViewCityCurrentWeather>) {
        viewModelScope.launch {
            cityRepository.updateCitiesOrders(orderedCities.map { it.city })
        }
    }

    fun deleteCity(city: ViewCityCurrentWeather) {
        viewModelScope.launch {
            cityRepository.deleteCity(city.city)

            val newCitiesList = _citiesCurrentWeathers.value.toMutableList().apply {
                removeAt(indexOfFirst { it.city == city.city })
            }
            _citiesCurrentWeathers.value = newCitiesList
        }
    }
}