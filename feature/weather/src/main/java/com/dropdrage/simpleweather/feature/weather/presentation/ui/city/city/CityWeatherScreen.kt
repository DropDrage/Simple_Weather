package com.dropdrage.simpleweather.feature.weather.presentation.ui.city.weather.city

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dropdrage.simpleweather.feature.weather.presentation.ui.cities_weather.CitiesSharedViewModel
import com.dropdrage.simpleweather.feature.weather.presentation.ui.city.city.CityWeatherViewModel
import com.dropdrage.simpleweather.feature.weather.presentation.ui.city.weather.BaseCityWeatherScreen

@Composable
internal fun CityWeatherScreen(isVisible: Boolean, order: Int, citiesSharedViewModel: CitiesSharedViewModel) {
    val viewModel = hiltViewModel<CityWeatherViewModel>(key = order.toString()).apply { this.order = order }

    BaseCityWeatherScreen(isVisible = isVisible, viewModel = viewModel, citiesSharedViewModel = citiesSharedViewModel)
}
