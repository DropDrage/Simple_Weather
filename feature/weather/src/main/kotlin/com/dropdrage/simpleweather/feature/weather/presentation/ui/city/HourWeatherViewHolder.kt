package com.dropdrage.simpleweather.feature.weather.presentation.ui.city

import android.graphics.Typeface
import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.feature.weather.databinding.ItemHourWeatherBinding
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.feature.weather.presentation.util.extension.setWeather
import com.dropdrage.util.extension.implicitAccess

internal class HourWeatherViewHolder(
    binding: ItemHourWeatherBinding,
    onItemClick: OnItemClickListener<ViewHourWeather>,
) : ClickableViewHolder<ViewHourWeather, ItemHourWeatherBinding>(binding, onItemClick) {
    override fun bindData(value: ViewHourWeather) = binding.implicitAccess {
        time.text = value.timeFormatted
        time.typeface = if (value.isNow) Typeface.DEFAULT_BOLD else Typeface.DEFAULT

        weatherIcon.setWeather(value.weatherType)
        temperature.text = value.temperature
    }
}
