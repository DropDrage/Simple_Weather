package com.dropdrage.simpleweather.weather.presentation.ui.city.weather

import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.weather.databinding.ItemDayWeatherBinding
import com.dropdrage.simpleweather.weather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.weather.presentation.util.extension.setWeather

internal class DayWeatherViewHolder(
    binding: ItemDayWeatherBinding,
    onItemClick: OnItemClickListener<ViewDayWeather>,
) : ClickableViewHolder<ViewDayWeather, ItemDayWeatherBinding>(binding, onItemClick) {
    override fun bindData(value: ViewDayWeather) {
        binding.apply {
            day.text = value.dayTitle
            date.text = value.dateFormatted
            weatherIcon.setWeather(value.weatherType)
            temperatureMin.text = value.temperatureRange.start
            temperatureMax.text = value.temperatureRange.end
        }
    }
}