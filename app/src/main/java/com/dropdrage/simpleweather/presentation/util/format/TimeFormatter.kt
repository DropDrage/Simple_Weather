package com.dropdrage.simpleweather.presentation.util.format

import android.content.Context
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.data.preferences.GeneralPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject

class TimeFormatter @Inject constructor(@ApplicationContext private val context: Context) {

    fun formatAsHour(time: LocalDateTime): String = time.format(GeneralPreferences.timeFormat.hourFormatter)

    fun formatAsHourOrNow(time: LocalDateTime, isNow: Boolean): String =
        if (!isNow) formatAsHour(time)
        else context.getString(R.string.weather_hourly_now)

    fun formatTime(time: LocalDateTime): String = time.format(GeneralPreferences.timeFormat.timeFormatter)

}
