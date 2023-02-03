package com.dropdrage.simpleweather.weather.presentation.ui.cities_weather

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.common.presentation.utils.ChangeableAppBar
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.weather.presentation.R
import com.dropdrage.simpleweather.weather.presentation.databinding.FragmentCitiesWeatherBinding
import com.dropdrage.simpleweather.weather.presentation.presentation.ui.cities_weather.CitiesWeatherScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitiesWeatherFragment : Fragment(R.layout.fragment_cities_weather) {

    private val binding by viewBinding(FragmentCitiesWeatherBinding::bind)
    private val viewModel: CitiesWeatherViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as ChangeableAppBar).hideAppBar()

        binding.content.setContent {
            ComposeMaterial3Theme {
                CitiesWeatherScreen(
                    navigateCityList = { findNavController().navigate(CitiesWeatherFragmentDirections.navigateCityList()) },
                    navigateSettings = { findNavController().navigate(CitiesWeatherFragmentDirections.navigateSettingsFragment()) }
                )
            }
        }
    }


    override fun onStart() {
        super.onStart()
        viewModel.updateWeather()
    }

    override fun onDestroyView() {
        (requireActivity() as ChangeableAppBar).restoreDefaultAppBar()
        super.onDestroyView()
    }

}
