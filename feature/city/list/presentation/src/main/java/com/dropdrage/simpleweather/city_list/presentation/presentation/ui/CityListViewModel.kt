package com.dropdrage.simpleweather.city_list.presentation.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.city.domain.CityRepository
import com.dropdrage.simpleweather.city_list.presentation.domain.use_case.ObserveCitiesWithWeatherUseCase
import com.dropdrage.simpleweather.city_list.presentation.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.city_list.presentation.presentation.utils.CityCurrentWeatherConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CityListViewModel @Inject constructor(
    observeCitiesWithWeather: ObserveCitiesWithWeatherUseCase,
    cityCurrentWeatherConverter: CityCurrentWeatherConverter,
    private val cityRepository: CityRepository,
) : ViewModel() {

    val citiesCurrentWeathers: Flow<List<ViewCityCurrentWeather>> = observeCitiesWithWeather()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        .map { it.map(cityCurrentWeatherConverter::convertToView) }


    fun saveOrder(orderedCities: List<ViewCityCurrentWeather>) {
        viewModelScope.launch { cityRepository.updateCitiesOrders(orderedCities.map { it.city }) }
    }

    fun deleteCity(city: ViewCityCurrentWeather) {
        viewModelScope.launch { cityRepository.deleteCity(city.city) }
    }

}
