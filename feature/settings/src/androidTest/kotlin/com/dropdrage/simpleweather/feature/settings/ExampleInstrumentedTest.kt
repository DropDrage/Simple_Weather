package com.dropdrage.simpleweather.feature.settings

import androidx.test.platform.app.InstrumentationRegistry
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert
import org.junit.jupiter.api.Test

class ExampleInstrumentedTest : TestCase() {
    @Test
    fun useAppContext() = run {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.dropdrage.simpleweather.feature.settings.test", appContext.packageName)
    }
}
