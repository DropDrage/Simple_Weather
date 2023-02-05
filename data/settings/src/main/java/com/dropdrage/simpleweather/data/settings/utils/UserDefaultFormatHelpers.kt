package com.dropdrage.simpleweather.settings.data.util

import android.content.Context
import android.icu.util.LocaleData
import android.icu.util.ULocale
import android.os.Build
import android.text.format.DateFormat
import java.util.*

internal fun isLocaleMetric() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        LocaleData.getMeasurementSystem(ULocale.getDefault()) == LocaleData.MeasurementSystem.SI
    else Locale.getDefault().isLocaleMetric()

private fun Locale.isLocaleMetric(): Boolean = when (country.uppercase(this)) {
    "US", "LR", "MM" -> false
    else -> true
}

private const val DAY_FORMAT_CHAR = 'd'
private const val MONTH_FORMAT_CHAR = 'M'

internal fun isDateFormatStraight(context: Context): Boolean {
    val formatOrder = DateFormat.getDateFormatOrder(context)
    return formatOrder.indexOf(DAY_FORMAT_CHAR) < formatOrder.indexOf(MONTH_FORMAT_CHAR)
}
