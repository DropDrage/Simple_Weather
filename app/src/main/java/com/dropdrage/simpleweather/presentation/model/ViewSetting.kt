package com.dropdrage.simpleweather.presentation.model

data class ViewSetting(
    val label: String,
    var currentValue: String,
    val values: List<AnySetting>,
)
