package com.dropdrage.simpleweather.domain.weather

import java.time.LocalTime

data class Weather(val dailyWeather: List<DayWeather>) {

    val currentDayWeather: DayWeather = dailyWeather.first()

    val currentHourWeather: HourWeather
        get() {
            val currentHour = LocalTime.now().hour
            return currentDayWeather.weatherPerHour.find { it.dateTime.hour == currentHour }
                ?: error("Current hour is not found in current day: $currentHour. \n $currentDayWeather")
        }

    val hourlyWeather: List<HourWeather> = dailyWeather.flatMap { it.weatherPerHour }

}
