package com.dropdrage.simpleweather.feature.weather.presentation.model

import com.dropdrage.common.domain.Range
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import java.time.LocalDate

internal data class ViewDayWeather(
    private val date: LocalDate,
    val dayTitle: String,
    val dateFormatted: String,
    val weatherType: ViewWeatherType,
    val temperatureRange: Range<String>,
)
