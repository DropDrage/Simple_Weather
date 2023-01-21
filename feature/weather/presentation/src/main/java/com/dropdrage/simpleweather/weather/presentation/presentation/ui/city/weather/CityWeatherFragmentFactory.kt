package com.dropdrage.simpleweather.weather.presentation.ui.city.weather

import android.os.Bundle
import com.dropdrage.simpleweather.weather.presentation.ui.city.weather.city.CityWeatherFragment
import com.dropdrage.simpleweather.weather.presentation.ui.city.weather.current_location.CurrentLocationWeatherFragment

internal object CityWeatherFragmentFactory {

    fun createForCurrentLocation() = CurrentLocationWeatherFragment()

    fun createForCityWithOrder(order: Int) = CityWeatherFragment().apply {
        arguments = Bundle().apply {
            putInt(CityWeatherFragment.ORDER_ARGUMENT, order)
        }
    }

}