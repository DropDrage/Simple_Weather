package com.dropdrage.simpleweather.presentation.ui.cities_weather

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dropdrage.adapters.pool.PrefetchPlainViewPool
import com.dropdrage.adapters.pool.ViewHolderProducer
import com.dropdrage.simpleweather.presentation.model.ViewCityTitle

private const val HOURLY_WEATHER_POOL_SIZE_PER_FRAGMENT = 8
private const val HOURLY_WEATHER_POOL_INITIAL_SIZE: Int = (HOURLY_WEATHER_POOL_SIZE_PER_FRAGMENT * 2.5f).toInt()
private const val HOURLY_WEATHER_POOL_MAX_SIZE: Int = HOURLY_WEATHER_POOL_SIZE_PER_FRAGMENT * 3

private const val DAILY_WEATHER_POOL_SIZE_PER_FRAGMENT = 7
private const val DAILY_WEATHER_POOL_INITIAL_SIZE: Int = DAILY_WEATHER_POOL_SIZE_PER_FRAGMENT * 2
private const val DAILY_WEATHER_POOL_MAX_SIZE: Int = DAILY_WEATHER_POOL_SIZE_PER_FRAGMENT * 2

interface ObservableCityTitle {
    val currentCityTitle: LiveData<ViewCityTitle>
}

class CitiesSharedViewModel : ViewModel(), ObservableCityTitle {

    private val _currentCityTitle = MutableLiveData<ViewCityTitle>()
    override val currentCityTitle: LiveData<ViewCityTitle> = _currentCityTitle

    lateinit var hourlyWeatherRecyclerPool: PrefetchPlainViewPool
        private set
    val isHourlyWeatherPoolInitialized
        get() = ::hourlyWeatherRecyclerPool.isInitialized

    lateinit var dailyWeatherRecyclerPool: PrefetchPlainViewPool
        private set
    val isDailyWeatherPoolInitialized
        get() = ::dailyWeatherRecyclerPool.isInitialized


    fun createHourWeatherPoolIfNeeded(context: Context, hourWeatherHolderProducer: ViewHolderProducer) {
        if (isHourlyWeatherPoolInitialized) return
        createHourWeatherPool(context, hourWeatherHolderProducer)
    }

    private fun createHourWeatherPool(context: Context, hourWeatherHolderProducer: ViewHolderProducer) {
        hourlyWeatherRecyclerPool = PrefetchPlainViewPool.createPrefetchedWithCoroutineSupplier(
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


    override fun onCleared() {
        super.onCleared()
        hourlyWeatherRecyclerPool.clear()
        dailyWeatherRecyclerPool.clear()
    }

}
