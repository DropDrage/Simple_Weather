package com.dropdrage.simpleweather.data.util

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.dropdrage.simpleweather.data.weather.repository.CacheRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val TAG = "Worker_CacheClear"

@HiltWorker
internal class CacheClearWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cacheRepository: CacheRepository,
) : CoroutineWorker(applicationContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            cacheRepository.clearOutdated()
            if (!cacheRepository.hasCache()) {
                WorkManager.getInstance(applicationContext).cancelWorkById(id)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
            Result.failure()
        }
    }
}
