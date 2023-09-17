package com.dropdrage.simpleweather.feature.weather.presentation.model

import com.dropdrage.common.presentation.util.TextMessage
import com.dropdrage.common.presentation.util.toTextMessage

internal data class ViewCityTitle(
    val city: TextMessage,
    val countryCode: TextMessage,
) {
    constructor(city: String, countryCode: String) : this(city.toTextMessage(), countryCode.toTextMessage())
}
