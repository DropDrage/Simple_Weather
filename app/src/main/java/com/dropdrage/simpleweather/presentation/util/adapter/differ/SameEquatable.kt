package com.dropdrage.simpleweather.presentation.util.adapter.differ

interface SameEquatable<T> {
    fun isSame(other: T): Boolean
}