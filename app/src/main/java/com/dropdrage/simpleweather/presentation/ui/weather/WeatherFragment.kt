package com.dropdrage.simpleweather.presentation.ui.weather

import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.FragmentWeatherBinding
import com.dropdrage.simpleweather.domain.location.LocationResult
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.wholedetail.changemysleep.presentation.ui.utils.SimpleMarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "Weather"

private const val REQUEST_INTERVAL = 5000L
private const val GPS_ACTIVATION_REQUEST_CODE = 100

@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private val binding by viewBinding(FragmentWeatherBinding::bind)
    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var permissionRequest: ActivityResultLauncher<String>

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var hourlyWeatherLayoutManager: RecyclerView.LayoutManager


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button2.setOnClickListener {
            findNavController().navigate(WeatherFragmentDirections.navigateCityList())
        }
        initHourlyWeather()
        observeViewModel()
    }

    private fun initHourlyWeather() {
        binding.hourlyWeather.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                .also { hourlyWeatherLayoutManager = it }
            adapter = HourlyWeatherAdapter().also { hourlyWeatherAdapter = it }

            addItemDecoration(SimpleMarginItemDecoration(
                leftMargin = resources.getDimensionPixelSize(R.dimen.small_100),
                rightMargin = resources.getDimensionPixelSize(R.dimen.small_100)
            ))
        }
    }

    private fun observeViewModel() = viewModel.apply {
        currentWeather.observe(this@WeatherFragment, ::updateCurrentWeather)
        hourlyWeather.observe(this@WeatherFragment) {
            hourlyWeatherAdapter.values = it

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            hourlyWeatherLayoutManager.scrollToPosition(it.indexOfFirst {
                currentDayOfMonth == it.dateTime.dayOfMonth && currentHour == it.dateTime.hour
            })
        }

        error.observe(this@WeatherFragment) {
            Toast.makeText(requireContext(), it.getMessage(requireContext()), Toast.LENGTH_SHORT).show()
        }
        locationObtainingError.observe(this@WeatherFragment) {
            when (it) {
                is LocationResult.NoPermission -> requestLocationPermission(it.permission)
                LocationResult.GpsDisabled -> requestGpsActivation()
                LocationResult.NoLocation -> {}
            }
        }
    }

    private fun updateCurrentWeather(weather: ViewHourWeather) = binding.apply {
        weatherIcon.setImageResource(weather.weatherType.iconRes)
        temperature.text = weather.temperature
        weatherDescription.setText(weather.weatherType.weatherDescriptionRes)

        pressure.text = weather.pressure
        humidity.text = weather.humidity
        windSpeed.text = weather.windSpeed
    }

    private fun requestLocationPermission(permission: String) {
        Log.d(TAG, "Permission requested")
        permissionRequest.launch(permission)
    }

    fun requestGpsActivation() {
        Log.d(TAG, "GPS activation requested")

        val locationRequest: LocationRequest =
            LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, REQUEST_INTERVAL).build()
        val locationSettingsRequest: LocationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val client = LocationServices.getSettingsClient(requireContext())
        val task = client.checkLocationSettings(locationSettingsRequest)
        task.addOnSuccessListener { locationSettingsResponse ->
            val state = locationSettingsResponse.locationSettingsStates!!
            val response = "GPS >> (Present: ${state.isGpsPresent}  | Usable: ${state.isGpsUsable}) \n" +
                "Network >> (Present: ${state.isNetworkLocationPresent} | Usable: ${state.isNetworkLocationUsable}) \n" +
                "Location >> (Present: ${state.isLocationPresent} | Usable: ${state.isLocationUsable})"
            Log.d(TAG, response)

            viewModel.loadWeather()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(requireActivity(), GPS_ACTIVATION_REQUEST_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e(TAG, "Exception", sendEx)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadWeather()
    }

}