package com.dropdrage.simpleweather.feature.settings.test.presentation.util

import android.content.Context
import com.dropdrage.common.test.util.justArgToString
import com.dropdrage.common.test.util.verifyOnce
import com.dropdrage.common.test.util.verifyTwice
import com.dropdrage.simpleweather.data.settings.DateFormat
import com.dropdrage.simpleweather.data.settings.GeneralFormat
import com.dropdrage.simpleweather.data.settings.TimeFormat
import com.dropdrage.simpleweather.feature.settings.presentation.model.AnySetting
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewDateFormat
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewSetting
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewTimeFormat
import com.dropdrage.simpleweather.feature.settings.presentation.util.GeneralFormatConverter
import com.dropdrage.simpleweather.feature.settings.util.matchEnumsAsArguments
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
internal class GeneralFormatConverterTest {

    @MockK
    lateinit var context: Context

    private lateinit var converter: GeneralFormatConverter


    @BeforeEach
    fun setUp() {
        converter = GeneralFormatConverter(context)
    }


    @ParameterizedTest
    @MethodSource("provideGeneralFormatsWithView")
    fun `convertToSetting success`(format: GeneralFormat, expectedSetting: AnySetting) {
        val setting = converter.convertToSetting(format)

        assertEquals(expectedSetting, setting)
    }

    @ParameterizedTest
    @MethodSource("provideGeneralFormatsWithView")
    fun `convertToViewSetting success`(format: GeneralFormat, expectedSetting: AnySetting) {
        justArgToString { context.getString(any()) }
        val expectedViewSetting = ViewSetting(
            label = expectedSetting.labelResId.toString(),
            currentValue = expectedSetting.unitResId.toString(),
            values = expectedSetting.values,
        )

        val viewSetting = converter.convertToViewSetting(format)

        verifyTwice { context.getString(any()) }
        assertEquals(expectedViewSetting, viewSetting)
    }

    @ParameterizedTest
    @MethodSource("provideGeneralFormats")
    fun `convertToValue success`(format: AnySetting) {
        justArgToString { context.getString(any()) }

        val value = converter.convertToValue(format)

        verifyOnce { context.getString(any()) }
        assertEquals(format.unitResId.toString(), value)
    }


    private companion object {

        @JvmStatic
        fun provideGeneralFormatsWithView() = Stream.of(
            *matchEnumsAsArguments(TimeFormat.values(), ViewTimeFormat.values()),
            *matchEnumsAsArguments(DateFormat.values(), ViewDateFormat.values()),
        )

        @JvmStatic
        fun provideGeneralFormats() = Stream.of(
            *ViewTimeFormat.values(),
            *ViewDateFormat.values(),
        )

    }

}
