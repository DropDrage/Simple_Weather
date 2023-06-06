package com.dropdrage.simpleweather.data.weather.test.repository

import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeather
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.simpleweather.data.settings.TemperatureUnit
import com.dropdrage.simpleweather.data.settings.WeatherUnitsPreferences
import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherResponseDto
import com.dropdrage.simpleweather.data.weather.local.cache.dao.HourWeatherDao
import com.dropdrage.simpleweather.data.weather.local.cache.dao.LocationDao
import com.dropdrage.simpleweather.data.weather.local.cache.model.LocationModel
import com.dropdrage.simpleweather.data.weather.remote.WeatherApi
import com.dropdrage.simpleweather.data.weather.repository.CurrentWeatherRepositoryImpl
import com.dropdrage.simpleweather.data.weather.utils.WeatherTypeConverter
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConverter
import com.dropdrage.test.util.coVerifyNever
import com.dropdrage.test.util.coVerifyOnce
import com.dropdrage.test.util.createList
import com.dropdrage.test.util.runTestWithMockLogE
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.UnknownHostException
import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherDto as RemoteCurrentWeatherDto
import com.dropdrage.simpleweather.data.weather.local.cache.dto.CurrentWeatherDto as LocalCurrentWeatherDto

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class CurrentWeatherRepositoryImplTest {

    @MockK
    lateinit var api: WeatherApi

    @MockK
    lateinit var locationDao: LocationDao

    @MockK
    lateinit var hourWeatherDao: HourWeatherDao

    private lateinit var repository: CurrentWeatherRepositoryImpl


    @BeforeEach
    fun setUp() {
        repository = CurrentWeatherRepositoryImpl(api, locationDao, hourWeatherDao)
    }


    @Nested
    inner class getCurrentWeather {

        @Test
        fun `locations empty then empty lists`() = runTest {
            val location = emptyList<Location>()

            val result = repository.getCurrentWeather(location).toList()

            coVerifyNever {
                locationDao.getLocationApproximately(any(), any())
                hourWeatherDao.getCurrentWeather(any(), any())
                api.getCurrentWeather(any(), any(), any())
            }
            assertThat(result).hasSize(2)
            assertThat(result[0]).isEmpty()
            assertThat(result[1]).isEmpty()
        }

        @Test
        fun `locations filled but not in db and api then two list of nulls`() = runTest {
            mockTemperatureConversion {
                coEvery { locationDao.getLocationApproximately(any(), any()) } returns null
                val temperature = 1f
                val weatherCode = 1
                coEvery { api.getCurrentWeather(any(), any(), any()) } returns CurrentWeatherResponseDto(
                    RemoteCurrentWeatherDto(temperature, weatherCode)
                )
                val location = listOf(
                    Location(1f, 2f),
                    Location(3f, 2f),
                    Location(4f, 2f),
                )
                val weatherType = WeatherTypeConverter.toWeatherType(weatherCode)

                val result = repository.getCurrentWeather(location).toList()

                coVerify(exactly = location.size) {
                    locationDao.getLocationApproximately(any(), any())
                    api.getCurrentWeather(any(), any(), any())
                }
                coVerifyNever { hourWeatherDao.getCurrentWeather(any(), any()) }
                assertThat(result).hasSize(2)
                assertThat(result[0]).containsExactlyElementsIn(createList(location.size) { null })
                assertThat(result[1]).containsExactlyElementsIn(createList(location.size) {
                    CurrentWeather(temperature, weatherType)
                })
            }
        }

        @Test
        fun `locations filled but weather empty in db then first nulls and second not nulls`() = runTest {
            mockTemperatureConversion {
                var id = 0L
                coEvery { locationDao.getLocationApproximately(any(), any()) } answers {
                    LocationModel(id++, firstArg(), secondArg())
                }
                coEvery { hourWeatherDao.getCurrentWeather(any(), any()) } returns null
                val temperature = 1f
                val weatherCode = 1
                coEvery { api.getCurrentWeather(any(), any(), any()) } returns CurrentWeatherResponseDto(
                    RemoteCurrentWeatherDto(temperature, weatherCode)
                )
                val location = listOf(
                    Location(1f, 2f),
                    Location(3f, 2f),
                    Location(4f, 2f),
                )
                val weatherType = WeatherTypeConverter.toWeatherType(weatherCode)

                val result = repository.getCurrentWeather(location).toList()

                coVerify(exactly = location.size) {
                    locationDao.getLocationApproximately(any(), any())
                    hourWeatherDao.getCurrentWeather(any(), any())
                    api.getCurrentWeather(any(), any(), any())
                }
                assertThat(result).hasSize(2)
                assertThat(result[0]).containsExactlyElementsIn(createList(location.size) { null })
                assertThat(result[1]).containsExactlyElementsIn(createList(location.size) {
                    CurrentWeather(temperature, weatherType)
                })
            }
        }

        @Test
        fun `locations filled and weather in db and api then two list of nulls`() = runTest {
            mockTemperatureConversion {
                var id = 0L
                coEvery { locationDao.getLocationApproximately(any(), any()) } answers {
                    LocationModel(id++, firstArg(), secondArg())
                }
                val localTemperature = 10f
                val localWeatherType = WeatherType.Foggy
                coEvery { hourWeatherDao.getCurrentWeather(any(), any()) } returns LocalCurrentWeatherDto(
                    localTemperature, localWeatherType
                )
                val remoteTemperature = 1f
                val remoteWeatherCode = 1
                val remoteWeatherType = WeatherTypeConverter.toWeatherType(remoteWeatherCode)
                coEvery { api.getCurrentWeather(any(), any(), any()) } returns CurrentWeatherResponseDto(
                    RemoteCurrentWeatherDto(remoteTemperature, remoteWeatherCode)
                )
                val location = listOf(
                    Location(1f, 2f),
                    Location(3f, 2f),
                    Location(4f, 2f),
                )

                val result = repository.getCurrentWeather(location).toList()

                coVerify(exactly = location.size) {
                    locationDao.getLocationApproximately(any(), any())
                    hourWeatherDao.getCurrentWeather(any(), any())
                    api.getCurrentWeather(any(), any(), any())
                }
                assertThat(result).hasSize(2)
                assertThat(result[0]).containsExactlyElementsIn(createList(location.size) {
                    CurrentWeather(localTemperature, localWeatherType)
                })
                assertThat(result[1]).containsExactlyElementsIn(createList(location.size) {
                    CurrentWeather(remoteTemperature, remoteWeatherType)
                })
            }
        }

        @Test
        fun `locations filled and weather in db but api throws UnknownHost then only first list`() =
            runTestWithMockLogE {
                mockTemperatureConversion {
                    var id = 0L
                    coEvery { locationDao.getLocationApproximately(any(), any()) } answers {
                        LocationModel(id++, firstArg(), secondArg())
                    }
                    val localTemperature = 10f
                    val localWeatherType = WeatherType.Foggy
                    coEvery { hourWeatherDao.getCurrentWeather(any(), any()) } returns LocalCurrentWeatherDto(
                        localTemperature, localWeatherType
                    )
                    coEvery { api.getCurrentWeather(any(), any(), any()) } throws UnknownHostException()
                    val location = listOf(
                        Location(1f, 2f),
                        Location(3f, 2f),
                        Location(4f, 2f),
                    )

                    val result = repository.getCurrentWeather(location).toList()

                    coVerify(exactly = location.size) {
                        locationDao.getLocationApproximately(any(), any())
                        hourWeatherDao.getCurrentWeather(any(), any())
                    }
                    coVerifyOnce { api.getCurrentWeather(any(), any(), any()) }
                    assertThat(result).hasSize(1)
                    assertThat(result[0]).containsExactlyElementsIn(createList(location.size) {
                        CurrentWeather(localTemperature, localWeatherType)
                    })
                }
            }

        @Test
        fun `locations filled and weather in db but api throws Exception then only first list`() = runTestWithMockLogE {
            mockTemperatureConversion {
                var id = 0L
                coEvery { locationDao.getLocationApproximately(any(), any()) } answers {
                    LocationModel(id++, firstArg(), secondArg())
                }
                val localTemperature = 10f
                val localWeatherType = WeatherType.Foggy
                coEvery { hourWeatherDao.getCurrentWeather(any(), any()) } returns LocalCurrentWeatherDto(
                    localTemperature, localWeatherType
                )
                coEvery { api.getCurrentWeather(any(), any(), any()) } throws Exception()
                val location = listOf(
                    Location(1f, 2f),
                    Location(3f, 2f),
                    Location(4f, 2f),
                )

                val result = repository.getCurrentWeather(location).toList()

                coVerify(exactly = location.size) {
                    locationDao.getLocationApproximately(any(), any())
                    hourWeatherDao.getCurrentWeather(any(), any())
                }
                coVerifyOnce { api.getCurrentWeather(any(), any(), any()) }
                assertThat(result).hasSize(1)
                assertThat(result[0]).containsExactlyElementsIn(createList(location.size) {
                    CurrentWeather(localTemperature, localWeatherType)
                })
            }
        }

    }


    private inline fun mockTemperatureConversion(block: () -> Unit) =
        mockkObject(WeatherUnitsConverter, WeatherUnitsPreferences) {
            every { WeatherUnitsConverter.convertTemperatureIfApiDontSupport(any()) } returnsArgument 0
            every { WeatherUnitsPreferences.temperatureUnit } returns TemperatureUnit.CELSIUS

            block()
        }

}