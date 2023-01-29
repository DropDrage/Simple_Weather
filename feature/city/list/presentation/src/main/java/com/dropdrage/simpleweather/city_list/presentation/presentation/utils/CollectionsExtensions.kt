package com.dropdrage.simpleweather.city_list.presentation.presentation.utils

import java.util.*

fun <T> MutableList<T>.swap(from: Int, to: Int): MutableList<T> {
    Collections.swap(this, from, to)
    return this
}

inline fun <T> Iterable<T>.findIndexedOrNull(predicate: (Int, T) -> Boolean): T? {
    forEachIndexed { i, item -> if (predicate(i, item)) return item }
    return null
}
