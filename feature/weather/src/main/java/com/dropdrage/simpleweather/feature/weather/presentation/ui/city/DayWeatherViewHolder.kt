package com.dropdrage.simpleweather.feature.weather.presentation.ui.city

import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.feature.weather.databinding.ItemDayWeatherBinding
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.feature.weather.presentation.util.extension.setWeather
import com.dropdrage.util.extension.implicitAccess

internal class DayWeatherViewHolder(
    binding: ItemDayWeatherBinding,
    onItemClick: OnItemClickListener<ViewDayWeather>,
) : ClickableViewHolder<ViewDayWeather, ItemDayWeatherBinding>(binding, onItemClick) {
    override fun bindData(value: ViewDayWeather) = binding.implicitAccess {
        day.text = value.dayTitle
        date.text = value.dateFormatted
        weatherIcon.setWeather(value.weatherType)
        temperatureMin.text = value.temperatureRange.start
        temperatureMax.text = value.temperatureRange.end
    }
}
