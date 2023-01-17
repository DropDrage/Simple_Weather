package com.dropdrage.simpleweather.settings.model

internal data class ViewSetting(
    val label: String,
    var currentValue: String,
    val values: List<AnySetting>,
)
