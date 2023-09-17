package com.dropdrage.simpleweather.feature.weather.presentation.ui.cities_weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCityTitle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class CitiesSharedViewModel : ViewModel() {

    private val _currentCityTitle = MutableSharedFlow<ViewCityTitle>()
    val currentCityTitle: Flow<ViewCityTitle> = _currentCityTitle.asSharedFlow()

    fun setCityTitle(city: ViewCityTitle) {
        viewModelScope.launch {
            _currentCityTitle.emit(city)
        }
    }

}
