package com.dropdrage.simpleweather.weather.presentation.ui.city.weather

import android.graphics.Typeface
import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.weather.presentation.databinding.ItemHourWeatherBinding
import com.dropdrage.simpleweather.weather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.weather.presentation.util.extension.setWeather

internal class HourWeatherViewHolder(
    binding: ItemHourWeatherBinding,
    onItemClick: OnItemClickListener<ViewHourWeather>,
) : ClickableViewHolder<ViewHourWeather, ItemHourWeatherBinding>(binding, onItemClick) {
    override fun bindData(value: ViewHourWeather) {
        binding.apply {
            time.text = value.timeFormatted
            time.typeface = if (value.isNow) Typeface.DEFAULT_BOLD else Typeface.DEFAULT

            weatherIcon.setWeather(value.weatherType)
            temperature.text = value.temperature
        }
    }
}
