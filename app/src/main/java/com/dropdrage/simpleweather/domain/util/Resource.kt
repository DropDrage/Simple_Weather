package com.dropdrage.simpleweather.domain.util

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    open class Error<T>(val exception: Exception, val message: String? = exception.message) : Resource<T>()
    class CantObtainResource<T> : Error<T>(CantObtainResourceException())
}
