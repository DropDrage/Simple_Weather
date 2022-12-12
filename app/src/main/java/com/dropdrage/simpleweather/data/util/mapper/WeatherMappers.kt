package com.dropdrage.simpleweather.data.util.mapper

import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherResponseDto
import com.dropdrage.simpleweather.data.source.remote.dto.DailyWeatherDto
import com.dropdrage.simpleweather.data.source.remote.dto.HourlyWeatherDto
import com.dropdrage.simpleweather.data.source.remote.dto.WeatherResponseDto
import com.dropdrage.simpleweather.data.util.WeatherUnitsConverter.convertPrecipitationIfApiDontSupport
import com.dropdrage.simpleweather.data.util.WeatherUnitsConverter.convertPressureIfApiDontSupport
import com.dropdrage.simpleweather.data.util.WeatherUnitsConverter.convertTemperatureIfApiDontSupport
import com.dropdrage.simpleweather.data.util.WeatherUnitsConverter.convertVisibilityIfApiDontSupport
import com.dropdrage.simpleweather.data.util.WeatherUnitsConverter.convertWindSpeedIfApiDontSupport
import com.dropdrage.simpleweather.domain.util.Range
import com.dropdrage.simpleweather.domain.weather.DayWeather
import com.dropdrage.simpleweather.domain.weather.HourWeather
import com.dropdrage.simpleweather.domain.weather.Weather
import com.dropdrage.simpleweather.domain.weather.WeatherType
import com.dropdrage.simpleweather.domain.weather.current.CurrentWeather

private typealias DomainWeather = Weather

private const val HOURS_IN_DAY = 24

fun WeatherResponseDto.toDomainWeather(): DomainWeather {
    val weathersPerHour = hourly.toWeatherPerHour().chunked(HOURS_IN_DAY)
    val dailyWeather = daily.toDayWeather(weathersPerHour)
    return Weather(dailyWeather)
}

private fun HourlyWeatherDto.toWeatherPerHour(): List<HourWeather> = time.mapIndexed { index, time ->
    val temperature = convertTemperatureIfApiDontSupport(temperatures[index])
    val weatherType = codeToWeatherType(weatherCodes[index])
    val windSpeed = convertWindSpeedIfApiDontSupport(windSpeeds[index])
    val pressure = convertPressureIfApiDontSupport(pressures[index])
    val humidity = humidities[index]
    val visibility = convertVisibilityIfApiDontSupport(visibilities[index])

    HourWeather(
        dateTime = time,
        weatherType = weatherType,
        temperature = temperature,
        pressure = pressure,
        windSpeed = windSpeed,
        humidity = humidity,
        visibility = visibility,
    )
}

private fun DailyWeatherDto.toDayWeather(weathersPerHour: List<List<HourWeather>>): List<DayWeather> =
    dates.mapIndexed { index, date ->
        val weatherType = codeToWeatherType(weatherCodes[index])
        val temperatureRange = Range(
            convertTemperatureIfApiDontSupport(minTemperatures[index]),
            convertTemperatureIfApiDontSupport(maxTemperatures[index])
        )
        val apparentTemperatureRange = Range(
            convertTemperatureIfApiDontSupport(apparentMinTemperatures[index]),
            convertTemperatureIfApiDontSupport(apparentMaxTemperatures[index]),
        )
        val precipitationSum = convertPrecipitationIfApiDontSupport(precipitationSums[index])
        val maxWindSpeed = maxWindSpeeds[index]
        val sunrise = sunrises[index]
        val sunset = sunsets[index]

        DayWeather(
            date = date,
            weatherType = weatherType,
            temperatureRange = temperatureRange,
            apparentTemperatureRange = apparentTemperatureRange,
            precipitationSum = precipitationSum,
            maxWindSpeed = maxWindSpeed,
            sunrise = sunrise,
            sunset = sunset,
            weatherPerHour = weathersPerHour[index],
        )
    }


fun CurrentWeatherResponseDto.toDomainCurrentWeather(): CurrentWeather = CurrentWeather(
    convertTemperatureIfApiDontSupport(currentWeather.temperature),
    codeToWeatherType(currentWeather.weatherCode)
)


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
    56, 66 -> WeatherType.LightFreezingDrizzle
    57 -> WeatherType.DenseFreezingDrizzle
    61 -> WeatherType.SlightRain
    63 -> WeatherType.ModerateRain
    65 -> WeatherType.HeavyRain
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
