package com.dropdrage.simpleweather.presentation.ui.city.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.weather.use_case.GetCitiesWithWeatherUseCase
import com.dropdrage.simpleweather.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.presentation.util.model_converter.CityCurrentWeatherConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val cityCurrentWeatherConverter: CityCurrentWeatherConverter,
    private val getCitiesWithWeather: GetCitiesWithWeatherUseCase,
    private val cityRepository: CityRepository,
) : ViewModel() {

    private val _citiesCurrentWeathers = MutableLiveData<List<ViewCityCurrentWeather>>()
    val citiesCurrentWeathers: LiveData<out List<ViewCityCurrentWeather>> = _citiesCurrentWeathers


    fun loadCities() {
        viewModelScope.launch {
            getCitiesWithWeather.invoke()
                .collect {
                    _citiesCurrentWeathers.value = it.map(cityCurrentWeatherConverter::convertToView)
                }
        }
    }

    fun changeOrder(orderedCities: List<ViewCityCurrentWeather>) {
        viewModelScope.launch {
            cityRepository.updateCitiesOrders(orderedCities.map { it.city })
        }
    }

    fun deleteCity(city: ViewCityCurrentWeather) {
        viewModelScope.launch {
            cityRepository.deleteCity(city.city)
            _citiesCurrentWeathers.value = _citiesCurrentWeathers.value?.toMutableList()?.apply {
                remove(city)
            }
        }
    }
}