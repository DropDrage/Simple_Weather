package com.dropdrage.simpleweather.presentation.ui.city.weather

import android.graphics.Typeface
import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.databinding.ItemHourWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.util.extension.setWeather

class HourWeatherViewHolder(
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
