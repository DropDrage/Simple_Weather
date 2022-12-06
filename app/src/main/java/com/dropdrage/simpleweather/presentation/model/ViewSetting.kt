package com.dropdrage.simpleweather.presentation.model

data class ViewSetting(
    val label: String,
    val currentValue: String,
    val values: List<AnySetting>,
)
