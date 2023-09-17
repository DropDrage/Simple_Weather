package com.dropdrage.simpleweather

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.chibatching.kotpref.Kotpref
import com.dropdrage.simpleweather.data.util.CacheClearWorker
import dagger.hilt.android.HiltAndroidApp
import java.time.Duration
import javax.inject.Inject

@HiltAndroidApp
internal class WeatherApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)

        startClearCacheWorker()
    }

    private fun startClearCacheWorker() {
        val cacheClearWorker = PeriodicWorkRequestBuilder<CacheClearWorker>(CACHE_CLEAR_PERIOD)
            .setInitialDelay(CACHE_CLEAR_INITIAL_DELAY)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(CACHE_CLEAR_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, cacheClearWorker)
    }


    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()


    companion object {
        private const val CACHE_CLEAR_WORK_NAME = "work_cache_clear"
        private val CACHE_CLEAR_PERIOD = Duration.ofDays(1)
        private val CACHE_CLEAR_INITIAL_DELAY = CACHE_CLEAR_PERIOD
    }

}
