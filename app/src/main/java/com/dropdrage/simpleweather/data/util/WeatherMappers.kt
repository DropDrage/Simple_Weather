package com.dropdrage.simpleweather.data.util

import com.dropdrage.simpleweather.data.dto.HourlyWeatherDto
import com.dropdrage.simpleweather.data.dto.WeatherDto
import com.dropdrage.simpleweather.domain.weather.DayWeather
import com.dropdrage.simpleweather.domain.weather.HourWeather
import com.dropdrage.simpleweather.domain.weather.Weather
import com.dropdrage.simpleweather.domain.weather.WeatherType

private typealias DomainWeather = Weather

private const val HOURS_IN_DAY = 24

fun WeatherDto.toDomainWeather(): DomainWeather {
    var dailyWeather = hourly.toWeatherPerHour().chunked(HOURS_IN_DAY).map {
        DayWeather(it)
    }
    return Weather(dailyWeather)
}

private fun HourlyWeatherDto.toWeatherPerHour(): List<HourWeather> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        val humidity = humidities[index]

        HourWeather(
            dateTime = time,
            temperatureCelsius = temperature,
            pressure = pressure,
            windSpeed = windSpeed,
            humidity = humidity,
            weatherType = codeToWeatherType(weatherCode),
        )
    }
}


private fun codeToWeatherType(code: Int): WeatherType = when (code) {
    0 -> WeatherType.ClearSky
    1 -> WeatherType.MainlyClear
    2 -> WeatherType.PartlyCloudy
    3 -> WeatherType.Overcast
    45 -> WeatherType.Foggy
    48 -> WeatherType.DepositingRimeFog
    51 -> WeatherType.LightDrizzle
    53 -> WeatherType.ModerateDrizzle
    55 -> WeatherType.DenseDrizzle
    56 -> WeatherType.LightFreezingDrizzle
    57 -> WeatherType.DenseFreezingDrizzle
    61 -> WeatherType.SlightRain
    63 -> WeatherType.ModerateRain
    65 -> WeatherType.HeavyRain
    66 -> WeatherType.LightFreezingDrizzle
    67 -> WeatherType.HeavyFreezingRain
    71 -> WeatherType.SlightSnowFall
    73 -> WeatherType.ModerateSnowFall
    75 -> WeatherType.HeavySnowFall
    77 -> WeatherType.SnowGrains
    80 -> WeatherType.SlightRainShowers
    81 -> WeatherType.ModerateRainShowers
    82 -> WeatherType.ViolentRainShowers
    85 -> WeatherType.SlightSnowShowers
    86 -> WeatherType.HeavySnowShowers
    95 -> WeatherType.ModerateThunderstorm
    96 -> WeatherType.SlightHailThunderstorm
    99 -> WeatherType.HeavyHailThunderstorm
    else -> WeatherType.ClearSky
}