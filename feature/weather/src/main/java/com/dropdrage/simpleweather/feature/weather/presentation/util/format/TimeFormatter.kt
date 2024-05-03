package com.dropdrage.simpleweather.feature.weather.presentation.util.format

import android.content.Context
import com.dropdrage.simpleweather.data.settings.GeneralPreferences
import com.dropdrage.simpleweather.feature.weather.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

internal class TimeFormatter @Inject constructor(@ApplicationContext private val context: Context) {

    fun formatAsHourOrNow(time: LocalDateTime, isNow: Boolean): String =
        formatAsHourOrNow(time, isNow, GeneralPreferences.timeFormat.hourFormatter)

    private fun formatAsHourOrNow(time: LocalDateTime, isNow: Boolean, hourFormatter: DateTimeFormatter): String =
        if (!isNow) formatAsHour(time, hourFormatter)
        else context.getString(R.string.weather_hourly_now)

    private fun formatAsHour(time: LocalDateTime, hourFormatter: DateTimeFormatter): String = time.format(hourFormatter)

    fun formatTime(time: LocalDateTime): String = formatTime(time, GeneralPreferences.timeFormat.timeFormatter)
    private fun formatTime(time: LocalDateTime, timeFormatter: DateTimeFormatter): String = time.format(timeFormatter)


    @OptIn(ExperimentalContracts::class)
    fun <T : Any> bulkFormat(block: BulkTimeFormatter.() -> T): T {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        return BulkTimeFormatter().run(block)
    }


    inner class BulkTimeFormatter {

        private val hourFormatter: DateTimeFormatter = GeneralPreferences.timeFormat.hourFormatter
        private val timeFormatter: DateTimeFormatter = GeneralPreferences.timeFormat.timeFormatter

        fun formatAsHourOrNow(time: LocalDateTime, isNow: Boolean): String =
            formatAsHourOrNow(time, isNow, hourFormatter)

        fun formatTime(time: LocalDateTime): String = formatTime(time, timeFormatter)

    }

}
