package com.dropdrage.common.test.util

import app.cash.turbine.TurbineContext
import app.cash.turbine.turbineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest

inline fun runTurbineTest(
    crossinline block: suspend TurbineContext.(backgroundScope: CoroutineScope) -> Unit,
) = runTest { turbineScope { block(backgroundScope) } }
