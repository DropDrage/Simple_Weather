package com.dropdrage.simpleweather.domain.weather

import java.time.LocalDateTime

data class Weather(private val dailyWeather: List<DayWeather>) {

    val currentDayWeather: DayWeather
        get() = dailyWeather.first()

    val currentHourWeather: HourWeather
        get() {
            val currentDay = dailyWeather.first()
            val currentHour = LocalDateTime.now().hour
            return currentDay.weatherPerHour.find { it.dateTime.hour == currentHour }
                ?: error("Current hour is not found in current day: $currentHour. \n $currentDay")
        }

    val hourlyWeather: List<HourWeather>
        get() = dailyWeather.flatMap { it.weatherPerHour }

}
