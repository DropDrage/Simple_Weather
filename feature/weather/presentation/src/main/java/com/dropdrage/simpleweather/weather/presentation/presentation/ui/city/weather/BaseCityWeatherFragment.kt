package com.dropdrage.simpleweather.weather.presentation.ui.city.weather

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.weather.presentation.R
import com.dropdrage.simpleweather.weather.presentation.databinding.FragmentCityWeatherBinding
import com.dropdrage.simpleweather.weather.presentation.util.extension.viewModels
import kotlin.reflect.KClass

internal abstract class BaseCityWeatherFragment<VM : BaseCityWeatherViewModel>(
    viewModelClass: KClass<VM>,
) : Fragment(R.layout.fragment_city_weather) {

    protected val binding by viewBinding(FragmentCityWeatherBinding::bind)
    protected val viewModel: VM by viewModels(viewModelClass)


    override fun onStart() {
        super.onStart()
        viewModel.loadWeather()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCityName()
    }

    override fun onStop() {
        super.onStop()
        viewModel.clearErrors()
    }

}