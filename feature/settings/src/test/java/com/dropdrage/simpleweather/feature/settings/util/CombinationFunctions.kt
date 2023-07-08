package com.dropdrage.simpleweather.feature.settings.util

import org.junit.jupiter.params.provider.Arguments

internal fun matchEnumsAsArguments(enums: Array<*>, matchedEnums: Array<*>): Array<Arguments> =
    enums.zip(matchedEnums).map { (first, second) -> Arguments.of(first, second) }.toTypedArray()
