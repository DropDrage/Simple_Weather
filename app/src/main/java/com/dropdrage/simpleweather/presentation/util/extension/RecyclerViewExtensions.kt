package com.dropdrage.simpleweather.presentation.util.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val SCROLL_DIRECTION_RIGHT = 1
private const val SCROLL_DIRECTION_LEFT = -1

val RecyclerView.canScrollRight: Boolean
    get() = canScrollHorizontally(SCROLL_DIRECTION_RIGHT)

val RecyclerView.canScrollLeft: Boolean
    get() = canScrollHorizontally(SCROLL_DIRECTION_LEFT)


fun RecyclerView.setLinearLayoutManager() {
    layoutManager = LinearLayoutManager(context)
}

fun RecyclerView.setLinearLayoutManager(@RecyclerView.Orientation orientation: Int, reverseLayout: Boolean = false) {
    layoutManager = LinearLayoutManager(context, orientation, reverseLayout)
}
