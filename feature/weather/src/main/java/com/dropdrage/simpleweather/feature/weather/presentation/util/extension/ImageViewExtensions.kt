package com.dropdrage.simpleweather.feature.weather.presentation.util.extension

import android.widget.ImageView
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType

internal fun ImageView.setWeather(weatherType: ViewWeatherType) {
    setImageResource(weatherType.iconRes)
    contentDescription = context.getString(weatherType.weatherDescriptionRes)
}
