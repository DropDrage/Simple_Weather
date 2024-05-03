package com.dropdrage.simpleweather.data.weather.local.cache.util.mapper

import com.dropdrage.simpleweather.data.weather.local.cache.dto.CurrentWeatherDto
import com.dropdrage.simpleweather.data.weather.local.cache.model.DayWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.model.HourWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.model.TemperatureRange
import com.dropdrage.simpleweather.data.weather.local.cache.relation.DayToHourWeather
import com.dropdrage.simpleweather.data.weather.local.cache.util.converter.WeatherUnitsDeconverter
import com.dropdrage.simpleweather.data.weather.remote.DailyWeatherDto
import com.dropdrage.simpleweather.data.weather.remote.HourlyWeatherDto
import com.dropdrage.simpleweather.data.weather.utils.WeatherTypeConverter
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConverter
import com.dropdrage.simpleweather.feature.city.list.domain.weather.CurrentWeather as DomainCurrentWeather
import com.dropdrage.simpleweather.feature.weather.domain.weather.DayWeather as DomainDayWeather
import com.dropdrage.simpleweather.feature.weather.domain.weather.HourWeather as DomainHourWeather
import com.dropdrage.simpleweather.feature.weather.domain.weather.Weather as DomainWeather

private const val HOURS_IN_DAY = 24


internal fun List<DayToHourWeather>.toDomainWeather(): DomainWeather = DomainWeather(
    this.map { it.day.toDomain(it.hours.map(HourWeatherModel::toDomain)) }
)

private fun HourWeatherModel.toDomain(): DomainHourWeather {
    val dateTime = dateTime
    val weatherType = weatherType
    val temperature = WeatherUnitsConverter.convertTemperature(temperature)
    val windSpeed = WeatherUnitsConverter.convertWindSpeed(windSpeed)
    val pressure = WeatherUnitsConverter.convertPressure(pressure)
    val humidity = humidity
    val visibility = WeatherUnitsConverter.convertVisibility(visibility)

    return DomainHourWeather(
        dateTime = dateTime,
        weatherType = weatherType,
        temperature = temperature,
        pressure = pressure,
        windSpeed = windSpeed,
        humidity = humidity,
        visibility = visibility,
    )
}

private fun DayWeatherModel.toDomain(weathersPerHour: List<DomainHourWeather>): DomainDayWeather {
    val weatherType = weatherType
    val temperatureRange = temperatureRange.toRange().map(WeatherUnitsConverter::convertTemperature)
    val apparentTemperatureRange = apparentTemperatureRange.toRange().map(WeatherUnitsConverter::convertTemperature)
    val precipitationSum = WeatherUnitsConverter.convertPrecipitation(precipitationSum)
    val maxWindSpeed = WeatherUnitsConverter.convertWindSpeed(maxWindSpeed)
    val sunrise = sunrise
    val sunset = sunset

    return DomainDayWeather(
        date = date,
        weatherType = weatherType,
        temperatureRange = temperatureRange,
        apparentTemperatureRange = apparentTemperatureRange,
        precipitationSum = precipitationSum,
        maxWindSpeed = maxWindSpeed,
        sunrise = sunrise,
        sunset = sunset,
        weatherPerHour = weathersPerHour,
    )
}


internal fun DailyWeatherDto.toDayModels(locationId: Long): List<DayWeatherModel> = dates.mapIndexed { index, date ->
    val date = date
    val weatherType = WeatherTypeConverter.toWeatherType(weatherCodes[index])
    val temperatureRange = TemperatureRange(
        WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(minTemperatures[index]),
        WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(maxTemperatures[index])
    )
    val apparentTemperatureRange = TemperatureRange(
        WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(apparentMinTemperatures[index]),
        WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(apparentMaxTemperatures[index]),
    )
    val precipitationSum = WeatherUnitsDeconverter.deconvertPrecipitationIfApiDontSupport(precipitationSums[index])
    val maxWindSpeed = WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(maxWindSpeeds[index])
    val sunrise = sunrises[index]
    val sunset = sunsets[index]

    DayWeatherModel(
        date = date,
        locationId = locationId,
        weatherType = weatherType,
        temperatureRange = temperatureRange,
        apparentTemperatureRange = apparentTemperatureRange,
        precipitationSum = precipitationSum,
        maxWindSpeed = maxWindSpeed,
        sunrise = sunrise,
        sunset = sunset,
    )
}

internal fun HourlyWeatherDto.toHourModels(dayIds: List<Long>): List<HourWeatherModel> = time.chunked(HOURS_IN_DAY)
    .flatMapIndexed { dayIndex, times ->
        val dayId = dayIds[dayIndex]
        times.mapIndexed { timeIndex, time ->
            val index = dayIndex * HOURS_IN_DAY + timeIndex
            val dateTime = time
            val weatherType = WeatherTypeConverter.toWeatherType(weatherCodes[index])
            val temperature = WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(temperatures[index])
            val pressure = WeatherUnitsDeconverter.deconvertPressureIfApiDontSupport(pressures[index].toInt())
            val windSpeed = WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(windSpeeds[index])
            val humidity = humidities[index]
            val visibility = WeatherUnitsDeconverter.deconvertVisibilityIfApiDontSupport(visibilities[index].toFloat())

            HourWeatherModel(
                dateTime = dateTime,
                dayId = dayId,
                weatherType = weatherType,
                temperature = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                visibility = visibility,
            )
        }
    }


internal fun CurrentWeatherDto.toDomain(): DomainCurrentWeather = DomainCurrentWeather(
    WeatherUnitsConverter.convertTemperature(temperature),
    weatherType
)
