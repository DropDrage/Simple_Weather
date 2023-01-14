package com.dropdrage.simpleweather.presentation.util.extension

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@MainThread
fun <VM : ViewModel> Fragment.viewModels(
    viewModelClass: KClass<VM>,
    ownerProducer: () -> ViewModelStoreOwner = { this },
    extrasProducer: (() -> CreationExtras)? = null,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> {
    val owner by lazy(LazyThreadSafetyMode.NONE) { ownerProducer() }
    return createViewModelLazy(
        viewModelClass,
        { owner.viewModelStore },
        {
            extrasProducer?.invoke()
                ?: (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras
                ?: CreationExtras.Empty
        },
        factoryProducer ?: {
            (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
                ?: defaultViewModelProviderFactory
        })
}


fun Fragment.focusEditText(target: EditText) {
    target.requestFocus()
    val inputMethodManager = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
    inputMethodManager?.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
}


val Fragment.viewLifecycleScope: LifecycleCoroutineScope
    get() = viewLifecycleOwner.lifecycleScope

val Fragment.viewLifecycle: Lifecycle
    get() = viewLifecycleOwner.lifecycle

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

