package com.dropdrage.simpleweather.data.repository

import android.util.Log
import com.dropdrage.simpleweather.domain.util.Resource

abstract class SimpleRepository<T>(protected val tag: String) {
    protected inline fun simplyResourceWrap(tryGetData: () -> T): Resource<T> = try {
        Resource.Success(tryGetData())
    } catch (e: Exception) {
        Log.e(tag, e.message, e)
        Resource.Error(e.message, e)
    }
}