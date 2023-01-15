package com.dropdrage.simpleweather.presentation.util.format

import java.time.LocalDate
import javax.inject.Inject

class DateFormatter @Inject constructor() {
    fun formatDayMonth(date: LocalDate): String =
        date.format(com.dropdrage.simpleweather.settings_data.GeneralPreferences.dateFormat.formatter)
}