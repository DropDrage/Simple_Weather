package com.dropdrage.simpleweather.presentation.ui.city.weather

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.FragmentCityWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.ui.cities_weather.CitiesWeatherFragmentDirections
import com.dropdrage.simpleweather.presentation.util.adapter.HorizontalScrollInterceptor
import com.dropdrage.simpleweather.presentation.util.viewModels
import com.wholedetail.changemysleep.presentation.ui.utils.SimpleMarginItemDecoration
import java.util.*
import kotlin.reflect.KClass

abstract class BaseCityWeatherFragment<VM : BaseCityWeatherViewModel>(
    viewModelClass: KClass<VM>,
) : Fragment(R.layout.fragment_city_weather) {

    protected val binding by viewBinding(FragmentCityWeatherBinding::bind)
    protected val viewModel: VM by viewModels(viewModelClass)

    protected lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    protected lateinit var hourlyWeatherLayoutManager: RecyclerView.LayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button2.setOnClickListener {
            findNavController().navigate(CitiesWeatherFragmentDirections.navigateCityList())
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

            addOnItemTouchListener(HorizontalScrollInterceptor())
        }
    }

    private fun observeViewModel() = viewModel.apply {
        cityName.observe(this@BaseCityWeatherFragment) {
            binding.city.text = it.getMessage(requireContext())
        }

        currentWeather.observe(this@BaseCityWeatherFragment, ::updateCurrentWeather)
        hourlyWeather.observe(this@BaseCityWeatherFragment) {
            hourlyWeatherAdapter.values = it

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            hourlyWeatherLayoutManager.scrollToPosition(it.indexOfFirst {
                currentDayOfMonth == it.dateTime.dayOfMonth && currentHour == it.dateTime.hour
            })
        }

        error.observe(this@BaseCityWeatherFragment) {
            Toast.makeText(requireContext(), it.getMessage(requireContext()), Toast.LENGTH_SHORT).show()
        }

        additionalObserveViewModel()
    }

    private fun updateCurrentWeather(weather: ViewHourWeather) = binding.apply {
        weatherIcon.setImageResource(weather.weatherType.iconRes)
        temperature.text = weather.temperature
        weatherDescription.setText(weather.weatherType.weatherDescriptionRes)

        pressure.text = weather.pressure
        humidity.text = weather.humidity
        windSpeed.text = weather.windSpeed
    }

    protected open fun VM.additionalObserveViewModel() {
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "start")
        viewModel.loadData()
    }


    protected companion object {
        const val TAG = "Weather"
    }

}