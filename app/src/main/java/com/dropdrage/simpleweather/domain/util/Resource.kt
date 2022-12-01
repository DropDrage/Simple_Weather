package com.dropdrage.simpleweather.domain.util

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val message: String?, val exception: Exception) : Resource<T>()
}
