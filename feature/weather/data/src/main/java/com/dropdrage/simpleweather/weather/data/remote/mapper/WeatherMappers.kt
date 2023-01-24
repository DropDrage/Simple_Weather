package com.dropdrage.simpleweather.data.weather.remote

import com.dropdrage.common.domain.Range
import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeather
import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherResponseDto
import com.dropdrage.simpleweather.weather.data.local.cache.dto.CurrentWeatherDto
import com.dropdrage.simpleweather.weather.data.local.cache.util.converter.WeatherTypeConverter
import com.dropdrage.simpleweather.weather.data.local.cache.util.converter.WeatherUnitsConverter
import com.dropdrage.simpleweather.weather.data.local.cache.util.converter.WeatherUnitsConverter.convertPrecipitationIfApiDontSupport
import com.dropdrage.simpleweather.weather.data.local.cache.util.converter.WeatherUnitsConverter.convertPressureIfApiDontSupport
import com.dropdrage.simpleweather.weather.data.local.cache.util.converter.WeatherUnitsConverter.convertTemperatureIfApiDontSupport
import com.dropdrage.simpleweather.weather.data.local.cache.util.converter.WeatherUnitsConverter.convertVisibilityIfApiDontSupport
import com.dropdrage.simpleweather.weather.data.local.cache.util.converter.WeatherUnitsConverter.convertWindSpeedIfApiDontSupport
import com.dropdrage.simpleweather.weather.domain.weather.DayWeather
import com.dropdrage.simpleweather.weather.domain.weather.HourWeather
import com.dropdrage.simpleweather.weather.domain.weather.Weather

private typealias DomainWeather = Weather
private typealias DomainCurrentWeather = CurrentWeather

private const val HOURS_IN_DAY = 24

internal fun WeatherResponseDto.toDomainWeather(): DomainWeather {
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

internal fun CurrentWeatherDto.toDomain(): DomainCurrentWeather = DomainCurrentWeather(
    WeatherUnitsConverter.convertTemperature(temperature),
    weatherType
)

internal fun CurrentWeatherResponseDto.toDomainCurrentWeather(): CurrentWeather = CurrentWeather(
    convertTemperatureIfApiDontSupport(currentWeather.temperature),
    WeatherTypeConverter.toWeatherType(currentWeather.weatherCode)
)
