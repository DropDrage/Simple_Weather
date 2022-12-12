package com.dropdrage.simpleweather.presentation.ui.city.weather

import android.graphics.Typeface
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.ItemHourWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.util.adapter.ClickableViewHolder
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener
import com.dropdrage.simpleweather.presentation.util.extension.setWeather
import java.util.*

class HourWeatherViewHolder(
    binding: ItemHourWeatherBinding,
    onItemClick: OnItemClickListener<ViewHourWeather>,
) : ClickableViewHolder<ViewHourWeather, ItemHourWeatherBinding>(binding, onItemClick) {
    override fun bindData(value: ViewHourWeather) {
        binding.apply {
            val calendar = Calendar.getInstance()

            val isNotCurrentHourWeather = (calendar.get(Calendar.HOUR_OF_DAY) != value.dateTime.hour
                || calendar.get(Calendar.DAY_OF_MONTH) != value.dateTime.dayOfMonth)
            if (isNotCurrentHourWeather) {
                time.text = value.timeFormatted
                time.typeface = Typeface.DEFAULT
            } else {
                time.text = root.context.getString(R.string.weather_hourly_now)
                time.typeface = Typeface.DEFAULT_BOLD
            }
            weatherIcon.setWeather(value.weatherType)
            temperature.text = value.temperature
        }
    }
}