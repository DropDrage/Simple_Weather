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
            if (isNotCurrentHourWeather(value)) {
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


    private companion object {
        val calendar = Calendar.getInstance()

        fun isNotCurrentHourWeather(value: ViewHourWeather) =
            calendar.get(Calendar.HOUR_OF_DAY) != value.hour
                || calendar.get(Calendar.DAY_OF_MONTH) != value.dayOfMonth
    }

}
