package com.dropdrage.simpleweather.data.source.local.util

sealed class LocalResource<T> {
    class Success<T>(val data: T) : LocalResource<T>()
    class NotFound<T> : LocalResource<T>()
}
