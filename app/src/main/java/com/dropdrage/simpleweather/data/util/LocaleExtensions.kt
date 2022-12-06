package com.dropdrage.simpleweather.data.util

import android.icu.util.LocaleData
import android.icu.util.ULocale
import android.os.Build
import java.util.*

fun isLocaleMetric() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        LocaleData.getMeasurementSystem(ULocale.getDefault()) == LocaleData.MeasurementSystem.SI
    else Locale.getDefault().isLocaleMetric()

private fun Locale.isLocaleMetric(): Boolean = when (country.uppercase(this)) {
    "US", "LR", "MM" -> false
    else -> true
}
