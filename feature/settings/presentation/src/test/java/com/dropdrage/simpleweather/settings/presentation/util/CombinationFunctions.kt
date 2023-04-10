package com.dropdrage.simpleweather.settings.presentation.util

import org.junit.jupiter.params.provider.Arguments

internal fun matchEnumsAsArguments(enums: Array<*>, matchedEnums: Array<*>): Array<Arguments> =
    enums.zip(matchedEnums).map { enumsPair -> Arguments.of(enumsPair.first, enumsPair.second) }.toTypedArray()
