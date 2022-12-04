package com.dropdrage.simpleweather.presentation.ui.cities_weather

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.FragmentCitiesWeatherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitiesWeatherFragment : Fragment(R.layout.fragment_cities_weather) {

    private val binding by viewBinding(FragmentCitiesWeatherBinding::bind)
    private val viewModel: CitiesWeatherViewModel by viewModels()

    private lateinit var citiesAdapter: CitiesWeatherAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.citiesWeather.adapter = CitiesWeatherAdapter(childFragmentManager, lifecycle)
            .also { citiesAdapter = it }
        viewModel.loadCities()
        observeViewModel()
    }

    private fun observeViewModel() = viewModel.apply {
        cities.observe(this@CitiesWeatherFragment) {
            citiesAdapter.citiesCount = it.size
        }
    }
}