package com.dropdrage.common.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

@Composable
fun <T> Flow<T>.collectInLaunchedEffect(collector: FlowCollector<T>) {
    LaunchedEffect(key1 = Unit) {
        collect(collector)
    }
}
