package com.dropdrage.simpleweather.presentation.util.extension

import android.widget.ImageView
import com.dropdrage.simpleweather.presentation.model.ViewWeatherType

fun ImageView.setWeather(weatherType: ViewWeatherType) {
    setImageResource(weatherType.iconRes)
    contentDescription = context.getString(weatherType.weatherDescriptionRes)
}