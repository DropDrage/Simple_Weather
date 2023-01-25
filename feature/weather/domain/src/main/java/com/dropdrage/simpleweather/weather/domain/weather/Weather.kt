package com.dropdrage.simpleweather.weather.domain.weather

import java.time.LocalTime

data class Weather(val dailyWeather: List<DayWeather>) {

    val currentDayWeather: DayWeather = dailyWeather.first()

    val currentHourWeather: HourWeather
        get() {
            val currentHour = LocalTime.now().hour
            return currentDayWeather.weatherPerHour.find { it.dateTime.hour == currentHour }
                ?: error("Current hour ($currentHour) is not found in current day: $currentDayWeather")
        }

    val hourlyWeather: List<HourWeather> = dailyWeather.flatMap { it.weatherPerHour }

}
