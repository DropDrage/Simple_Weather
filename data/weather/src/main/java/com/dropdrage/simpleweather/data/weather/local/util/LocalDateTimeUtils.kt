package com.dropdrage.simpleweather.data.weather.local.util

import java.time.LocalDateTime

object LocalDateTimeUtils {

    val nowHourOnly: LocalDateTime
        get() = LocalDateTime.now().withMinute(0).withSecond(0)

}