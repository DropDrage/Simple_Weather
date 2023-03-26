package com.dropdrage.simpleweather.data.location.test

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.dropdrage.simpleweather.data.location.DefaultLocationTracker
import com.dropdrage.simpleweather.data.location.util.mockLocation
import com.dropdrage.simpleweather.weather.domain.location.LocationResult
import com.dropdrage.test.util.justMock
import com.dropdrage.test.util.mockLooper
import com.dropdrage.test.util.runTestWithMockLooper
import com.dropdrage.test.util.setSdkSuspend
import com.dropdrage.test.util.verifyNever
import com.dropdrage.test.util.verifyOnce
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private const val TASK_KT = "kotlinx.coroutines.tasks.TasksKt"
private const val CHECK_LOCATION_AVAILABILITY_METHOD = "checkLocationAvailability"

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
internal class DefaultLocationTrackerTest {

    @MockK
    lateinit var locationClient: FusedLocationProviderClient

    @MockK
    lateinit var context: Context

    private lateinit var locationTracker: DefaultLocationTracker


    @BeforeEach
    fun setUp() {
        locationTracker = spyk(DefaultLocationTracker(locationClient, context))
    }


    //region getCurrentLocation

    @Test
    fun `getCurrentLocation but location unavailable due to no permission`() =
        runContextCompatPermissionTest(PERMISSION_DENIED) {
            val result = locationTracker.getCurrentLocation()

            verifyNever {
                locationClient.lastLocation
                locationClient.requestLocationUpdates(any(), any<LocationListener>(), any())
            }
            assertThat(result).isInstanceOf(LocationResult.NoPermission::class.java)
            val resultPermission = (result as LocationResult.NoPermission).permission
            assertEquals(Manifest.permission.ACCESS_COARSE_LOCATION, resultPermission)
        }

    @Test
    fun `getCurrentLocation but location unavailable due to gps disabled with SDK P`() =
        runContextCompatPermissionTest {
            setSdkSuspend(Build.VERSION_CODES.P) {
                val locationManager = mockk<LocationManager> {
                    every { isLocationEnabled } returns false
                }
                every { context.getSystemService(eq(LocationManager::class.java)) } returns locationManager

                val result = locationTracker.getCurrentLocation()

                verifyNever {
                    locationClient.lastLocation
                    locationClient.requestLocationUpdates(any(), any<LocationListener>(), any())
                }
                assertSame(LocationResult.GpsDisabled, result)
            }
        }

