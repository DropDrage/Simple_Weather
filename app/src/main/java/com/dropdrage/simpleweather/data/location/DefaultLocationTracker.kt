package com.dropdrage.simpleweather.data.location

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.annotation.CheckResult
import androidx.core.content.ContextCompat
import com.dropdrage.simpleweather.data.util.toLocationResult
import com.dropdrage.simpleweather.domain.location.LocationResult
import com.dropdrage.simpleweather.domain.location.LocationTracker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "Location_Tracker"

private const val LOCATION_REQUEST_INTERVAL = 60000L

class DefaultLocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application,
) : LocationTracker {

    override suspend fun getCurrentLocation(): LocationResult {
        val locationAvailabilityResult = checkLocationAvailability()
        if (locationAvailabilityResult != null) {
            return locationAvailabilityResult
        }

        //noinspection MissingPermission
        val lastLocation = locationClient.lastLocation.await()
        return lastLocation.toLocationResult()
    }

    override suspend fun requestLocationUpdate(): Flow<LocationResult> = channelFlow {
        val locationAvailabilityResult = checkLocationAvailability()
        if (locationAvailabilityResult != null) {
            channel.send(locationAvailabilityResult)
            channel.close()
            return@channelFlow
        }

        val locationRequest: LocationRequest =
            LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, LOCATION_REQUEST_INTERVAL)
                .setMaxUpdates(1)
                .build()

        //noinspection MissingPermission
        locationClient.requestLocationUpdates(locationRequest, { location ->
            runBlocking {
                channel.send(location.toLocationResult())
                channel.close()
            }
        }, Looper.getMainLooper()).await()

        awaitClose()
    }


    @CheckResult
    private fun checkLocationAvailability(): LocationResult? =
        if (!hasLocationPermission()) LocationResult.NoPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        else if (!isGpsEnabled()) LocationResult.GpsDisabled
        else null

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

    private fun isGpsEnabled(): Boolean {
        val locationManager = application.getSystemService(LocationManager::class.java)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            locationManager.isLocationEnabled
        else locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}