package com.dropdrage.simpleweather.presentation.util.format

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

class TimeFormatter @Inject constructor() {
    private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)


    fun formatAsHour(time: LocalDateTime): String = time.format(timeFormatter)
}