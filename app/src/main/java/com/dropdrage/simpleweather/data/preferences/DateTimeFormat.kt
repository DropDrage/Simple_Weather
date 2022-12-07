package com.dropdrage.simpleweather.data.preferences

import java.time.format.DateTimeFormatter

enum class TimeFormat(hourFormat: String, timeFormat: String) {
    H_12("h a", "h:mm a"), H_24("HH:00", "HH:mm");

    val hourFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(hourFormat) }
    val timeFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(timeFormat) }
}

enum class DateFormat(format: String) {
    REVERSED("MM.dd"), STRAIGHT("dd.MM");

    val formatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(format) }
}