package com.dropdrage.adapters.differ

interface SameEquatable<T> {
    fun isSame(other: T): Boolean
}