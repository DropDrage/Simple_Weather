package com.dropdrage.common.test.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class ViewModelScopeExtension : BeforeAllCallback, BeforeEachCallback, AfterEachCallback {

    private var error: Throwable? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun beforeAll(context: ExtensionContext?) {
        Thread.setDefaultUncaughtExceptionHandler { _, throwable -> error = throwable }

        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    override fun beforeEach(context: ExtensionContext?) {
        error = null
    }

    override fun afterEach(context: ExtensionContext?) {
        error?.let { throw it }
    }

}
