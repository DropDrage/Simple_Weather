package com.dropdrage.simpleweather.presentation.ui.cities_weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.domain.city.City
import com.dropdrage.simpleweather.domain.city.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesWeatherViewModel @Inject constructor(
    private val cityRepository: CityRepository,
) : ViewModel() {

    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> = _cities


    fun loadCities() {
        viewModelScope.launch {
            _cities.value = cityRepository.getAllCitiesOrdered()
        }
    }
}