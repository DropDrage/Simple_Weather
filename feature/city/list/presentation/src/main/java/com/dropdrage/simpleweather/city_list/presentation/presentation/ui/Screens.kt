package com.dropdrage.simpleweather.city_list.presentation.presentation.ui

import com.dropdrage.simpleweather.city_search.presentation.ui.CitySearchFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

internal object Screens {
    fun CitySearch() = FragmentScreen { CitySearchFragment() }
}
