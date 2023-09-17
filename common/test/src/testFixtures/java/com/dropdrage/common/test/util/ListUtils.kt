package com.dropdrage.common.test.util

inline fun <T> createList(count: Int, producer: () -> T): List<T> = buildList(count) {
    repeat(count) { add(producer()) }
}

inline fun <T> createListIndexed(count: Int, producer: (Int) -> T): List<T> = buildList(count) {
    repeat(count) { add(producer(it)) }
}
