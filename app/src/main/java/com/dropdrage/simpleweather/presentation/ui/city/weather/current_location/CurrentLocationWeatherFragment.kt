package com.dropdrage.simpleweather.presentation.ui.city.weather.current_location

import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
private const val GPS_ACTIVATION_REQUEST_CODE = 100

@AndroidEntryPoint
class CurrentLocationWeatherFragment :
    BaseCityWeatherFragment<CurrentLocationWeatherViewModel>(CurrentLocationWeatherViewModel::class) {

    private lateinit var permissionRequest: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.loadWeather()
            } else {
                Log.d(TAG, "Permission isn't granted")
            }
        }
    }


    override fun CurrentLocationWeatherViewModel.additionalObserveViewModel() {
        locationObtainingError.observe(viewLifecycleOwner) {
            when (it) {
                is LocationResult.NoPermission -> requestLocationPermission(it.permission)
                LocationResult.GpsDisabled -> requestGpsActivation()
                LocationResult.NoLocation -> {}
            }
        }
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
                    exception.startResolutionForResult(requireActivity(), GPS_ACTIVATION_REQUEST_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e(TAG, "Exception", sendEx)
                }
            }
        }
    }

}