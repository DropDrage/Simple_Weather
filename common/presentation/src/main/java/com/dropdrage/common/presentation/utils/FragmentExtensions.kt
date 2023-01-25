package com.dropdrage.common.presentation.utils

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun Fragment.focusEditText(target: EditText) {
    target.requestFocus()
    val inputMethodManager = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
    inputMethodManager?.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
}


val Fragment.viewLifecycleScope: LifecycleCoroutineScope
    get() = viewLifecycleOwner.lifecycleScope

val Fragment.viewLifecycle: Lifecycle
    get() = viewLifecycleOwner.lifecycle

@OptIn(InternalCoroutinesApi::class)
fun <T> collectWithLifecycle(
    flow: Flow<T>,
    collector: FlowCollector<T>,
    scope: CoroutineScope,
    flowLifecycle: Lifecycle,
) {
    scope.launch { flow.flowWithLifecycle(flowLifecycle).collect(collector) }
}

fun <T> Fragment.collectWithViewLifecycle(flow: Flow<T>, collector: FlowCollector<T>) {
    collectWithLifecycle(flow, collector, viewLifecycleScope, viewLifecycle)
}
