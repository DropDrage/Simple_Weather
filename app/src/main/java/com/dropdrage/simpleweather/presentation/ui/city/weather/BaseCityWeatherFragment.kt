package com.dropdrage.simpleweather.presentation.ui.city.weather

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.FragmentCityWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.ui.cities_weather.TitleHolder
import com.dropdrage.simpleweather.presentation.util.SimpleMarginItemDecoration
import com.dropdrage.simpleweather.presentation.util.adapter.HorizontalScrollInterceptor
import com.dropdrage.simpleweather.presentation.util.extension.viewModels
import java.util.*
import kotlin.reflect.KClass

abstract class BaseCityWeatherFragment<VM : BaseCityWeatherViewModel>(
    viewModelClass: KClass<VM>,
) : Fragment(R.layout.fragment_city_weather) {

    protected val binding by viewBinding(FragmentCityWeatherBinding::bind)
    protected val viewModel: VM by viewModels(viewModelClass)

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var hourlyWeatherLayoutManager: RecyclerView.LayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHourlyWeather()
        observeViewModel()
    }

    private fun initHourlyWeather() {
        binding.hourlyWeather.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                .also { hourlyWeatherLayoutManager = it }
            adapter = HourlyWeatherAdapter().also { hourlyWeatherAdapter = it }

            addItemDecoration(SimpleMarginItemDecoration(
                horizontalMargin = resources.getDimensionPixelSize(R.dimen.small_100)
            ))

            addOnItemTouchListener(HorizontalScrollInterceptor())
        }
    }

    private fun observeViewModel() = viewModel.apply {
        city.observe(viewLifecycleOwner) {
            (requireParentFragment() as TitleHolder).setTitle(
                it.title.getMessage(requireContext()),
                it.subtitle.getMessage(requireContext())
            )
        }

        currentDayWeather.observe(viewLifecycleOwner, ::updateCurrentDayWeather)
        currentWeather.observe(viewLifecycleOwner, ::updateCurrentWeather)
        hourlyWeather.observe(viewLifecycleOwner) { hourWeatherList ->
            hourlyWeatherAdapter.values = hourWeatherList

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            hourlyWeatherLayoutManager.scrollToPosition(hourWeatherList.indexOfFirst {
                currentDayOfMonth == it.dateTime.dayOfMonth && currentHour == it.dateTime.hour
            })
        }

        error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.getMessage(requireContext()), Toast.LENGTH_SHORT).show()
        }

        additionalObserveViewModel()
    }

    private fun updateCurrentDayWeather(weather: ViewDayWeather) = binding.apply {
        temperatureRange.setText(weather.temperatureRange)
        apparentTemperatureRange.setText(weather.apparentTemperatureRange)
        precipitation.setText(weather.precipitationSum)
        maxWindSpeed.setText(weather.maxWindSpeed)
    }

    private fun updateCurrentWeather(weather: ViewHourWeather) = binding.apply {
        weatherIcon.setImageResource(weather.weatherType.iconRes)
        weatherIcon.contentDescription = getString(weather.weatherType.weatherDescriptionRes)
        weatherDescription.setText(weather.weatherType.weatherDescriptionRes)
        temperature.text = weather.temperature

        pressure.text = weather.pressure
        humidity.text = weather.humidity
        wind.text = weather.windSpeed
        visibility.text = weather.visibility
    }

    protected open fun VM.additionalObserveViewModel() {
    }


    override fun onStart() {
        super.onStart()
        viewModel.loadWeather()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCityName()
    }


    protected companion object {
        const val TAG = "Weather"
    }

}