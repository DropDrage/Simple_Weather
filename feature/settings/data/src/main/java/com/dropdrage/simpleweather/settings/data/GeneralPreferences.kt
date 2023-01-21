package com.dropdrage.simpleweather.data.settings

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumOrdinalPref
import com.dropdrage.simpleweather.settings.data.util.isDateFormatStraight

private typealias AndroidDateFormat = android.text.format.DateFormat

object GeneralPreferences : KotprefModel() {

    var timeFormat: TimeFormat by enumOrdinalPref(
        if (AndroidDateFormat.is24HourFormat(context)) TimeFormat.H_24 else TimeFormat.H_12
    )

    var dateFormat: DateFormat by enumOrdinalPref(
        if (isDateFormatStraight(context)) DateFormat.STRAIGHT else DateFormat.REVERSED
    )

}
