package com.dropdrage.simpleweather.presentation.ui.city.weather.current_location

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dropdrage.common.presentation.utils.collectWithViewLifecycle
import com.dropdrage.simpleweather.domain.location.LocationResult
import com.dropdrage.simpleweather.presentation.ui.city.weather.BaseCityWeatherFragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint

private const val REQUEST_INTERVAL = 5000L

@AndroidEntryPoint
class CurrentLocationWeatherFragment :
    BaseCityWeatherFragment<CurrentLocationWeatherViewModel>(CurrentLocationWeatherViewModel::class) {

    private lateinit var permissionRequest: ActivityResultLauncher<String>
    private lateinit var gpsActivationListener: ActivityResultLauncher<IntentSenderRequest>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.loadWeather()
            } else {
                Log.d(TAG, "Permission isn't granted")
            }
        }

        gpsActivationListener = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            Log.d(TAG, "GPS activation result: ${it.resultCode}")
            if (it.resultCode == RESULT_OK) {
                viewModel.loadWeather()
            }
        }
    }


    override fun observeViewModel() = viewModel.apply {
        super.observeViewModel()

        collectWithViewLifecycle(locationObtainingError, {
            when (it) {
                is LocationResult.NoPermission -> requestLocationPermission(it.permission)
                LocationResult.GpsDisabled -> requestGpsActivation()
                else -> {}
            }
        })
    }

    private fun requestLocationPermission(permission: String) {
        Log.d(TAG, "Permission requested")
        permissionRequest.launch(permission)
    }

    private fun requestGpsActivation() {
        Log.d(TAG, "GPS activation requested")

        val checkLocationSettings = createCheckLocationSettingsTask()
        observeCheckLocationSettingsTask(checkLocationSettings)
    }

    private fun createCheckLocationSettingsTask(): Task<LocationSettingsResponse> {
        val locationRequest: LocationRequest =
            LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, REQUEST_INTERVAL).build()
        val locationSettingsRequest: LocationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val client = LocationServices.getSettingsClient(requireContext())
        return client.checkLocationSettings(locationSettingsRequest)
    }

    private fun observeCheckLocationSettingsTask(checkLocationSettings: Task<LocationSettingsResponse>) {
        checkLocationSettings.addOnSuccessListener { locationSettingsResponse ->
            val state = locationSettingsResponse.locationSettingsStates!!
            val response = "GPS >> (Present: ${state.isGpsPresent}  | Usable: ${state.isGpsUsable}) \n" +
                "Network >> (Present: ${state.isNetworkLocationPresent} | Usable: ${state.isNetworkLocationUsable}) \n" +
                "Location >> (Present: ${state.isLocationPresent} | Usable: ${state.isLocationUsable})"
            Log.d(TAG, response)

            viewModel.loadWeather()
        }
        checkLocationSettings.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    Log.d(TAG, "Location settings resolving error")
                    val request = IntentSenderRequest.Builder(exception.resolution.intentSender)
                        .setFillInIntent(Intent())
                        .setFlags(FLAG_ACTIVITY_CLEAR_TASK, 0)
                        .build()
                    gpsActivationListener.launch(request)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e(TAG, "Exception", sendEx)
                }
            } else {
                Log.e(TAG, exception.message, exception)
            }
        }
    }

}