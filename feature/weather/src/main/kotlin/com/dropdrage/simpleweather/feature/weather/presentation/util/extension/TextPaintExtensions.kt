package com.dropdrage.simpleweather.feature.weather.presentation.util.extension

import android.text.TextPaint
import kotlin.math.absoluteValue

internal fun TextPaint.calculateTextHeight() = fontMetrics.ascent.absoluteValue - fontMetrics.descent
