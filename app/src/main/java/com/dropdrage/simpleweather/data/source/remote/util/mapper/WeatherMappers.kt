package com.dropdrage.simpleweather.data.util.mapper

import com.dropdrage.simpleweather.core.domain.Range
import com.dropdrage.simpleweather.data.source.local.util.converter.WeatherTypeConverter
import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherResponseDto
import com.dropdrage.simpleweather.data.source.remote.dto.DailyWeatherDto
import com.dropdrage.simpleweather.data.source.remote.dto.HourlyWeatherDto
import com.dropdrage.simpleweather.data.source.remote.dto.WeatherResponseDto
import com.dropdrage.simpleweather.data.util.WeatherUnitsConverter.convertPrecipitationIfApiDontSupport
import com.dropdrage.simpleweather.data.util.WeatherUnitsConverter.convertPressureIfApiDontSupport
import com.dropdrage.simpleweather.data.util.WeatherUnitsConverter.convertTemperatureIfApiDontSupport
import com.dropdrage.simpleweather.data.util.WeatherUnitsConverter.convertVisibilityIfApiDontSupport
import com.dropdrage.simpleweather.data.util.WeatherUnitsConverter.convertWindSpeedIfApiDontSupport
import com.dropdrage.simpleweather.domain.weather.DayWeather
import com.dropdrage.simpleweather.domain.weather.HourWeather
import com.dropdrage.simpleweather.domain.weather.Weather
import com.dropdrage.simpleweather.domain.weather.current.CurrentWeather

private typealias DomainWeather = Weather

private const val HOURS_IN_DAY = 24

fun WeatherResponseDto.toDomainWeather(): DomainWeather {
    val weathersPerHour = hourly.toWeatherPerHour().chunked(HOURS_IN_DAY)
    val dailyWeather = daily.toDayWeather(weathersPerHour)
    return DomainWeather(dailyWeather)
}

private fun HourlyWeatherDto.toWeatherPerHour(): List<HourWeather> = time.mapIndexed { index, time ->
    val weatherType = WeatherTypeConverter.toWeatherType(weatherCodes[index])
    val temperature = convertTemperatureIfApiDontSupport(temperatures[index])
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
        val weatherType = WeatherTypeConverter.toWeatherType(weatherCodes[index])
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
    WeatherTypeConverter.toWeatherType(currentWeather.weatherCode)
)
