package com.dropdrage.simpleweather.presentation.util.format

import android.content.Context
import com.dropdrage.simpleweather.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject

class TimeFormatter @Inject constructor(@ApplicationContext private val context: Context) {

    fun formatAsHour(time: LocalDateTime): String =
        time.format(com.dropdrage.simpleweather.settings_data.GeneralPreferences.timeFormat.hourFormatter)

    fun formatAsHourOrNow(time: LocalDateTime, isNow: Boolean): String =
        if (!isNow) formatAsHour(time)
        else context.getString(R.string.weather_hourly_now)

    fun formatTime(time: LocalDateTime): String =
        time.format(com.dropdrage.simpleweather.settings_data.GeneralPreferences.timeFormat.timeFormatter)

}
