package com.dropdrage.simpleweather.feature.weather.presentation.ui.city

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.common.presentation.util.TextMessage
import com.dropdrage.common.presentation.util.extension.setLinearLayoutManager
import com.dropdrage.common.presentation.util.extension.setPool
import com.dropdrage.common.presentation.utils.CommonDimen
import com.dropdrage.common.presentation.utils.SimpleMarginItemDecoration
import com.dropdrage.common.presentation.utils.collectWithViewLifecycle
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.feature.weather.R
import com.dropdrage.simpleweather.feature.weather.databinding.FragmentCityWeatherBinding
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCurrentDayWeather
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCurrentHourWeather
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.feature.weather.presentation.ui.cities_weather.CitiesSharedViewModel
import com.dropdrage.simpleweather.feature.weather.presentation.util.adapter.HorizontalScrollInterceptor
import com.dropdrage.simpleweather.feature.weather.presentation.util.extension.setWeather
import com.dropdrage.simpleweather.feature.weather.presentation.util.extension.viewModels
import com.dropdrage.util.extension.implicitAccess
import kotlin.reflect.KClass

internal abstract class BaseCityWeatherFragment<VM : BaseCityWeatherViewModel>(
    viewModelClass: KClass<VM>,
) : Fragment(R.layout.fragment_city_weather) {

    protected val binding by viewBinding(FragmentCityWeatherBinding::bind)
    protected val viewModel: VM by viewModels(viewModelClass)
    private val citiesSharedModel: CitiesSharedViewModel by viewModels(ownerProducer = ::requireParentFragment)

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var hourlyWeatherLayoutManager: RecyclerView.LayoutManager

    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWeatherLists()
        observeViewModel()
    }

    private fun initWeatherLists() {
        val marginDecoration = SimpleMarginItemDecoration(
            horizontalMargin = resources.getDimensionPixelSize(CommonDimen.small_100)
        )
        val horizontalScrollInterceptor = HorizontalScrollInterceptor()

        initHourlyWeather(marginDecoration, horizontalScrollInterceptor)
        initDailyWeather(marginDecoration, horizontalScrollInterceptor)
    }

    private fun initHourlyWeather(
        marginDecoration: RecyclerView.ItemDecoration,
        horizontalScrollInterceptor: HorizontalScrollInterceptor,
    ) = binding.hourlyWeather.implicitAccess {
        setLinearLayoutManager(LinearLayoutManager.HORIZONTAL, false).also { hourlyWeatherLayoutManager = it }
        adapter = HourlyWeatherAdapter { makeToast(it.weatherType) }.also { hourlyWeatherAdapter = it }
        setHourlyWeatherPool(hourlyWeatherAdapter)

        setHasFixedSize(true)

        addItemDecoration(marginDecoration)
        addOnItemTouchListener(horizontalScrollInterceptor)
    }

    private fun RecyclerView.setHourlyWeatherPool(adapter: HourlyWeatherAdapter) {
        citiesSharedModel.createHourWeatherPoolIfNeeded(requireContext(), adapter::createViewHolder)
        setPool(citiesSharedModel.hourlyWeatherRecyclerPool)
    }

    private fun initDailyWeather(
        marginDecoration: RecyclerView.ItemDecoration,
        horizontalScrollInterceptor: HorizontalScrollInterceptor,
    ) = binding.dailyWeather.implicitAccess {
        setLinearLayoutManager(LinearLayoutManager.HORIZONTAL, false)
        adapter = DailyWeatherAdapter { makeToast(it.weatherType) }.also { dailyWeatherAdapter = it }
        setDailyWeatherPool(dailyWeatherAdapter)

        setHasFixedSize(true)

        addItemDecoration(marginDecoration)
        addOnItemTouchListener(horizontalScrollInterceptor)
    }

    private fun makeToast(weatherType: ViewWeatherType) {
        Toast.makeText(requireContext(), weatherType.weatherDescriptionRes, Toast.LENGTH_SHORT).show()
    }

    private fun RecyclerView.setDailyWeatherPool(adapter: DailyWeatherAdapter) {
        citiesSharedModel.createDailyWeatherPoolIfNeeded(requireContext(), adapter::createViewHolder)
        setPool(citiesSharedModel.dailyWeatherRecyclerPool)
    }

    @CallSuper
    protected open fun observeViewModel() = viewModel.implicitAccess {
        collectWithViewLifecycle(cityTitle, ::setCityTitle)

        collectWithViewLifecycle(currentDayWeather, ::updateCurrentDayWeather)
        collectWithViewLifecycle(currentHourWeather, ::updateCurrentHourWeather)
        collectWithViewLifecycle(hourlyWeather, ::updateHourlyWeather)
        collectWithViewLifecycle(dailyWeather, ::updateDailyWeather)

        collectWithViewLifecycle(error, ::showErrorMessage)
    }

    private fun setCityTitle(title: ViewCityTitle) {
        citiesSharedModel.setCityTitle(title)
    }

    private fun updateCurrentDayWeather(weather: ViewCurrentDayWeather) = binding.implicitAccess {
        temperatureRange.setTextEndOnTop(weather.temperatureRange)
        apparentTemperatureRange.setTextEndOnTop(weather.apparentTemperatureRange)
        precipitation.setText(weather.precipitationSum)
        maxWindSpeed.setText(weather.maxWindSpeed)

        sunTimes.setSunTimes(weather.sunrise, weather.sunset, weather.sunriseTime, weather.sunsetTime)
    }

    private fun updateCurrentHourWeather(weather: ViewCurrentHourWeather) = binding.implicitAccess {
        weatherIcon.setWeather(weather.weatherType)
        weatherDescription.setText(weather.weatherType.weatherDescriptionRes)
        temperature.text = weather.temperature

        pressure.text = weather.pressure
        humidity.text = weather.humidity
        wind.text = weather.windSpeed
        visibility.text = weather.visibility
    }

    private fun updateHourlyWeather(hourWeatherList: List<ViewHourWeather>) {
        hourlyWeatherAdapter.submitList(hourWeatherList)

        hourlyWeatherLayoutManager.scrollToPosition(hourWeatherList.indexOfFirst { it.isNow })
    }

    private fun updateDailyWeather(dailyWeatherList: List<ViewDayWeather>) {
        dailyWeatherAdapter.submitList(dailyWeatherList)
    }

    private fun showErrorMessage(message: TextMessage) {
        message.let {
            val context = requireContext()
            Toast.makeText(context, it.getMessage(context), Toast.LENGTH_SHORT).show()
        }
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