package com.dropdrage.simpleweather.weather.presentation.util.format

import android.content.Context
import com.dropdrage.simpleweather.weather.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class DayNameFormat @Inject constructor(@ApplicationContext private val context: Context) {
    fun formatFromStartDay(day: LocalDate, startDay: LocalDate): String {
        val dayOffset = (day.toEpochDay() - startDay.toEpochDay()).toInt()
        return when (dayOffset) {
            0 -> context.getString(R.string.day_today_short)
            1 -> context.getString(R.string.day_tomorrow_short)
            else -> when (day.dayOfWeek!!) {
                DayOfWeek.MONDAY -> context.getString(R.string.week_day_monday_short)
                DayOfWeek.TUESDAY -> context.getString(R.string.week_day_tuesday_short)
                DayOfWeek.WEDNESDAY -> context.getString(R.string.week_day_wednesday_short)
                DayOfWeek.THURSDAY -> context.getString(R.string.week_day_thursday_short)
                DayOfWeek.FRIDAY -> context.getString(R.string.week_day_friday_short)
                DayOfWeek.SATURDAY -> context.getString(R.string.week_day_saturday_short)
                DayOfWeek.SUNDAY -> context.getString(R.string.week_day_sunday_short)
            }
        }
    }
}