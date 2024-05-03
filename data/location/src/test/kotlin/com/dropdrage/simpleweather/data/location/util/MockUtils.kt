package com.dropdrage.simpleweather.data.location.util

import android.location.Location
import io.mockk.every
import io.mockk.mockk

fun mockLocation(latitude: Double, longitude: Double) = mockk<Location> {
    every { this@mockk.latitude } returns latitude
    every { this@mockk.longitude } returns longitude
}
