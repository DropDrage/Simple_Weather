package com.dropdrage.simpleweather.city_list.presentation.presentation.ui

import com.dropdrage.adapters.differ.DefaultDifferCallback
import com.dropdrage.simpleweather.city_list.presentation.presentation.model.ViewCityCurrentWeather

internal class CityCurrentWeatherDifferCallback : DefaultDifferCallback<ViewCityCurrentWeather>() {
    override fun getChangePayload(oldItem: ViewCityCurrentWeather, newItem: ViewCityCurrentWeather): Any? {
        if (oldItem.isSame(newItem) && oldItem.currentWeather != newItem.currentWeather) {
            return newItem.currentWeather
        }

        return super.getChangePayload(oldItem, newItem)
    }
}
