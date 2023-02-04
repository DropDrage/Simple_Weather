package com.dropdrage.simpleweather

import androidx.annotation.StringRes

internal interface Route {
    val route: String
}

internal enum class Screen(override val route: String, @get:StringRes val title: Int? = null) : Route {
    CITIES_WEATHER("cities_weather"),
    CITY_LIST("city_list", R.string.title_city_list),
    CITY_SEARCH("city_search"),
    SETTINGS("settings", R.string.title_settings);

    companion object {
        @StringRes
        fun getTitleResOfRoute(route: String?): Int? =
            if (route != null) values().find { it.route == route }!!.title
            else null
    }
}
