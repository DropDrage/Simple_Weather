package com.dropdrage.simpleweather.presentation.ui.city.weather

import android.graphics.Typeface
import android.widget.Toast
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.ItemHourWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.util.adapter.SimpleViewHolder
import java.util.*

class HourWeatherViewHolder(binding: ItemHourWeatherBinding) :
    SimpleViewHolder<ViewHourWeather, ItemHourWeatherBinding>(binding) {
    override fun bindData(value: ViewHourWeather) {
        binding.apply {
            val context = root.context
            val calendar = Calendar.getInstance()

            val isNotCurrentHourWeather = (calendar.get(Calendar.HOUR_OF_DAY) != value.dateTime.hour
                || calendar.get(Calendar.DAY_OF_MONTH) != value.dateTime.dayOfMonth)
            if (isNotCurrentHourWeather) {
                time.text = value.timeFormatted
                time.typeface = Typeface.DEFAULT
            } else {
                time.text = context.getString(R.string.weather_hourly_now)
                time.typeface = Typeface.DEFAULT_BOLD
            }
            weatherIcon.setImageResource(value.weatherType.iconRes)
            weatherIcon.contentDescription = context.getString(value.weatherType.weatherDescriptionRes)
            temperature.text = value.temperature

            root.setOnClickListener {
                Toast.makeText(it.context, value.weatherType.weatherDescriptionRes, Toast.LENGTH_SHORT).show()
            }
        }
    }
}