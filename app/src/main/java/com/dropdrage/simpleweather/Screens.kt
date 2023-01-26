package com.dropdrage.simpleweather

import com.dropdrage.simpleweather.weather.presentation.ui.cities_weather.CitiesWeatherFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

internal object Screens {
    fun CitiesWeather() = FragmentScreen { CitiesWeatherFragment() }
}
