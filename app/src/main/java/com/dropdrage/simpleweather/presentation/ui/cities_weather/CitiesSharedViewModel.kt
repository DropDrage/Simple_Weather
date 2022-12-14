package com.dropdrage.simpleweather.presentation.ui.cities_weather

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.simpleweather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.presentation.util.adapter.pool.PrefetchPlainViewPool
import com.dropdrage.simpleweather.presentation.util.adapter.pool.ViewHolderProducer

private const val HOURLY_WEATHER_POOL_SIZE_PER_FRAGMENT = 8
private const val HOURLY_WEATHER_POOL_INITIAL_SIZE = HOURLY_WEATHER_POOL_SIZE_PER_FRAGMENT * 3 + 2
private const val HOURLY_WEATHER_POOL_MAX_SIZE = HOURLY_WEATHER_POOL_SIZE_PER_FRAGMENT * 4

private const val DAILY_WEATHER_POOL_SIZE_PER_FRAGMENT = 7
private const val DAILY_WEATHER_POOL_INITIAL_SIZE = DAILY_WEATHER_POOL_SIZE_PER_FRAGMENT * 3
private const val DAILY_WEATHER_POOL_MAX_SIZE = DAILY_WEATHER_POOL_SIZE_PER_FRAGMENT * 4

interface ObservableCityTitle {
    val currentCityTitle: LiveData<ViewCityTitle>
}

class CitiesSharedViewModel : ViewModel(), ObservableCityTitle {

    private val _currentCityTitle = MutableLiveData<ViewCityTitle>()
    override val currentCityTitle: LiveData<ViewCityTitle> = _currentCityTitle

    lateinit var hourWeatherRecyclerPool: RecyclerView.RecycledViewPool
        private set
    lateinit var dailyWeatherRecyclerPool: RecyclerView.RecycledViewPool
        private set

    val isHourWeatherPoolInitialized
        get() = ::hourWeatherRecyclerPool.isInitialized

    val isDailyWeatherPoolInitialized
        get() = ::dailyWeatherRecyclerPool.isInitialized


    fun createHourWeatherPoolIfNeeded(context: Context, hourWeatherHolderProducer: ViewHolderProducer) {
        if (isHourWeatherPoolInitialized) return
        createHourWeatherPool(context, hourWeatherHolderProducer)
    }

    private fun createHourWeatherPool(context: Context, hourWeatherHolderProducer: ViewHolderProducer) {
        hourWeatherRecyclerPool = PrefetchPlainViewPool.createPrefetchedWithCoroutineSupplier(
            context,
            hourWeatherHolderProducer,
            HOURLY_WEATHER_POOL_MAX_SIZE,
            HOURLY_WEATHER_POOL_INITIAL_SIZE
        )
    }

    fun createDailyWeatherPoolIfNeeded(context: Context, dailyWeatherHolderProducer: ViewHolderProducer) {
        if (isDailyWeatherPoolInitialized) return
        createDailyWeatherPool(context, dailyWeatherHolderProducer)
    }

    private fun createDailyWeatherPool(context: Context, dailyWeatherHolderProducer: ViewHolderProducer) {
        dailyWeatherRecyclerPool = PrefetchPlainViewPool.createPrefetchedWithCoroutineSupplier(
            context,
            dailyWeatherHolderProducer,
            DAILY_WEATHER_POOL_MAX_SIZE,
            DAILY_WEATHER_POOL_INITIAL_SIZE
        )
    }


    fun setCityTitle(city: ViewCityTitle) {
        _currentCityTitle.value = city
    }

}