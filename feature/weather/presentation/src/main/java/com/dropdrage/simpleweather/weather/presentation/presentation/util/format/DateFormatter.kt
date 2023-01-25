package com.dropdrage.simpleweather.weather.presentation.util.format

import com.dropdrage.simpleweather.settings.GeneralPreferences
import java.time.LocalDate
import javax.inject.Inject

class DateFormatter @Inject constructor() {
    fun formatDayMonth(date: LocalDate): String = date.format(GeneralPreferences.dateFormat.formatter)
}