package com.dropdrage.simpleweather.common.data

sealed class LocalResource<T> {
    class Success<T>(val data: T) : LocalResource<T>()
    class NotFound<T> : LocalResource<T>()
}
