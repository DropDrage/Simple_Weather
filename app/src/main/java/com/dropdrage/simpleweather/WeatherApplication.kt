package com.dropdrage.simpleweather

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        Kotpref.init(this)
    }
}