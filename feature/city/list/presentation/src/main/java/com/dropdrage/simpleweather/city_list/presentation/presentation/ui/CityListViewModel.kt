package com.dropdrage.simpleweather.city_list.presentation.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.CityRepository
import com.dropdrage.simpleweather.city_list.presentation.domain.use_case.ObserveCitiesWithWeatherUseCase
import com.dropdrage.simpleweather.city_list.presentation.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.city_list.presentation.presentation.utils.CityCurrentWeatherConverter
import com.dropdrage.simpleweather.city_list.presentation.presentation.utils.findIndexedOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CityListViewModel @Inject constructor(
    observeCitiesWithWeather: ObserveCitiesWithWeatherUseCase,
    private val cityCurrentWeatherConverter: CityCurrentWeatherConverter,
    private val cityRepository: CityRepository,
) : ViewModel() {

    private val _cityCurrentWeathers: StateFlow<List<ViewCityCurrentWeather>> = observeCitiesWithWeather()
        .map { it.map(cityCurrentWeatherConverter::convertToView) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val citiesCurrentWeathers: Flow<List<ViewCityCurrentWeather>> = _cityCurrentWeathers


    fun saveOrder(orderedCities: List<ViewCityCurrentWeather>) {
        viewModelScope.launch {
            val isOrderChanged = _cityCurrentWeathers.value.findIndexedOrNull { i, item ->
                orderedCities[i] != item
            } != null
            if (isOrderChanged) {
                cityRepository.updateCitiesOrders(orderedCities.map { it.city })
            }
        }
    }

    fun deleteCity(city: City) {
        viewModelScope.launch { cityRepository.deleteCity(city) }
    }

}
