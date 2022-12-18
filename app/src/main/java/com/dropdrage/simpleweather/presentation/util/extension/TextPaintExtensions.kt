package com.dropdrage.simpleweather.presentation.util.extension

import android.text.TextPaint
import kotlin.math.absoluteValue

fun TextPaint.calculateTextHeight() = fontMetrics.ascent.absoluteValue - fontMetrics.descent
