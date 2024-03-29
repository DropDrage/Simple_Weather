package com.dropdrage.simpleweather.feature.weather.presentation.util.format

import android.content.Context
import com.dropdrage.simpleweather.data.settings.GeneralPreferences
import com.dropdrage.simpleweather.feature.weather.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject

internal class TimeFormatter @Inject constructor(@ApplicationContext private val context: Context) {

    fun formatAsHourOrNow(time: LocalDateTime, isNow: Boolean): String =
        if (!isNow) formatAsHour(time)
        else context.getString(R.string.weather_hourly_now)

    private fun formatAsHour(time: LocalDateTime): String = time.format(GeneralPreferences.timeFormat.hourFormatter)

    fun formatTime(time: LocalDateTime): String = time.format(GeneralPreferences.timeFormat.timeFormatter)

}
