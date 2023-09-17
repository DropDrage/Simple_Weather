package com.dropdrage.simpleweather.feature.weather.presentation.ui.city.weather.current_location

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dropdrage.common.presentation.utils.collectInLaunchedEffect
import com.dropdrage.simpleweather.core.util.LogTags
import com.dropdrage.simpleweather.feature.weather.domain.location.LocationResult
import com.dropdrage.simpleweather.feature.weather.presentation.ui.cities_weather.CitiesSharedViewModel
import com.dropdrage.simpleweather.feature.weather.presentation.ui.city.current_location.CurrentLocationWeatherViewModel
import com.dropdrage.simpleweather.feature.weather.presentation.ui.city.weather.BaseCityWeatherScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task

private const val REQUEST_INTERVAL = 5000L

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun CurrentLocationWeatherScreen(isVisible: Boolean, citiesSharedViewModel: CitiesSharedViewModel) {
    val localContext = LocalContext.current

    val viewModel = hiltViewModel<CurrentLocationWeatherViewModel>()

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)
    val gpsActivationListener =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            Log.d(LogTags.WEATHER, "GPS activation result: ${it.resultCode}")
            if (it.resultCode == RESULT_OK) {
                viewModel.loadWeather()
            }
        }


    fun createCheckLocationSettingsTask(): Task<LocationSettingsResponse> {
        val locationRequest: LocationRequest =
            LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, REQUEST_INTERVAL).build()
        val locationSettingsRequest: LocationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val client = LocationServices.getSettingsClient(localContext)
        return client.checkLocationSettings(locationSettingsRequest)
    }

    fun observeCheckLocationSettingsTask(checkLocationSettings: Task<LocationSettingsResponse>) {
        checkLocationSettings.addOnSuccessListener { locationSettingsResponse ->
            val state = locationSettingsResponse.locationSettingsStates!!
            val response = "GPS >> (Present: ${state.isGpsPresent} | Usable: ${state.isGpsUsable})\n" +
                "Network >> (Present: ${state.isNetworkLocationPresent} | Usable: ${state.isNetworkLocationUsable})\n" +
                "Location >> (Present: ${state.isLocationPresent} | Usable: ${state.isLocationUsable})"
            Log.d(LogTags.WEATHER, response)

            viewModel.loadWeather()
        }
        checkLocationSettings.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    Log.d(LogTags.WEATHER, "Location settings resolving error")
                    val request = IntentSenderRequest.Builder(exception.resolution.intentSender)
                        .setFillInIntent(Intent())
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK, 0)
                        .build()
                    gpsActivationListener.launch(request)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e(LogTags.WEATHER, "Exception", sendEx)
                }
            } else {
                Log.e(LogTags.WEATHER, exception.message, exception)
            }
        }
    }

    fun requestGpsActivation() {
        Log.d(LogTags.WEATHER, "GPS activation requested")

        val checkLocationSettings = createCheckLocationSettingsTask()
        observeCheckLocationSettingsTask(checkLocationSettings)
    }


    viewModel.locationObtainingError.collectInLaunchedEffect {
        when (it) {
            is LocationResult.NoPermission -> locationPermissionState.launchPermissionRequest()
            LocationResult.GpsDisabled -> requestGpsActivation()
            else -> {}
        }
    }

    LaunchedEffect(key1 = locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            viewModel.loadWeather()
        } else {
            Log.d(LogTags.WEATHER, "Permission isn't granted")
        }
    }


    BaseCityWeatherScreen(isVisible = isVisible, viewModel = viewModel, citiesSharedViewModel = citiesSharedViewModel)
}
