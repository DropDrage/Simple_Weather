package com.dropdrage.simpleweather.data.weather.test.repository

import com.dropdrage.common.domain.Resource
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.weather.local.cache.dao.DayWeatherDao
import com.dropdrage.simpleweather.data.weather.local.cache.dao.LocationDao
import com.dropdrage.simpleweather.data.weather.local.cache.dao.WeatherCacheDao
import com.dropdrage.simpleweather.data.weather.local.cache.model.HourWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.model.LocationModel
import com.dropdrage.simpleweather.data.weather.local.cache.relation.DayToHourWeather
import com.dropdrage.simpleweather.data.weather.local.util.mapper.toDomainWeather
import com.dropdrage.simpleweather.data.weather.remote.WeatherApi
import com.dropdrage.simpleweather.data.weather.remote.WeatherResponseDto
import com.dropdrage.simpleweather.data.weather.repository.WeatherRepositoryImpl
import com.dropdrage.simpleweather.data.weather.util.createDailyWeatherDto
import com.dropdrage.simpleweather.data.weather.util.createDayToHourWeather
import com.dropdrage.simpleweather.data.weather.util.createHourlyWeatherDto
import com.dropdrage.simpleweather.data.weather.util.simplyToDayWeather
import com.dropdrage.simpleweather.data.weather.util.simplyToDomainWeather
import com.dropdrage.simpleweather.weather.domain.weather.Weather
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
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


    //region getWeatherFromNow

    @Test
    fun `getWeatherFromNow location not in db db then CantObtainResource`() = runTest {
        val location = Location(1f, 2f)
        coEvery { locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude)) } returns null

        val result = repository.getWeatherFromNow(location)

        coVerifyNever { dayWeatherDao.getWeatherForLocationFromDay(any(), any()) }
        coVerifyOnce { locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude)) }
        assertThat(result).isInstanceOf(Resource.CantObtainResource::class.java)
    }

    @Test
    fun `getWeatherFromNow location in db but weather empty in db then CantObtainResource`() = runTest {
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
        assertThat(result).isInstanceOf(Resource.CantObtainResource::class.java)
    }

    @Test
    fun `getWeatherFromNow location in db and weather in db with empty hours then Success`() = runTest {
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
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val resultData = (result as Resource.Success<Weather>).data
            assertEquals(Weather(dayToHourWeathers.map(DayToHourWeather::simplyToDayWeather)), resultData)
        }
    }

    @Test
    fun `getWeatherFromNow location in db and weather in db with filled hours then Success`() = runTest {
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
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val resultData = (result as Resource.Success<Weather>).data
            assertEquals(Weather(dayToHourWeathers.map(DayToHourWeather::simplyToDayWeather)), resultData)
        }
    }

    //endregion

    //region getWeatherFromNow

    @Test
    fun `getUpdatedWeatherFromNow but location not in db and no weather from api then CantObtainResource`() =
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
            assertThat(result[0]).isInstanceOf(Resource.CantObtainResource::class.java)
        }

    @Test
    fun `getUpdatedWeatherFromNow and location in db but no weather from api and db then CantObtainResource`() =
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
            assertThat(result[0]).isInstanceOf(Resource.CantObtainResource::class.java)
        }

    @Test
    fun `getUpdatedWeatherFromNow and location in db and no update required then Success`() = runTestWithMockLogEShort {
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

            val result = ArrayList<Resource<Weather>>(1)
            assertThrows<CancellationException> {
                repository.getUpdatedWeatherFromNow(location).toList(result)
            }

            coVerifyNever {
                locationDao.insertAndGetId(any())
                api.getWeather(any(), any(), any(), any(), any(), any())
                weatherCacheDao.updateWeather(any(), any(), any())
            }
            coVerifyOnce {
                locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any())
            }
            assertThat(result).hasSize(1)
            assertThat(result.first()).isInstanceOf(Resource.Success::class.java)
            val resultData = (result.first() as Resource.Success<Weather>).data
            assertEquals(weatherForDay.simplyToDomainWeather(), resultData)
        }
    }

    @Test
    fun `getUpdatedWeatherFromNow and location in db and update required then Success and CantObtainResource`() =
        runTestWithMockLogEShort {
            mockDayToHourWeatherToDomainWeather {
                val locationId = 2L
                val location = Location(1f, 2f)
                val locationModel = LocationModel(locationId, location.latitude, location.longitude, LocalDateTime.MIN)
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

                val result = repository.getUpdatedWeatherFromNow(location).toList()

                coVerifyNever { locationDao.insertAndGetId(any()) }
                coVerifyOnce {
                    locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude))
                    api.getWeather(any(), any(), any(), any(), any(), any())
                    weatherCacheDao.updateWeather(any(), any(), any())
                }
                coVerifyTwice { dayWeatherDao.getWeatherForLocationFromDay(eq(locationId), any()) }
                assertThat(result).hasSize(2)
                assertThat(result[0]).isInstanceOf(Resource.Success::class.java)
                val resultData = (result[0] as Resource.Success<Weather>).data
                assertEquals(weatherForDay.simplyToDomainWeather(), resultData)
                assertThat(result[1]).isInstanceOf(Resource.CantObtainResource::class.java)
            }
        }

    @Test
    fun `getUpdatedWeatherFromNow but location not in db and update required then Success`() = runTest {
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
            assertThat(result[0]).isInstanceOf(Resource.Success::class.java)
            val resultData = (result[0] as Resource.Success<Weather>).data
            assertEquals(weatherForDay.simplyToDomainWeather(), resultData)
        }
    }

    //endregion

    //region updateWeather

    @Test
    fun `updateWeather update not required due to no location in db`() = runTest {
        val location = Location(1f, 2f)
        coEvery { locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude)) } returns null

        repository.updateWeather(location)

        coVerifyNever {
            locationDao.insertAndGetId(any())
            api.getWeather(any(), any(), any(), any(), any(), any())
            weatherCacheDao.updateWeather(any(), any(), any())
        }
        coVerifyOnce { locationDao.getLocationApproximately(eq(location.latitude), eq(location.longitude)) }
    }

    @Test
    fun `updateWeather update not required due to time`() = runTest {
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
    fun `updateWeather update required`() = runTest {
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

    //endregion


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
