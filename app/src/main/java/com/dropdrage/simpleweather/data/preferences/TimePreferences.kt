package com.dropdrage.simpleweather.data.preferences

import android.text.format.DateFormat
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumOrdinalPref

object TimePreferences : KotprefModel() {
    var timeFormat: TimeFormat by enumOrdinalPref(
        if (!DateFormat.is24HourFormat(context)) TimeFormat.H_24 else TimeFormat.H_12
    )
}