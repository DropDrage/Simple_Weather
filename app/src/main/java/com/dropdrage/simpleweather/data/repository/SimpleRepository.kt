package com.dropdrage.simpleweather.data.repository

import android.util.Log
import com.dropdrage.simpleweather.core.domain.Resource
import kotlinx.coroutines.CancellationException

abstract class SimpleRepository<T>(protected val tag: String) {
    protected inline fun simplyResourceWrap(tryGetData: () -> T): Resource<T> = try {
        Resource.Success(tryGetData())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.e(tag, e.message, e)
        Resource.Error(e)
    }
}