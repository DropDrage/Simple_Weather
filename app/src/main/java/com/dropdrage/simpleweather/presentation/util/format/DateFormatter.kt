package com.dropdrage.simpleweather.presentation.util.format

import com.dropdrage.simpleweather.settings.data.GeneralPreferences
import java.time.LocalDate
import javax.inject.Inject

class DateFormatter @Inject constructor() {
    fun formatDayMonth(date: LocalDate): String = date.format(GeneralPreferences.dateFormat.formatter)
}