package com.dropdrage.simpleweather.presentation.ui.city.weather

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.FragmentCityWeatherBinding
import com.dropdrage.simpleweather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.presentation.model.ViewCurrentDayWeather
import com.dropdrage.simpleweather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.presentation.ui.cities_weather.CitiesSharedViewModel
import com.dropdrage.simpleweather.presentation.util.SimpleMarginItemDecoration
import com.dropdrage.simpleweather.presentation.util.TextMessage
import com.dropdrage.simpleweather.presentation.util.adapter.HorizontalScrollInterceptor
import com.dropdrage.simpleweather.presentation.util.extension.setLinearLayoutManager
import com.dropdrage.simpleweather.presentation.util.extension.setPool
import com.dropdrage.simpleweather.presentation.util.extension.setWeather
import com.dropdrage.simpleweather.presentation.util.extension.viewModels
import java.util.*
import kotlin.reflect.KClass

abstract class BaseCityWeatherFragment<VM : BaseCityWeatherViewModel>(
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
            horizontalMargin = resources.getDimensionPixelSize(R.dimen.small_100)
        )
        val horizontalScrollInterceptor = HorizontalScrollInterceptor()

        initHourlyWeather(marginDecoration, horizontalScrollInterceptor)
        initDailyWeather(marginDecoration, horizontalScrollInterceptor)
    }

    private fun initHourlyWeather(
        marginDecoration: RecyclerView.ItemDecoration,
        horizontalScrollInterceptor: HorizontalScrollInterceptor,
    ) = binding.hourlyWeather.apply {
        setLinearLayoutManager(LinearLayoutManager.HORIZONTAL, false).also { hourlyWeatherLayoutManager = it }
        adapter = HourlyWeatherAdapter { makeToast(it.weatherType) }.also {
            setHourlyWeatherPool(it)
            hourlyWeatherAdapter = it
        }

        setHasFixedSize(true)

        addItemDecoration(marginDecoration)
        addOnItemTouchListener(horizontalScrollInterceptor)
    }

    private fun RecyclerView.setHourlyWeatherPool(adapter: HourlyWeatherAdapter) {
        citiesSharedModel.createHourWeatherPoolIfNeeded(requireContext(), adapter::createViewHolder)
        setPool(citiesSharedModel.hourWeatherRecyclerPool)
    }

    private fun initDailyWeather(
        marginDecoration: RecyclerView.ItemDecoration,
        horizontalScrollInterceptor: HorizontalScrollInterceptor,
    ) = binding.dailyWeather.apply {
        setLinearLayoutManager(LinearLayoutManager.HORIZONTAL, false)
        adapter = DailyWeatherAdapter { makeToast(it.weatherType) }.also {
            setDailyWeatherPool(it)
            dailyWeatherAdapter = it
        }

        setHasFixedSize(true)

        addItemDecoration(marginDecoration)
        addOnItemTouchListener(horizontalScrollInterceptor)
    }

    private fun makeToast(weatherType: ViewWeatherType) {
        Toast.makeText(requireContext(), weatherType.weatherDescriptionRes, Toast.LENGTH_SHORT).show()
    }

    private fun RecyclerView.setDailyWeatherPool(adapter: DailyWeatherAdapter) {
        citiesSharedModel.createDailyWeatherPoolIfNeeded(requireContext(), adapter::onCreateViewHolder)
        setPool(citiesSharedModel.dailyWeatherRecyclerPool)
    }

    @CallSuper
    protected open fun observeViewModel() = viewModel.apply {
        cityTitle.observe(viewLifecycleOwner, ::setCityTitle)

        currentDayWeather.observe(viewLifecycleOwner, ::updateCurrentDayWeather)
        currentHourWeather.observe(viewLifecycleOwner, ::updateCurrentHourWeather)
        hourlyWeather.observe(viewLifecycleOwner, ::updateHourlyWeather)
        dailyWeather.observe(viewLifecycleOwner, ::updateDailyWeather)

        error.observe(viewLifecycleOwner, ::toastTextMessage)
    }

    private fun setCityTitle(title: ViewCityTitle) {
        citiesSharedModel.setCityTitle(title)
    }

    private fun updateCurrentDayWeather(weather: ViewCurrentDayWeather) = binding.apply {
        temperatureRange.setText(weather.temperatureRange)
        apparentTemperatureRange.setText(weather.apparentTemperatureRange)
        precipitation.setText(weather.precipitationSum)
        maxWindSpeed.setText(weather.maxWindSpeed)

        sunTimes.setSunTimes(weather.sunrise, weather.sunset, weather.sunriseTime, weather.sunsetTime)
    }

    private fun updateCurrentHourWeather(weather: ViewHourWeather) = binding.apply {
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

        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        hourlyWeatherLayoutManager.scrollToPosition(hourWeatherList.indexOfFirst {
            currentHour == it.hour && currentDayOfMonth == it.dayOfMonth
        })
    }

    private fun updateDailyWeather(dailyWeatherList: List<ViewDayWeather>) {
        dailyWeatherAdapter.submitList(dailyWeatherList)
    }

    private fun toastTextMessage(message: TextMessage?) {
        message?.let {
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

    override fun onStop() {
        super.onStop()
        viewModel.clearErrors()
    }


    protected companion object {
        const val TAG = "Weather"

        private val calendar = Calendar.getInstance()
    }

}