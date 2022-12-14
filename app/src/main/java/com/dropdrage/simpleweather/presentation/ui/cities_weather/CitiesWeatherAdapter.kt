package com.dropdrage.simpleweather.presentation.ui.cities_weather

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dropdrage.simpleweather.presentation.ui.city.weather.CityWeatherFragmentFactory
import kotlin.math.absoluteValue

private const val NO_CITIES = 0

private const val LOCATION_FRAGMENT_POSITION = 0
private const val LOCATION_FRAGMENT_COUNT = 1

class CitiesWeatherAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    var citiesCount: Int = NO_CITIES
        set(value) {
            val previousFragmentCount = fragmentsCount
            fragmentsCount = value + LOCATION_FRAGMENT_COUNT
            val fragmentCountChange = fragmentsCount - previousFragmentCount
            if (fragmentCountChange > 0) {
                notifyItemRangeInserted(previousFragmentCount, fragmentCountChange)
            } else {
                notifyItemRangeRemoved(fragmentsCount, previousFragmentCount.absoluteValue)
            }

            field = value
        }

    private var fragmentsCount: Int = LOCATION_FRAGMENT_COUNT


    override fun createFragment(position: Int): Fragment =
        if (position != LOCATION_FRAGMENT_POSITION)
            CityWeatherFragmentFactory.createForCityWithOrder(position - LOCATION_FRAGMENT_COUNT)
        else CityWeatherFragmentFactory.createForCurrentLocation()


    override fun getItemCount(): Int = fragmentsCount
}