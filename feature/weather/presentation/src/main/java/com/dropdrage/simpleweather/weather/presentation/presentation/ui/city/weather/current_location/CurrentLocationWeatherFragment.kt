package com.dropdrage.simpleweather.weather.presentation.ui.city.weather.current_location

import android.os.Bundle
import android.view.View
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.weather.presentation.presentation.ui.city.weather.current_location.CurrentLocationWeatherScreen
import com.dropdrage.simpleweather.weather.presentation.ui.city.weather.BaseCityWeatherFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CurrentLocationWeatherFragment :
    BaseCityWeatherFragment<CurrentLocationWeatherViewModel>(CurrentLocationWeatherViewModel::class) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.content.setContent {
            ComposeMaterial3Theme {
                CurrentLocationWeatherScreen(citiesViewModelOwner = requireParentFragment())
            }
        }
    }
}
