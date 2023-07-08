package com.dropdrage.simpleweather.feature.weather.presentation.ui.city

import android.os.Bundle
import com.dropdrage.simpleweather.feature.weather.presentation.ui.city.city.CityWeatherFragment
import com.dropdrage.simpleweather.feature.weather.presentation.ui.city.current_location.CurrentLocationWeatherFragment

internal object CityWeatherFragmentFactory {

    fun createForCurrentLocation() = CurrentLocationWeatherFragment()

    fun createForCityWithOrder(order: Int) = CityWeatherFragment().apply {
        arguments = Bundle().apply {
            putInt(CityWeatherFragment.ORDER_ARGUMENT, order)
        }
    }

}