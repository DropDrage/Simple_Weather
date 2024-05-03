package com.dropdrage.simpleweather.feature.weather.presentation.ui.city.city

import android.os.Bundle
import com.dropdrage.simpleweather.feature.weather.presentation.ui.city.BaseCityWeatherFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CityWeatherFragment : BaseCityWeatherFragment<CityWeatherViewModel>(CityWeatherViewModel::class) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.order = requireArguments().getInt(ORDER_ARGUMENT)
    }


    companion object {
        const val ORDER_ARGUMENT = "order"
    }

}
