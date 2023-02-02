package com.dropdrage.simpleweather.weather.presentation.ui.city.weather.city

import android.os.Bundle
import android.view.View
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.weather.presentation.presentation.ui.city.weather.CityWeatherScreen
import com.dropdrage.simpleweather.weather.presentation.ui.city.weather.BaseCityWeatherFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CityWeatherFragment : BaseCityWeatherFragment<CityWeatherViewModel>(CityWeatherViewModel::class) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.order = requireArguments().getInt(ORDER_ARGUMENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.content.setContent {
            ComposeMaterial3Theme {
                CityWeatherScreen(viewModel = viewModel, citiesViewModelOwner = requireParentFragment())
            }
        }
    }


    companion object {
        const val ORDER_ARGUMENT = "order"
    }

}
