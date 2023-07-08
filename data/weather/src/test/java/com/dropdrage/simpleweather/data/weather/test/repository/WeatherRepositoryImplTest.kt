package com.dropdrage.simpleweather.data.weather.test.repository

import app.cash.turbine.test
import com.dropdrage.common.domain.Resource
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.weather.local.cache.dao.DayWeatherDao
import com.dropdrage.simpleweather.data.weather.local.cache.dao.LocationDao
import com.dropdrage.simpleweather.data.weather.local.cache.dao.WeatherCacheDao
import com.dropdrage.simpleweather.data.weather.local.cache.model.HourWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.model.LocationModel
import com.dropdrage.simpleweather.data.weather.local.cache.relation.DayToHourWeather
import com.dropdrage.simpleweather.data.weather.local.cache.util.mapper.toDomainWeather
import com.dropdrage.simpleweather.data.weather.remote.WeatherApi
import com.dropdrage.simpleweather.data.weather.remote.WeatherResponseDto
import com.dropdrage.simpleweather.data.weather.repository.WeatherRepositoryImpl
import com.dropdrage.simpleweather.data.weather.util.createDailyWeatherDto
import com.dropdrage.simpleweather.data.weather.util.createDayToHourWeather
import com.dropdrage.simpleweather.data.weather.util.createHourlyWeatherDto
import com.dropdrage.simpleweather.data.weather.util.simplyToDayWeather
import com.dropdrage.simpleweather.data.weather.util.simplyToDomainWeather
import com.dropdrage.simpleweather.weather.domain.weather.Weather
import com.dropdrage.test.util.assertInstanceOf
import com.dropdrage.test.util.coVerifyNever
import com.dropdrage.test.util.coVerifyOnce
import com.dropdrage.test.util.coVerifyTwice
import com.dropdrage.test.util.createList
import com.dropdrage.test.util.runTestWithMockLogEShort
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
internal class WeatherRepositoryImplTest {

    @MockK
    lateinit var api: WeatherApi

    @MockK
    lateinit var locationDao: LocationDao

    @MockK
    lateinit var dayWeatherDao: DayWeatherDao

    @MockK
    lateinit var weatherCacheDao: WeatherCacheDao

    private lateinit var repository: WeatherRepositoryImpl


    @BeforeEach
    fun setUp() {
        repository = WeatherRepositoryImpl(api, locationDao, dayWeatherDao, weatherCacheDao)
    }


    @Nested
    inner class getWeatherFromNow {

        @Test
        fun `location not in db db then CantObtainResource`() = runTest {
            val location = Location(1f, 2f)
            coEvery { locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude)) } returns null

            val result = repository.getWeatherFromNow(location)