    @Test
    fun `getCurrentLocation location available but lastLocation null returns NoLocation`() = runTest {
        mockTaskKt {
            every { locationTracker["checkLocationAvailability"]() } returns null
            coEvery { locationClient.lastLocation.await() } returns null

            val result = locationTracker.getCurrentLocation()

            verifyNever { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
            verifyOnce { locationClient.lastLocation }
            assertSame(LocationResult.NoLocation, result)
        }
    }

    @Test
    fun `getCurrentLocation location available and lastLocation exists returns Success`() = runTest {
        mockTaskKt {
            every { locationTracker["checkLocationAvailability"]() } returns null
            val latitude = 1.0
            val longitude = 2.0
            val lastLocation = mockLocation(latitude, longitude)
            coEvery { locationClient.lastLocation.await() } returns lastLocation

            val result = locationTracker.getCurrentLocation()

            verifyNever { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
            verifyOnce { locationClient.lastLocation }
            assertThat(result).isInstanceOf(LocationResult.Success::class.java)
            val resultLocation = (result as LocationResult.Success).location
            assertEquals(latitude.toFloat(), resultLocation.latitude)
            assertEquals(longitude.toFloat(), resultLocation.longitude)
        }
    }

    @Test
    fun `getCurrentLocation but location unavailable due to gps disabled both providers with SDK O`() =
        runContextCompatPermissionTest {
            setSdkSuspend(Build.VERSION_CODES.O) {
                mockLocationManager(false, false)

                val result = locationTracker.getCurrentLocation()

                verifyNever {
                    locationClient.lastLocation
                    locationClient.requestLocationUpdates(any(), any<LocationListener>(), any())
                }
                assertSame(LocationResult.GpsDisabled, result)
            }
        }

    @Test
    fun `getCurrentLocation but location available with gps by network provider with SDK O but NoLocation`() = runTest {
        mockTaskKt {
            setSdkSuspend(Build.VERSION_CODES.O) {
                mockLocationManager(true, false)
                every { locationTracker[CHECK_LOCATION_AVAILABILITY_METHOD]() } returns null
                coEvery { locationClient.lastLocation.await() } returns null

                val result = locationTracker.getCurrentLocation()

                verifyNever { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
                verifyOnce { locationClient.lastLocation }
                assertSame(LocationResult.NoLocation, result)
            }
        }
    }

    @Test
    fun `getCurrentLocation but location available with gps by network provider with SDK O and Success`() = runTest {
        mockTaskKt {
            setSdkSuspend(Build.VERSION_CODES.O) {
                mockLocationManager(true, false)
                every { locationTracker[CHECK_LOCATION_AVAILABILITY_METHOD]() } returns null
                val latitude = 1.0
                val longitude = 2.0
                val lastLocation = mockLocation(latitude, longitude)
                coEvery { locationClient.lastLocation.await() } returns lastLocation

                val result = locationTracker.getCurrentLocation()

                verifyNever { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
                verifyOnce { locationClient.lastLocation }
                assertThat(result).isInstanceOf(LocationResult.Success::class.java)
                val resultLocation = (result as LocationResult.Success).location
                assertEquals(latitude.toFloat(), resultLocation.latitude)
                assertEquals(longitude.toFloat(), resultLocation.longitude)
            }
        }
    }

    @Test
    fun `getCurrentLocation but location available with gps by gps provider with SDK O but NoLocation`() =
        runContextCompatPermissionTest {
            mockTaskKt {
                setSdkSuspend(Build.VERSION_CODES.O) {
                    mockLocationManager(false, true)
                    every { locationTracker[CHECK_LOCATION_AVAILABILITY_METHOD]() } returns null
                    coEvery { locationClient.lastLocation.await() } returns null

                    val result = locationTracker.getCurrentLocation()

                    verifyNever { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
                    verifyOnce { locationClient.lastLocation }
                    assertSame(LocationResult.NoLocation, result)
                }
            }
        }

    @Test
    fun `getCurrentLocation but location available with gps by gps provider with SDK O and Success`() = runTest {
        mockTaskKt {
            setSdkSuspend(Build.VERSION_CODES.O) {
                mockLocationManager(false, true)
                every { locationTracker[CHECK_LOCATION_AVAILABILITY_METHOD]() } returns null
                val latitude = 1.0
                val longitude = 2.0
                val lastLocation = mockLocation(latitude, longitude)
                coEvery { locationClient.lastLocation.await() } returns lastLocation

                val result = locationTracker.getCurrentLocation()

                verifyNever { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
                verifyOnce { locationClient.lastLocation }
                assertThat(result).isInstanceOf(LocationResult.Success::class.java)
                val resultLocation = (result as LocationResult.Success).location
                assertEquals(latitude.toFloat(), resultLocation.latitude)
                assertEquals(longitude.toFloat(), resultLocation.longitude)
            }
        }
    }

    @Test
    fun `getCurrentLocation but location available with gps by both provider with SDK O but NoLocation`() = runTest {
        mockTaskKt {
            setSdkSuspend(Build.VERSION_CODES.O) {
                mockLocationManager(true, true)
                every { locationTracker[CHECK_LOCATION_AVAILABILITY_METHOD]() } returns null
                coEvery { locationClient.lastLocation.await() } returns null

                val result = locationTracker.getCurrentLocation()

                verifyNever { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
                verifyOnce { locationClient.lastLocation }
                assertSame(LocationResult.NoLocation, result)
            }
        }
    }

    @Test
    fun `getCurrentLocation but location available with gps by both provider with SDK O and Success`() = runTest {
        mockTaskKt {
            setSdkSuspend(Build.VERSION_CODES.O) {
                mockLocationManager(true, true)
                every { locationTracker[CHECK_LOCATION_AVAILABILITY_METHOD]() } returns null
                val latitude = 1.0
                val longitude = 2.0
                val lastLocation = mockLocation(latitude, longitude)
                coEvery { locationClient.lastLocation.await() } returns lastLocation

                val result = locationTracker.getCurrentLocation()

                verifyNever { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
                verifyOnce { locationClient.lastLocation }
                assertThat(result).isInstanceOf(LocationResult.Success::class.java)
                val resultLocation = (result as LocationResult.Success).location
                assertEquals(latitude.toFloat(), resultLocation.latitude)
                assertEquals(longitude.toFloat(), resultLocation.longitude)
            }
        }
    }

    //endregion


    //region requestLocationUpdate

    @Test
    fun `requestLocationUpdate but location unavailable due to no permission`() =
        runContextCompatPermissionTest(PERMISSION_DENIED) {
            val result = locationTracker.requestLocationUpdate().first()

            verifyNever {
                locationClient.lastLocation
                locationClient.requestLocationUpdates(any(), any<LocationListener>(), any())
            }
            assertThat(result).isInstanceOf(LocationResult.NoPermission::class.java)
            val resultPermission = (result as LocationResult.NoPermission).permission
            assertEquals(Manifest.permission.ACCESS_COARSE_LOCATION, resultPermission)
        }

    @Test
    fun `requestLocationUpdate but location unavailable due to gps disabled with SDK P`() =
        runContextCompatPermissionTest {
            setSdkSuspend(Build.VERSION_CODES.P) {
                val locationManager = mockk<LocationManager> {
                    every { isLocationEnabled } returns false
                }
                every { context.getSystemService(eq(LocationManager::class.java)) } returns locationManager

                val result = locationTracker.requestLocationUpdate().first()

                verifyNever {
                    locationClient.lastLocation
                    locationClient.requestLocationUpdates(any(), any<LocationListener>(), any())
                }
                assertSame(LocationResult.GpsDisabled, result)
            }
        }

    @Test
    fun `requestLocationUpdate location available returns Success`() = runTestWithMockLooper {
        mockLocationRequestBuilder {
            mockTaskKt {
                every { locationTracker[CHECK_LOCATION_AVAILABILITY_METHOD]() } returns null
                val latitude = 1.0
                val longitude = 2.0
                val location = mockLocation(latitude, longitude)
                every { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) } answers {
                    (args[1] as LocationListener).onLocationChanged(location)
                    mockk(relaxed = true)
                }

                val result = locationTracker.requestLocationUpdate().first()

                verifyNever { locationClient.lastLocation }
                verifyOnce { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
                assertThat(result).isInstanceOf(LocationResult.Success::class.java)
                val resultLocation = (result as LocationResult.Success).location
                assertEquals(latitude.toFloat(), resultLocation.latitude)
                assertEquals(longitude.toFloat(), resultLocation.longitude)
            }
        }
    }

    @Test
    fun `requestLocationUpdate but location unavailable due to gps disabled both providers with SDK O`() =
        runContextCompatPermissionTest {
            setSdkSuspend(Build.VERSION_CODES.O) {
                mockLocationManager(false, false)

                val result = locationTracker.requestLocationUpdate().first()

                verifyNever {
                    locationClient.lastLocation
                    locationClient.requestLocationUpdates(any(), any<LocationListener>(), any())
                }
                assertSame(LocationResult.GpsDisabled, result)
            }
        }

    @Test
    fun `requestLocationUpdate but location available with gps by network provider with SDK O and Success`() =
        runContextCompatPermissionTest {
            mockLooper {
                setSdkSuspend(Build.VERSION_CODES.O) {
                    mockLocationManager(true, false)
                    every { locationTracker[CHECK_LOCATION_AVAILABILITY_METHOD]() } returns null
                    val latitude = 1.0
                    val longitude = 2.0
                    val location = mockLocation(latitude, longitude)
                    every { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) } answers {
                        (args[1] as LocationListener).onLocationChanged(location)
                        mockk(relaxed = true)
                    }

                    val result = locationTracker.requestLocationUpdate().first()

                    verifyNever { locationClient.lastLocation }
                    verifyOnce { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
                    assertThat(result).isInstanceOf(LocationResult.Success::class.java)
                    val resultLocation = (result as LocationResult.Success).location
                    assertEquals(latitude.toFloat(), resultLocation.latitude)
                    assertEquals(longitude.toFloat(), resultLocation.longitude)
                }
            }
        }

    @Test
    fun `requestLocationUpdate but location available with gps by gps provider with SDK O and Success`() =
        runContextCompatPermissionTest {
            mockLooper {
                setSdkSuspend(Build.VERSION_CODES.O) {
                    mockLocationManager(false, true)
                    every { locationTracker[CHECK_LOCATION_AVAILABILITY_METHOD]() } returns null
                    val latitude = 1.0
                    val longitude = 2.0
                    val location = mockLocation(latitude, longitude)
                    every { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) } answers {
                        (args[1] as LocationListener).onLocationChanged(location)
                        mockk(relaxed = true)
                    }

                    val result = locationTracker.requestLocationUpdate().first()

                    verifyNever { locationClient.lastLocation }
                    verifyOnce { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
                    assertThat(result).isInstanceOf(LocationResult.Success::class.java)
                    val resultLocation = (result as LocationResult.Success).location
                    assertEquals(latitude.toFloat(), resultLocation.latitude)
                    assertEquals(longitude.toFloat(), resultLocation.longitude)
                }
            }
        }

    @Test
    fun `requestLocationUpdate but location available with gps by both provider with SDK O and Success`() =
        runContextCompatPermissionTest {
            mockLooper {
                setSdkSuspend(Build.VERSION_CODES.O) {
                    mockLocationManager(true, true)
                    every { locationTracker[CHECK_LOCATION_AVAILABILITY_METHOD]() } returns null
                    val latitude = 1.0
                    val longitude = 2.0
                    val location = mockLocation(latitude, longitude)
                    every { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) } answers {
                        (args[1] as LocationListener).onLocationChanged(location)
                        mockk(relaxed = true)
                    }

                    val result = locationTracker.requestLocationUpdate().first()

                    verifyNever { locationClient.lastLocation }
                    verifyOnce { locationClient.requestLocationUpdates(any(), any<LocationListener>(), any()) }
                    assertThat(result).isInstanceOf(LocationResult.Success::class.java)
                    val resultLocation = (result as LocationResult.Success).location
                    assertEquals(latitude.toFloat(), resultLocation.latitude)
                    assertEquals(longitude.toFloat(), resultLocation.longitude)
                }
            }
        }

    //endregion


    private inline fun runContextCompatPermissionTest(
        permissionAnswer: Int = PERMISSION_GRANTED,
        crossinline block: suspend () -> Unit,
    ) = runTest {
        mockkStatic(ContextCompat::class) {
            every {
                ContextCompat.checkSelfPermission(eq(context), eq(Manifest.permission.ACCESS_COARSE_LOCATION))
            } returns permissionAnswer

            block()
        }
    }

    private inline suspend fun mockTaskKt(crossinline block: suspend () -> Unit) = mockkStatic(TASK_KT) {
        block()
    }

    private fun mockLocationManager(isNetworkEnabled: Boolean, isGpsEnabled: Boolean) {
        every { context.getSystemService(eq(LocationManager::class.java)) } returns mockk {
            every { isProviderEnabled(eq(LocationManager.NETWORK_PROVIDER)) } returns isNetworkEnabled
            every { isProviderEnabled(eq(LocationManager.GPS_PROVIDER)) } returns isGpsEnabled
        }
    }

    private inline suspend fun mockLocationRequestBuilder(crossinline block: suspend () -> Unit) =
        mockkConstructor(LocationRequest.Builder::class) {
            justMock { anyConstructed<LocationRequest.Builder>().build() }

            block()
        }

}
