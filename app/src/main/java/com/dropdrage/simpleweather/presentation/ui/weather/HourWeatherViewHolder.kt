package com.dropdrage.simpleweather.presentation.ui.weather

import android.widget.Toast
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.ItemHourWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.util.simple_adapter.BindableViewHolder
import java.util.*

class HourWeatherViewHolder(binding: ItemHourWeatherBinding) :
    BindableViewHolder<ViewHourWeather, ItemHourWeatherBinding>(binding) {
    override fun displayData(weather: ViewHourWeather) {
        binding.apply {
            val context = binding.root.context
            val calendar = Calendar.getInstance()

            val isNotCurrentHourWeather = (calendar.get(Calendar.HOUR_OF_DAY) != weather.dateTime.hour
                || calendar.get(Calendar.DAY_OF_MONTH) != weather.dateTime.dayOfMonth)
            time.text =
                if (isNotCurrentHourWeather) weather.timeFormatted
                else context.getString(R.string.weather_hourly_now)
            weatherIcon.setImageResource(weather.weatherType.iconRes)
            temperature.text = weather.temperature

            root.setOnClickListener {
                Toast.makeText(it.context, weather.weatherType.weatherDescriptionRes, Toast.LENGTH_SHORT).show()
            }
        }
    }
}