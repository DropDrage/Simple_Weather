package com.dropdrage.simpleweather.feature.weather.presentation.util.format

import com.dropdrage.simpleweather.data.settings.GeneralPreferences
import java.time.LocalDate
import javax.inject.Inject

internal class DateFormatter @Inject constructor() {
    fun formatDayMonth(date: LocalDate): String = date.format(GeneralPreferences.dateFormat.formatter)
}
