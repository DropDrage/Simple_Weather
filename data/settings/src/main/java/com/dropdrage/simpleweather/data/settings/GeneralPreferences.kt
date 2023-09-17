package com.dropdrage.simpleweather.data.settings

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumOrdinalPref
import com.dropdrage.simpleweather.data.settings.util.isDateFormatStraight
import android.text.format.DateFormat as AndroidDateFormat

object GeneralPreferences : KotprefModel() {

    var timeFormat: TimeFormat by enumOrdinalPref(
        if (AndroidDateFormat.is24HourFormat(context)) TimeFormat.H_24 else TimeFormat.H_12
    )

    var dateFormat: DateFormat by enumOrdinalPref(
        if (isDateFormatStraight(context)) DateFormat.STRAIGHT else DateFormat.REVERSED
    )

}
