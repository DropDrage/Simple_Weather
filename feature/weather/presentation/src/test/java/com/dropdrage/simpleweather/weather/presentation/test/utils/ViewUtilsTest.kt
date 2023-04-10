package com.dropdrage.simpleweather.weather.presentation.test.utils

import com.dropdrage.simpleweather.weather.presentation.util.ViewUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class ViewUtilsTest {

    @ParameterizedTest(name = "{0}x{1} to {2} = {3}x{4}")
    @MethodSource("provideResizes")
    fun `resizeToTargetSize success`(
        iconIntrinsicWidth: Int,
        iconIntrinsicHeight: Int,
        iconSize: Int,
        expectedWidth: Int,
        expectedHeight: Int,
    ) {
        val targetSize = ViewUtils.resizeToTargetSize(iconIntrinsicWidth, iconIntrinsicHeight, iconSize)

        assertEquals(expectedWidth, targetSize.first)
        assertEquals(expectedHeight, targetSize.second)
    }


    private companion object {
        @JvmStatic
        fun provideResizes() = Stream.of(
            Arguments.of(48, 48, 24, 24, 24),
            Arguments.of(48, 36, 24, 24, 18),
            Arguments.of(36, 48, 24, 18, 24),
        )
    }

}
