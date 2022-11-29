package com.dropdrage.simpleweather

import android.app.Application
import com.chibatching.kotpref.Kotpref
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)
    }
}