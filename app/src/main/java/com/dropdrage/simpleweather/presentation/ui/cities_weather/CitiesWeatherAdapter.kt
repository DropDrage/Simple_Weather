package com.dropdrage.simpleweather.presentation.ui.cities_weather

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dropdrage.simpleweather.presentation.ui.city.weather.CityWeatherFragmentFactory

private const val NO_CITIES = 0

private const val LOCATION_FRAGMENT_POSITION = 0
private const val LOCATION_FRAGMENT_COUNT = 1

class CitiesWeatherAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    var citiesCount: Int = NO_CITIES
        set(value) {
            fragmentsCount = value + LOCATION_FRAGMENT_COUNT
            field = value
        }

    private var fragmentsCount: Int = LOCATION_FRAGMENT_COUNT


    override fun createFragment(position: Int): Fragment =
        if (position != LOCATION_FRAGMENT_POSITION)
            CityWeatherFragmentFactory.createForCityWithOrder(position - LOCATION_FRAGMENT_COUNT)
        else CityWeatherFragmentFactory.createForCurrentLocation()


    override fun getItemCount(): Int = fragmentsCount
}