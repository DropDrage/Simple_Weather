package com.dropdrage.common.presentation.util.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool

private const val SCROLL_DIRECTION_RIGHT = 1
private const val SCROLL_DIRECTION_LEFT = -1

val RecyclerView.canScrollRight: Boolean
    get() = canScrollHorizontally(SCROLL_DIRECTION_RIGHT)

val RecyclerView.canScrollLeft: Boolean
    get() = canScrollHorizontally(SCROLL_DIRECTION_LEFT)


fun RecyclerView.setLinearLayoutManager() {
    layoutManager = LinearLayoutManager(context)
}

fun RecyclerView.setLinearLayoutManager(
    @RecyclerView.Orientation orientation: Int,
    reverseLayout: Boolean = false,
): LinearLayoutManager = LinearLayoutManager(context, orientation, reverseLayout).also { layoutManager = it }


fun RecyclerView.setPool(pool: RecycledViewPool) {
    setRecycledViewPool(pool)
    (layoutManager as? LinearLayoutManager)?.recycleChildrenOnDetach = true
}

fun RecycledViewPool.bindToLifecycle(lifecycleOwner: LifecycleOwner, onDestroy: () -> Unit = {}) {
    lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            this@bindToLifecycle.clear()
            onDestroy()
        }
    })
}
