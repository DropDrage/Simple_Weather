package com.dropdrage.simpleweather.data.settings.util

import android.content.Context
import android.icu.util.LocaleData
import android.icu.util.ULocale
import android.os.Build
import android.text.format.DateFormat
import com.dropdrage.common.build_config_checks.isSdkVersionGreaterOrEquals
import java.util.Locale

internal fun isLocaleMetric() =
    if (isSdkVersionGreaterOrEquals(Build.VERSION_CODES.P))
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
