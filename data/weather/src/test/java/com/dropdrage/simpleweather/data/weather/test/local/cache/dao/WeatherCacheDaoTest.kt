package com.dropdrage.simpleweather.data.weather.test.local.cache.dao

import com.dropdrage.common.test.util.coVerifyOnce
import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.simpleweather.data.weather.local.cache.dao.WeatherCacheDao
import com.dropdrage.simpleweather.data.weather.local.cache.model.DayWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.model.HourWeatherModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
internal class WeatherCacheDaoTest {

    @MockK
    lateinit var dao: WeatherCacheDao


    @Nested
    inner class updateWeather {

        @Test
        fun `empty days and hours`() = runTest {
            val locationId = 1L
            coJustRun { dao["clearForLocation"](any<Long>()) }
            val insertedIds = listOf(1L, 2L, 4L, 6L)
            val insertedDayWeathers = slot<List<DayWeatherModel>>()
            coEvery { dao["insertAllDayWeathersAndGetIds"](capture(insertedDayWeathers)) } returns insertedIds
            val insertedHourWeathers = slot<List<HourWeatherModel>>()
            coJustRun { dao["insertAllHourWeathers"](capture(insertedHourWeathers)) }
            coJustRun { dao["updateLocationUpdateTime"](eq(locationId), any<LocalDateTime>()) }
            val dayWeathers = emptyList<DayWeatherModel>()
            coEvery { dao["clearAndInsertWeather"](eq(locationId), eq(dayWeathers)) } answers { callOriginal() }
            coEvery { dao.updateWeather(eq(locationId), eq(dayWeathers), any()) } answers { callOriginal() }

            dao.updateWeather(locationId, dayWeathers, ::convertIdsToHourWeatherModelEmpty)

            coVerifyOnce {
                dao["clearForLocation"](any<Long>())
                dao["insertAllDayWeathersAndGetIds"](any<List<DayWeatherModel>>())
                dao["insertAllHourWeathers"](any<List<HourWeatherModel>>())
                dao["updateLocationUpdateTime"](eq(locationId), any<LocalDateTime>())
            }
            assertThat(insertedDayWeathers.captured).isEmpty()
            assertThat(insertedHourWeathers.captured).isEmpty()
        }

        @Test
        fun `empty days and filled hours`() = runTest {
            val locationId = 1L
            coJustRun { dao["clearForLocation"](any<Long>()) }
            val insertedDayWeathers = slot<List<DayWeatherModel>>()
            coEvery { dao["insertAllDayWeathersAndGetIds"](capture(insertedDayWeathers)) } returns emptyList<Long>()
            val insertedHourWeathers = slot<List<HourWeatherModel>>()
            coJustRun { dao["insertAllHourWeathers"](capture(insertedHourWeathers)) }
            coJustRun { dao["updateLocationUpdateTime"](eq(locationId), any<LocalDateTime>()) }
            val dayWeathers = emptyList<DayWeatherModel>()
            coEvery { dao["clearAndInsertWeather"](eq(locationId), eq(dayWeathers)) } answers { callOriginal() }
            coEvery { dao.updateWeather(eq(locationId), eq(dayWeathers), any()) } answers { callOriginal() }

            dao.updateWeather(locationId, dayWeathers, ::convertIdsToHourWeatherModel)

            coVerifyOnce {
                dao["clearForLocation"](any<Long>())
                dao["insertAllDayWeathersAndGetIds"](any<List<DayWeatherModel>>())
                dao["insertAllHourWeathers"](any<List<HourWeatherModel>>())
                dao["updateLocationUpdateTime"](eq(locationId), any<LocalDateTime>())
            }
            assertThat(insertedDayWeathers.captured).isEmpty()
            assertThat(insertedHourWeathers.captured).isEmpty()
        }

        @Test
        fun `filled days and empty hours`() = runTest {
            val locationId = 1L
            coJustRun { dao["clearForLocation"](any<Long>()) }
            val insertedIds = listOf(1L, 2L, 4L, 6L)
            val insertedDayWeathers = slot<List<DayWeatherModel>>()
            coEvery { dao["insertAllDayWeathersAndGetIds"](capture(insertedDayWeathers)) } returns insertedIds
            val insertedHourWeathers = slot<List<HourWeatherModel>>()
            coJustRun { dao["insertAllHourWeathers"](capture(insertedHourWeathers)) }
            coJustRun { dao["updateLocationUpdateTime"](eq(locationId), any<LocalDateTime>()) }
            val dayWeathers = listOf(
                mockDayWeatherModel(locationId),
                mockDayWeatherModel(locationId),
                mockDayWeatherModel(locationId),
                mockDayWeatherModel(locationId),
            )
            coEvery { dao["clearAndInsertWeather"](eq(locationId), eq(dayWeathers)) } answers { callOriginal() }
            coEvery { dao.updateWeather(eq(locationId), eq(dayWeathers), any()) } answers { callOriginal() }

            dao.updateWeather(locationId, dayWeathers, ::convertIdsToHourWeatherModelEmpty)

            coVerifyOnce {
                dao["clearForLocation"](any<Long>())
                dao["insertAllDayWeathersAndGetIds"](any<List<DayWeatherModel>>())
                dao["insertAllHourWeathers"](any<List<HourWeatherModel>>())
                dao["updateLocationUpdateTime"](eq(locationId), any<LocalDateTime>())
            }
            assertThat(insertedDayWeathers.captured).containsExactlyElementsIn(dayWeathers)
            assertThat(insertedHourWeathers.captured).isEmpty()
        }

        @Test
        fun `filled days and hours`() = runTest {
            val locationId = 1L
            coJustRun { dao["clearForLocation"](any<Long>()) }
            val insertedIds = listOf(1L, 2L, 4L, 6L)
            val insertedDayWeathers = slot<List<DayWeatherModel>>()
            coEvery { dao["insertAllDayWeathersAndGetIds"](capture(insertedDayWeathers)) } returns insertedIds
            val insertedHourWeathers = slot<List<HourWeatherModel>>()
            coJustRun { dao["insertAllHourWeathers"](capture(insertedHourWeathers)) }
            coJustRun { dao["updateLocationUpdateTime"](eq(locationId), any<LocalDateTime>()) }
            val dayWeathers = listOf(
                mockDayWeatherModel(locationId),
                mockDayWeatherModel(locationId),
                mockDayWeatherModel(locationId),
                mockDayWeatherModel(locationId),
            )
            coEvery { dao["clearAndInsertWeather"](eq(locationId), eq(dayWeathers)) } answers { callOriginal() }
            coEvery { dao.updateWeather(eq(locationId), eq(dayWeathers), any()) } answers { callOriginal() }

            dao.updateWeather(locationId, dayWeathers, ::convertIdsToHourWeatherModel)

            coVerifyOnce {
                dao["clearForLocation"](any<Long>())
                dao["insertAllDayWeathersAndGetIds"](any<List<DayWeatherModel>>())
                dao["insertAllHourWeathers"](any<List<HourWeatherModel>>())
                dao["updateLocationUpdateTime"](eq(locationId), any<LocalDateTime>())
            }
            assertThat(insertedDayWeathers.captured).containsExactlyElementsIn(dayWeathers)
            assertThat(insertedHourWeathers.captured.map { it.dayId }).containsExactlyElementsIn(insertedIds)
        }

    }


    private fun mockDayWeatherModel(
        locationId: Long,
        weatherType: WeatherType = WeatherType.ClearSky,
        date: LocalDate = LocalDate.now(),
    ) = mockk<DayWeatherModel> {
        every { this@mockk.locationId } returns locationId
        every { this@mockk.weatherType } returns weatherType
        every { this@mockk.date } returns date
    }

    private fun convertIdsToHourWeatherModel(ids: List<Long>): List<HourWeatherModel> {
        val now = LocalDateTime.now()
        return ids.map {
            HourWeatherModel(
                dateTime = now,
                dayId = it,
                weatherType = WeatherType.ClearSky,
                temperature = 1f,
                pressure = 100,
                windSpeed = 1f,
                humidity = 1,
                visibility = 1000f,
            )
        }
    }

    private fun convertIdsToHourWeatherModelEmpty(
        @Suppress("UNUSED_PARAMETER") ids: List<Long>,
    ): List<HourWeatherModel> = emptyList()

}