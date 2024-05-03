package com.dropdrage.simpleweather.data.weather.test.remote.utils.mapper

import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherDto
import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherResponseDto
import com.dropdrage.simpleweather.data.weather.remote.toDomainCurrentWeather
import com.dropdrage.simpleweather.data.weather.utils.WeatherTypeConverter
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConverter
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class WeatherMapperTest {

    @Test
    fun `CurrentWeatherResponseDto toDomainCurrentWeather then success`() = mockkObject(WeatherUnitsConverter) {
        every { WeatherUnitsConverter.convertTemperatureIfApiDontSupport(any()) } returnsArgument 0
        val dto = CurrentWeatherResponseDto(CurrentWeatherDto(1f, 2))

        val result = dto.toDomainCurrentWeather()

        assertEquals(dto.currentWeather.temperature, result.temperature)
        assertEquals(WeatherTypeConverter.toWeatherType(dto.currentWeather.weatherCode), result.weatherType)
    }

}
