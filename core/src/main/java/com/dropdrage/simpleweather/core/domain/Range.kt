package com.dropdrage.simpleweather.core.domain

data class Range<T : Comparable<T>>(override val start: T, val end: T) : ClosedRange<T> {

    override val endInclusive: T = end

    override fun toString() = "$start->$end"

    inline fun <N : Comparable<N>> map(transform: (T) -> N) = Range(transform(start), transform(end))

}
