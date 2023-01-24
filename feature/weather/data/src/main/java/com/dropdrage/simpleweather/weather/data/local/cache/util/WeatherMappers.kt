package com.dropdrage.simpleweather.data.weather.local.util.mapper

import com.dropdrage.simpleweather.weather.data.local.cache.model.DayWeatherModel
import com.dropdrage.simpleweather.weather.data.local.cache.model.HourWeatherModel
import com.dropdrage.simpleweather.weather.data.local.cache.model.TemperatureRange
import com.dropdrage.simpleweather.weather.data.local.cache.relation.DayToHourWeather
import com.dropdrage.simpleweather.weather.data.local.cache.util.converter.WeatherUnitsConverter
import com.dropdrage.simpleweather.weather.data.local.cache.util.converter.WeatherUnitsDeconverter
import com.dropdrage.simpleweather.weather.domain.weather.DayWeather
import com.dropdrage.simpleweather.weather.domain.weather.HourWeather
import com.dropdrage.simpleweather.weather.domain.weather.Weather

private typealias DomainWeather = Weather
private typealias DomainDayWeather = DayWeather
private typealias DomainHourWeather = HourWeather

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
    val maxWindSpeed = maxWindSpeed
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


internal fun DomainWeather.toDayModels(locationId: Long): List<DayWeatherModel> = dailyWeather.map {
    val date = it.date
    val weatherType = it.weatherType
    val temperatureRange = TemperatureRange.fromRange(
        it.temperatureRange.map(WeatherUnitsDeconverter::deconvertTemperatureIfApiSupport)
    )
    val apparentTemperatureRange = TemperatureRange.fromRange(
        it.apparentTemperatureRange.map(WeatherUnitsDeconverter::deconvertTemperatureIfApiSupport)
    )
    val precipitationSum = WeatherUnitsDeconverter.deconvertPrecipitationIfApiSupport(it.precipitationSum)
    val maxWindSpeed = WeatherUnitsDeconverter.deconvertWindSpeedIfApiSupport(it.maxWindSpeed)
    val sunrise = it.sunrise
    val sunset = it.sunset

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

internal fun DomainWeather.toHourModels(dayIds: List<Long>): List<HourWeatherModel> =
    dailyWeather.flatMapIndexed { index, day ->
        val dayId = dayIds[index]
        day.weatherPerHour.map { it.toModel(dayId) }
    }

private fun DomainHourWeather.toModel(dayId: Long): HourWeatherModel {
    val dateTime = dateTime
    val weatherType = weatherType
    val temperature = WeatherUnitsDeconverter.deconvertTemperatureIfApiSupport(temperature)
    val pressure = WeatherUnitsDeconverter.deconvertPressureIfApiSupport(pressure)
    val windSpeed = WeatherUnitsDeconverter.deconvertWindSpeedIfApiSupport(windSpeed)
    val humidity = humidity
    val visibility = WeatherUnitsDeconverter.deconvertVisibilityIfApiSupport(visibility)

    return HourWeatherModel(
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
