package com.dropdrage.simpleweather.presentation.util.format

import com.dropdrage.simpleweather.data.preferences.GeneralPreferences
import java.time.LocalDateTime
import javax.inject.Inject

class TimeFormatter @Inject constructor() {
    fun formatAsHour(time: LocalDateTime): String = time.format(GeneralPreferences.timeFormat.hourFormatter)

    fun formatTime(time: LocalDateTime): String = time.format(GeneralPreferences.timeFormat.timeFormatter)
}