            coVerifyNever { dayWeatherDao.getWeatherForLocationFromDay(any(), any()) }
            coVerifyOnce { locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude)) }
            assertInstanceOf<Resource.CantObtainResource<Weather>>(result)
        }

        @Test
        fun `location in db but weather empty in db then CantObtainResource`() = runTest {
            val location = Location(1f, 2f)
            val locationModel = LocationModel(1, location.latitude, location.longitude)
            coEvery {
                locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
            } returns locationModel
            coEvery { dayWeatherDao.getWeatherForLocationFromDay(eq(locationModel.id!!), any()) } returns emptyList()

            val result = repository.getWeatherFromNow(location)

            coVerifyOnce {
                locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                dayWeatherDao.getWeatherForLocationFromDay(any(), any())
            }
            assertInstanceOf<Resource.CantObtainResource<Weather>>(result)
        }

        @Test
        fun `location in db and weather in db with empty hours then Success`() = runTest {
            mockDayToHourWeatherToDomainWeather {
                val location = Location(1f, 2f)
                val locationId = 1L
                val locationModel = LocationModel(locationId, location.latitude, location.longitude)
                coEvery {
                    locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                } returns locationModel
                val dayToHourWeathers = createList(3) { createDayToHourWeather(0) }
                coEvery { dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any()) } returns dayToHourWeathers

                val result = repository.getWeatherFromNow(location)

                coVerifyOnce {
                    locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                    dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any())
                }
                assertInstanceOf<Resource.Success<Weather>>(result)
                assertEquals(Weather(dayToHourWeathers.map(DayToHourWeather::simplyToDayWeather)), result.data)
            }
        }

        @Test
        fun `location in db and weather in db with filled hours then Success`() = runTest {
            mockDayToHourWeatherToDomainWeather {
                val location = Location(1f, 2f)
                val locationId = 1L
                val locationModel = LocationModel(locationId, location.latitude, location.longitude)
                coEvery {
                    locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                } returns locationModel
                val dayToHourWeathers = createList(3) { createDayToHourWeather(5) }
                coEvery { dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any()) } returns dayToHourWeathers

                val result = repository.getWeatherFromNow(location)

                coVerifyOnce {
                    locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                    dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any())
                }
                assertInstanceOf<Resource.Success<Weather>>(result)
                assertEquals(Weather(dayToHourWeathers.map(DayToHourWeather::simplyToDayWeather)), result.data)
            }
        }

    }

    @Nested
    inner class getUpdatedWeatherFromNow {

        @Test
        fun `but location not in db and no weather from api then CantObtainResource`() =
            runTestWithMockLogEShort {
                val locationId = 2L
                val location = Location(1f, 2f)
                coEvery {
                    locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                } returns null andThen LocationModel(locationId, location.latitude, location.longitude)
                val weatherResponse = createWeatherResponseDto(0, 0)
                coEvery { api.getWeather(any(), any(), any(), any(), any(), any()) } returns weatherResponse
                coEvery { locationDao.insertAndGetId(any()) } returns locationId
                coEvery { weatherCacheDao.updateWeather(eq(locationId), any(), any()) } answers {
                    (thirdArg() as (List<Long>) -> List<HourWeatherModel>)(secondArg())
                    Unit
                }
                coEvery { dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any()) } returns emptyList()

                val result = repository.getUpdatedWeatherFromNow(location).toList()

                coVerifyOnce {
                    api.getWeather(any(), any(), any(), any(), any(), any())
                    locationDao.insertAndGetId(any())
                    weatherCacheDao.updateWeather(any(), any(), any())
                    dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any())
                }
                coVerifyTwice {
                    locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                }
                assertThat(result).hasSize(1)
                assertInstanceOf<Resource.CantObtainResource<Weather>>(result.first())
            }

        @Test
        fun `location in db but no weather from api and db then CantObtainResource`() =
            runTestWithMockLogEShort {
                val locationId = 2L
                val location = Location(1f, 2f)
                val locationModel = LocationModel(locationId, location.latitude, location.longitude)
                coEvery {
                    locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                } returns locationModel
                val weatherResponse = createWeatherResponseDto(0, 0)
                coEvery { api.getWeather(any(), any(), any(), any(), any(), any()) } returns weatherResponse
                coJustRun { weatherCacheDao.updateWeather(eq(locationId), any(), any()) }
                coEvery { dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any()) } returns emptyList()

                val result = repository.getUpdatedWeatherFromNow(location).toList()

                coVerifyNever { locationDao.insertAndGetId(any()) }
                coVerifyOnce {
                    locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                    api.getWeather(any(), any(), any(), any(), any(), any())
                    weatherCacheDao.updateWeather(any(), any(), any())
                }
                coVerifyTwice { dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any()) }
                assertThat(result).hasSize(1)
                assertInstanceOf<Resource.CantObtainResource<Weather>>(result.first())
            }

        @Test
        fun `location in db and no update required then Success and cancel`() =
            runTestWithMockLogEShort {
                mockDayToHourWeatherToDomainWeather {
                    val locationId = 2L
                    val location = Location(1f, 2f)
                    val locationModel = LocationModel(locationId, location.latitude, location.longitude)
                    coEvery {
                        locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                    } returns locationModel
                    val weatherForDay = listOf(
                        createDayToHourWeather(0),
                        createDayToHourWeather(0),
                        createDayToHourWeather(0),
                    )
                    coEvery { dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any()) } returns weatherForDay

                    repository.getUpdatedWeatherFromNow(location).test {
                        val first = awaitItem()
                        val error = awaitError()

                        coVerifyNever {
                            locationDao.insertAndGetId(any())
                            api.getWeather(any(), any(), any(), any(), any(), any())
                            weatherCacheDao.updateWeather(any(), any(), any())
                        }
                        coVerifyOnce {
                            locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                            dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any())
                        }
                        assertInstanceOf<CancellationException>(error)
                        assertInstanceOf<Resource.Success<Weather>>(first)
                        assertEquals(weatherForDay.simplyToDomainWeather(), first.data)
                    }
                }
            }

        @Test
        fun `location in db and update required then Success and CantObtainResource`() =
            runTestWithMockLogEShort {
                mockDayToHourWeatherToDomainWeather {
                    val locationId = 2L
                    val location = Location(1f, 2f)
                    val locationModel =
                        LocationModel(locationId, location.latitude, location.longitude, LocalDateTime.MIN)
                    coEvery {
                        locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                    } returns locationModel
                    val weatherResponse = createWeatherResponseDto(0, 0)
                    coEvery { api.getWeather(any(), any(), any(), any(), any(), any()) } returns weatherResponse
                    coJustRun { weatherCacheDao.updateWeather(eq(locationId), any(), any()) }
                    val weatherForDay = listOf(
                        createDayToHourWeather(0),
                        createDayToHourWeather(0),
                        createDayToHourWeather(0),
                    )
                    coEvery {
                        dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any())
                    } returns weatherForDay andThen emptyList()

                    repository.getUpdatedWeatherFromNow(location).test {
                        val first = awaitItem()
                        val second = awaitItem()

                        awaitComplete()

                        coVerifyNever { locationDao.insertAndGetId(any()) }
                        coVerifyOnce {
                            locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                            api.getWeather(any(), any(), any(), any(), any(), any())
                            weatherCacheDao.updateWeather(any(), any(), any())
                        }
                        coVerifyTwice { dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any()) }
                        assertInstanceOf<Resource.Success<Weather>>(first)
                        assertEquals(weatherForDay.simplyToDomainWeather(), first.data)
                        assertThat(second).isInstanceOf(Resource.CantObtainResource::class.java)
                    }
                }
            }

        @Test
        fun `but location not in db and update required then Success`() = runTest {
            mockDayToHourWeatherToDomainWeather {
                val locationId = 2L
                val location = Location(1f, 2f)
                val locationModel = LocationModel(locationId, location.latitude, location.longitude)
                coEvery {
                    locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                } returns null andThen locationModel
                val weatherResponse = createWeatherResponseDto(0, 0)
                coEvery { api.getWeather(any(), any(), any(), any(), any(), any()) } returns weatherResponse
                coEvery { locationDao.insertAndGetId(any()) } returns locationId
                coJustRun { weatherCacheDao.updateWeather(eq(locationId), any(), any()) }
                val weatherForDay = listOf(
                    createDayToHourWeather(0),
                    createDayToHourWeather(0),
                    createDayToHourWeather(0),
                )
                coEvery { dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any()) } returns weatherForDay

                repository.getUpdatedWeatherFromNow(location).test {
                    val first = awaitItem()

                    awaitComplete()

                    coVerifyOnce {
                        api.getWeather(any(), any(), any(), any(), any(), any())
                        locationDao.insertAndGetId(any())
                        weatherCacheDao.updateWeather(any(), any(), any())
                        dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any())
                    }
                    coVerifyTwice {
                        locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                    }
                    assertInstanceOf<Resource.Success<Weather>>(first)
                    assertEquals(weatherForDay.simplyToDomainWeather(), first.data)
                }
            }
        }

    }

    @Nested
    inner class updateWeather {

        @Test
        fun `update not required due to time then nothing`() = runTest {
            val locationId = 2L
            val location = Location(1f, 2f)
            val locationModel = LocationModel(locationId, location.latitude, location.longitude)
            coEvery {
                locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
            } returns locationModel

            repository.updateWeather(location)

            coVerifyNever {
                locationDao.insertAndGetId(any())
                api.getWeather(any(), any(), any(), any(), any(), any())
                weatherCacheDao.updateWeather(any(), any(), any())
            }
            coVerifyOnce { locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude)) }
        }

        @Test
        fun `update required but no location in db then add and update`() = runTest {
            val location = Location(1f, 2f)
            coEvery { locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude)) } returns null
            val weatherResponse = createWeatherResponseDto(0, 0)
            coEvery { api.getWeather(any(), any(), any(), any(), any(), any()) } returns weatherResponse
            val locationId = 2L
            coEvery { locationDao.insertAndGetId(any()) } returns locationId
            coJustRun { weatherCacheDao.updateWeather(eq(locationId), any(), any()) }

            repository.updateWeather(location)

            coVerifyOnce {
                locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                api.getWeather(any(), any(), any(), any(), any(), any())
                locationDao.insertAndGetId(any())
                weatherCacheDao.updateWeather(any(), any(), any())
            }
        }

        @Test
        fun `update required due to time then update`() = runTest {
            val locationId = 2L
            val location = Location(1f, 2f)
            val locationModel = LocationModel(locationId, location.latitude, location.longitude, LocalDateTime.MIN)
            coEvery {
                locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
            } returns locationModel
            val weatherResponse = createWeatherResponseDto(0, 0)
            coEvery { api.getWeather(any(), any(), any(), any(), any(), any()) } returns weatherResponse
            coEvery { locationDao.insertAndGetId(any()) } returns locationId
            coJustRun { weatherCacheDao.updateWeather(eq(locationId), any(), any()) }

            repository.updateWeather(location)

            coVerifyNever { locationDao.insertAndGetId(any()) }
            coVerifyOnce {
                locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                api.getWeather(any(), any(), any(), any(), any(), any())
                weatherCacheDao.updateWeather(any(), any(), any())
            }
        }

    }


    private fun createWeatherResponseDto(daysCount: Int, hoursCount: Int) = WeatherResponseDto(
        daily = createDailyWeatherDto(daysCount),
        hourly = createHourlyWeatherDto(hoursCount)
    )

    private inline fun mockDayToHourWeatherToDomainWeather(block: () -> Unit) =
        mockkStatic(List<DayToHourWeather>::toDomainWeather) {
            every { any<List<DayToHourWeather>>().toDomainWeather() } answers {
                firstArg<List<DayToHourWeather>>().simplyToDomainWeather()
            }

            block()
        }

}
