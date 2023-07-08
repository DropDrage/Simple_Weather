package com.dropdrage.simpleweather.data.weather.test.local.cache.util

import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.simpleweather.data.weather.local.cache.dto.CurrentWeatherDto
import com.dropdrage.simpleweather.data.weather.local.cache.model.DayWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.model.HourWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.model.TemperatureRange
import com.dropdrage.simpleweather.data.weather.local.cache.relation.DayToHourWeather
import com.dropdrage.simpleweather.data.weather.local.cache.util.converter.WeatherUnitsDeconverter
import com.dropdrage.simpleweather.data.weather.local.util.mapper.toDayModels
import com.dropdrage.simpleweather.data.weather.local.util.mapper.toDomain
import com.dropdrage.simpleweather.data.weather.local.util.mapper.toDomainWeather
import com.dropdrage.simpleweather.data.weather.local.util.mapper.toHourModels
import com.dropdrage.simpleweather.data.weather.remote.DailyWeatherDto
import com.dropdrage.simpleweather.data.weather.remote.HourlyWeatherDto
import com.dropdrage.simpleweather.data.weather.util.createDailyWeatherDto
import com.dropdrage.simpleweather.data.weather.util.createDayToHourWeather
import com.dropdrage.simpleweather.data.weather.util.createHourlyWeatherDto
import com.dropdrage.simpleweather.data.weather.util.simplyToDayWeather
import com.dropdrage.simpleweather.data.weather.utils.WeatherTypeConverter
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConverter
import com.dropdrage.test.util.createList
import com.dropdrage.test.util.createListIndexed
import com.dropdrage.test.util.mockLogW
import com.dropdrage.test.util.verifyNever
import com.dropdrage.test.util.verifyOnce
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKVerificationScope
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private const val HOURS_IN_DAY = 24

internal class WeatherMappersTest {

    @Nested
    inner class `list DayToHourWeather toDomain` {

        @Test
        fun `empty list then throws NoSuchElementException`() = mockWeatherUnitsConverter {
            val dayToHourWeathers = emptyList<DayToHourWeather>()

            assertThrows<NoSuchElementException> { dayToHourWeathers.toDomainWeather() }
        }

        @Test
        fun `not empty list with empty hours then success`() = mockWeatherUnitsConverter {
            val dayToHourWeathers = createList(3) { createDayToHourWeather(0) }

            val result = dayToHourWeathers.toDomainWeather()

            val dayWeathers = dayToHourWeathers.map(DayToHourWeather::simplyToDayWeather)
            assertThat(result.dailyWeather).containsExactlyElementsIn(dayWeathers)
            verify(exactly = dayToHourWeathers.size * 4) { WeatherUnitsConverter.convertTemperature(any()) }
            verify(exactly = dayToHourWeathers.size) {
                WeatherUnitsConverter.convertWindSpeed(any())
                WeatherUnitsConverter.convertPrecipitation(any())
            }
            verifyNever {
                WeatherUnitsConverter.convertPressure(any())
                WeatherUnitsConverter.convertVisibility(any())
            }
        }

        @Test
        fun `not empty list with not empty hours then success`() = mockWeatherUnitsConverter {
            val daysCount = 3
            val hoursCount = 5
            val dayToHourWeathers = createList(daysCount) { createDayToHourWeather(hoursCount) }

            val result = dayToHourWeathers.toDomainWeather()

            val dayWeathers = dayToHourWeathers.map(DayToHourWeather::simplyToDayWeather)
            assertThat(result.dailyWeather).containsExactlyElementsIn(dayWeathers)
            val allHoursCount = daysCount * hoursCount
            verifyLeastMost(daysCount * 4 + allHoursCount) { WeatherUnitsConverter.convertTemperature(any()) }
            verifyLeastMost(daysCount + allHoursCount) { WeatherUnitsConverter.convertWindSpeed(any()) }
            verifyLeastMost(daysCount) { WeatherUnitsConverter.convertPrecipitation(any()) }
            verifyLeastMost(allHoursCount) {
                WeatherUnitsConverter.convertPressure(any())
                WeatherUnitsConverter.convertVisibility(any())
            }
        }

    }

    @Nested
    inner class `DailyWeatherDto toDayModels` {

        @Test
        fun `empty then success`() = mockWeatherUnitsDeconverter {
            mockLogW {
                val dto = createDailyWeatherDto(0)
                val locationId = 1L

                val result = dto.toDayModels(locationId)

                assertThat(result).isEmpty()
                verifyNever {
                    WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(any())
                    WeatherUnitsDeconverter.deconvertPrecipitationIfApiDontSupport(any())
                    WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(any())
                }
            }
        }

        @Test
        fun `filled then success`() = mockWeatherUnitsDeconverter {
            mockLogW {
                val daysCount = 5
                val dto = createDailyWeatherDto(daysCount)
                val locationId = 1L

                val result = dto.toDayModels(locationId)

                assertThat(result).containsExactlyElementsIn(dailyWeatherDtoToDayModels(dto, locationId))
                verify(exactly = daysCount * 4) { WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(any()) }
                verify(exactly = daysCount) {
                    WeatherUnitsDeconverter.deconvertPrecipitationIfApiDontSupport(any())
                    WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(any())
                }
            }
        }

    }

