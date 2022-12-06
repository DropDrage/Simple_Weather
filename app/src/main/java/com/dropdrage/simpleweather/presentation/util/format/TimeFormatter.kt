package com.dropdrage.simpleweather.presentation.util.format

import com.dropdrage.simpleweather.data.preferences.TimePreferences
import java.time.LocalDateTime
import javax.inject.Inject

class TimeFormatter @Inject constructor() {
    fun formatAsHour(time: LocalDateTime): String = time.format(TimePreferences.timeFormat.formatter)
}