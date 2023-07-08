package com.dropdrage.simpleweather.feature.weather.presentation.model

import com.dropdrage.common.presentation.util.TextMessage

internal data class ViewCityTitle(
    val city: TextMessage,
    val countryCode: TextMessage,
)
