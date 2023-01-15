package com.dropdrage.simpleweather.settings.model

data class ViewSetting(
    val label: String,
    var currentValue: String,
    val values: List<AnySetting>,
)