    @Nested
    inner class `HourlyWeatherDto toHourModels` {

        @Test
        fun `hours empty, days filled then success`() = mockWeatherUnitsDeconverter {
            mockLogW {
                val daysCount = 5
                val dto = createHourlyWeatherDto(0)
                val dayIds = createListIndexed(daysCount) { it.toLong() }

                val result = dto.toHourModels(dayIds)

                assertThat(result).containsExactlyElementsIn(hourlyWeatherDtoToHourModels(dto, dayIds))
                verifyNever {
                    WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(any())
                    WeatherUnitsDeconverter.deconvertPressureIfApiDontSupport(any())
                    WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(any())
                    WeatherUnitsDeconverter.deconvertVisibilityIfApiDontSupport(any())
                }
            }
        }

        @Test
        fun `hours filled, days empty then success`() = mockWeatherUnitsDeconverter {
            mockLogW {
                val daysCount = 5
                val hoursCount = daysCount * HOURS_IN_DAY
                val dto = createHourlyWeatherDto(hoursCount)
                val dayIds = emptyList<Long>()

                assertThrows<IndexOutOfBoundsException> { dto.toHourModels(dayIds) }
            }
        }

        @Test
        fun `hours and days filled then success`() = mockWeatherUnitsDeconverter {
            mockLogW {
                val daysCount = 5
                val hoursCount = daysCount * HOURS_IN_DAY
                val dto = createHourlyWeatherDto(hoursCount)
                val dayIds = createListIndexed(daysCount) { it.toLong() }

                val result = dto.toHourModels(dayIds)

                assertThat(result).containsExactlyElementsIn(hourlyWeatherDtoToHourModels(dto, dayIds))
                verify(exactly = hoursCount) {
                    WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(any())
                    WeatherUnitsDeconverter.deconvertPressureIfApiDontSupport(any())
                    WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(any())
                    WeatherUnitsDeconverter.deconvertVisibilityIfApiDontSupport(any())
                }
            }
        }

    }

    @Test
    fun `CurrentWeatherDto toDomain then success`() = mockkObject(WeatherUnitsConverter) {
        every { WeatherUnitsConverter.convertTemperature(any()) } returnsArgument 0
        val dto = CurrentWeatherDto(1f, WeatherType.HeavyHailThunderstorm)

        val result = dto.toDomain()

        assertEquals(dto.temperature, result.temperature)
        assertEquals(dto.weatherType, result.weatherType)
        verifyOnce { WeatherUnitsConverter.convertTemperature(any()) }
    }


    //region Converter mocks

    private inline fun mockWeatherUnitsConverter(block: () -> Unit) = mockkObject(WeatherUnitsConverter) {
        every { WeatherUnitsConverter.convertTemperature(any()) } returnsArgument 0
        every { WeatherUnitsConverter.convertWindSpeed(any()) } returnsArgument 0
        every { WeatherUnitsConverter.convertPressure(any()) } returnsArgument 0
        every { WeatherUnitsConverter.convertVisibility(any()) } returnsArgument 0
        every { WeatherUnitsConverter.convertPrecipitation(any()) } returnsArgument 0

        block()
    }

    private inline fun mockWeatherUnitsDeconverter(block: () -> Unit) = mockkObject(WeatherUnitsDeconverter) {
        every { WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(any()) } returnsArgument 0
        every { WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(any()) } returnsArgument 0
        every { WeatherUnitsDeconverter.deconvertPressureIfApiDontSupport(any()) } returnsArgument 0
        every { WeatherUnitsDeconverter.deconvertVisibilityIfApiDontSupport(any()) } returnsArgument 0
        every { WeatherUnitsDeconverter.deconvertPrecipitationIfApiDontSupport(any()) } returnsArgument 0

        block()
    }

    //endregion

    //region Mappers

    private fun dailyWeatherDtoToDayModels(dto: DailyWeatherDto, locationId: Long) = dto.dates.mapIndexed { i, date ->
        DayWeatherModel(
            date = date,
            locationId = locationId,
            weatherType = WeatherTypeConverter.toWeatherType(dto.weatherCodes[i]),
            temperatureRange = TemperatureRange(dto.minTemperatures[i], dto.maxTemperatures[i]),
            apparentTemperatureRange = TemperatureRange(
                dto.apparentMinTemperatures[i],
                dto.apparentMaxTemperatures[i]
            ),
            precipitationSum = dto.precipitationSums[i],
            maxWindSpeed = dto.maxWindSpeeds[i],
            sunrise = dto.sunrises[i],
            sunset = dto.sunsets[i],
        )
    }

    private fun hourlyWeatherDtoToHourModels(dto: HourlyWeatherDto, dayIds: List<Long>): List<HourWeatherModel> =
        dto.time.chunked(HOURS_IN_DAY)
            .flatMapIndexed { dayIndex, times ->
                val dayId = dayIds[dayIndex]
                times.mapIndexed { timeIndex, time ->
                    val index = dayIndex * HOURS_IN_DAY + timeIndex

                    HourWeatherModel(
                        dateTime = time,
                        dayId = dayId,
                        weatherType = WeatherTypeConverter.toWeatherType(dto.weatherCodes[index]),
                        temperature = dto.temperatures[index],
                        pressure = dto.pressures[index].toInt(),
                        windSpeed = dto.windSpeeds[index],
                        humidity = dto.humidities[index],
                        visibility = dto.visibilities[index].toFloat(),
                    )
                }
            }

    //endregion

    /***
     * Lifehack bcz `exactly` param doesn't work.
     */
    private fun verifyLeastMost(count: Int, block: MockKVerificationScope.() -> Unit) =
        verify(atLeast = count, atMost = count, verifyBlock = block)

}