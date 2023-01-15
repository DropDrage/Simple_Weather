package com.dropdrage.simpleweather.presentation.ui.city.list

import com.dropdrage.simpleweather.presentation.model.ViewCityCurrentWeather

class CityCurrentWeatherDifferCallback : com.dropdrage.adapters.differ.DefaultDifferCallback<ViewCityCurrentWeather>() {
    override fun getChangePayload(oldItem: ViewCityCurrentWeather, newItem: ViewCityCurrentWeather): Any? {
        if (oldItem.isSame(newItem) && oldItem.currentWeather != newItem.currentWeather) {
            return newItem.currentWeather
        }

        return super.getChangePayload(oldItem, newItem)
    }
}