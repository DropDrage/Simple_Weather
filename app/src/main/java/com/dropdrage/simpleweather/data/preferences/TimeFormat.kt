package com.dropdrage.simpleweather.data.preferences

import java.time.format.DateTimeFormatter

enum class TimeFormat(format: String) {
    H_12("h a"), H_24("HH:00");

    val formatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(format) }
}