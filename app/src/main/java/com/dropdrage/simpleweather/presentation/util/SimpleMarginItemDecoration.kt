package com.wholedetail.changemysleep.presentation.ui.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class SimpleMarginItemDecoration(
    @Px private val topMargin: Int = 0,
    @Px private val bottomMargin: Int = 0,
    @Px private val leftMargin: Int = 0,
    @Px private val rightMargin: Int = 0
) : RecyclerView.ItemDecoration() {

    constructor(@Px margin: Int) : this(margin, margin, margin, margin)

    constructor(@Px verticalMargin: Int, @Px horizontalMargin: Int) : this(
        verticalMargin,
        verticalMargin,
        horizontalMargin,
        horizontalMargin
    )


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.apply {
            top = topMargin
            bottom = bottomMargin
            left = leftMargin
            right = rightMargin
        }
    }
}