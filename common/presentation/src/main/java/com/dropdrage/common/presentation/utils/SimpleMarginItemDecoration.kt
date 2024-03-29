package com.dropdrage.common.presentation.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.util.extension.implicitAccess

class SimpleMarginItemDecoration(
    @Px private val topMargin: Int = 0,
    @Px private val bottomMargin: Int = 0,
    @Px private val leftMargin: Int = 0,
    @Px private val rightMargin: Int = 0,
) : RecyclerView.ItemDecoration() {

    constructor(@Px margin: Int) : this(margin, margin, margin, margin)

    constructor(@Px verticalMargin: Int = 0, @Px horizontalMargin: Int = 0) : this(
        verticalMargin,
        verticalMargin,
        horizontalMargin,
        horizontalMargin
    )


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.implicitAccess {
            top = topMargin
            bottom = bottomMargin
            left = leftMargin
            right = rightMargin
        }
    }

}
