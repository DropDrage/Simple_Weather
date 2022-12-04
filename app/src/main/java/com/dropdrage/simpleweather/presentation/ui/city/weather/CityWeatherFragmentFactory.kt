package com.dropdrage.simpleweather.presentation.ui.city.weather

import android.os.Bundle
import com.dropdrage.simpleweather.presentation.ui.city.weather.city.CityWeatherFragment
import com.dropdrage.simpleweather.presentation.ui.city.weather.current_location.CurrentLocationWeatherFragment

object CityWeatherFragmentFactory {
    fun createForCurrentLocation(): BaseCityWeatherFragment<*> = CurrentLocationWeatherFragment()

    fun createForCityWithOrder(order: Int) = CityWeatherFragment().apply {
        arguments = Bundle().apply {
            putInt(CityWeatherFragment.ORDER_ARGUMENT, order)
        }
    }
}