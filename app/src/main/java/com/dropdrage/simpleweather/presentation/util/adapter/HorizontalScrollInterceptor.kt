package com.dropdrage.simpleweather.presentation.util.adapter

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.simpleweather.presentation.util.extension.canScrollLeft
import com.dropdrage.simpleweather.presentation.util.extension.canScrollRight

/**
 * Disallows horizontal scrolling of [RecyclerView's][RecyclerView] parent if scroll is applied to RecyclerView.
 */
class HorizontalScrollInterceptor : RecyclerView.OnItemTouchListener {

    private var startX = 0f


    override fun onInterceptTouchEvent(recyclerView: RecyclerView, event: MotionEvent): Boolean =
        when (event.action) {
            MotionEvent.ACTION_DOWN -> startX = event.x
            MotionEvent.ACTION_MOVE -> {
                val isScrollingRight = event.x < startX
                val scrollItemsToRight = isScrollingRight && recyclerView.canScrollRight
                val scrollItemsToLeft = !isScrollingRight && recyclerView.canScrollLeft
                val disallowIntercept = scrollItemsToRight || scrollItemsToLeft
                recyclerView.parent.requestDisallowInterceptTouchEvent(disallowIntercept)
            }
            MotionEvent.ACTION_UP -> startX = 0f
            else -> Unit
        }.let { false }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

}
