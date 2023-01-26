package com.dropdrage.simpleweather.weather.presentation.presentation.ui

import com.dropdrage.simpleweather.city_list.presentation.presentation.ui.CityListFragment
import com.dropdrage.simpleweather.settings.presentation.SettingsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

internal object Screens {
    fun CityList() = FragmentScreen { CityListFragment() }
    fun Settings() = FragmentScreen { SettingsFragment() }
}
