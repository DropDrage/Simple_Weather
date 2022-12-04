package com.dropdrage.simpleweather.presentation.ui.city.list

import com.dropdrage.simpleweather.databinding.ItemCityBinding
import com.dropdrage.simpleweather.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.presentation.util.adapter.SimpleViewHolder

class CityCurrentWeatherViewHolder constructor(
    binding: ItemCityBinding,
    private val onDeleteClicked: (ViewCityCurrentWeather) -> Unit,
) : SimpleViewHolder<ViewCityCurrentWeather, ItemCityBinding>(binding) {
    override fun bindData(value: ViewCityCurrentWeather) {
        binding.apply {
            city.text = value.city.name
            countryCode.text = value.city.country.code

            weather.setImageResource(value.currentWeather.weatherType.iconRes)
            temperature.text = value.currentWeather.temperature

            delete.setOnClickListener { onDeleteClicked(value) }
        }
    }
}