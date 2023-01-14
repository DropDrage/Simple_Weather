package com.dropdrage.simpleweather

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.chibatching.kotpref.Kotpref
import com.dropdrage.simpleweather.data.util.CacheClearWorker
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import java.time.Duration
import javax.inject.Inject

private const val CACHE_CLEAR_WORK_NAME = "work_cache_clear"
private val CACHE_CLEAR_PERIOD = Duration.ofDays(1)
private val CACHE_CLEAR_INITIAL_DELAY = CACHE_CLEAR_PERIOD

@HiltAndroidApp
class WeatherApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        Kotpref.init(this)

        startClearCacheWorker()
    }


    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    fun startClearCacheWorker() {
        val cacheClearWorker = PeriodicWorkRequestBuilder<CacheClearWorker>(CACHE_CLEAR_PERIOD)
            .setInitialDelay(CACHE_CLEAR_INITIAL_DELAY)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(CACHE_CLEAR_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, cacheClearWorker)
    }

}
