package com.dropdrage.simpleweather.domain.util

data class Range<T : Comparable<T>>(override val start: T, val end: T) : ClosedRange<T> {

    override val endInclusive: T = end

    override fun toString() = "$start->$end"
}
