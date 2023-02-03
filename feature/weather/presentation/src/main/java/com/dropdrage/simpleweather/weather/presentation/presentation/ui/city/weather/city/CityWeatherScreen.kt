package com.dropdrage.simpleweather.weather.presentation.presentation.ui.city.weather.city

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dropdrage.simpleweather.weather.presentation.presentation.ui.city.weather.BaseCityWeatherScreen
import com.dropdrage.simpleweather.weather.presentation.ui.cities_weather.CitiesSharedViewModel
import com.dropdrage.simpleweather.weather.presentation.ui.city.weather.city.CityWeatherViewModel

@Composable
internal fun CityWeatherScreen(isVisible: Boolean, order: Int, citiesSharedViewModel: CitiesSharedViewModel) {
    val viewModel = viewModel<CityWeatherViewModel>(key = order.toString()).apply {
        this.order = order
    }

    BaseCityWeatherScreen(isVisible = isVisible, viewModel = viewModel, citiesSharedViewModel = citiesSharedViewModel)
}
