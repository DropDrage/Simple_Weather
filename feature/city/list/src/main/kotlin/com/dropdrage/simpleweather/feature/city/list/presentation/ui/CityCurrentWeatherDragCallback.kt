package com.dropdrage.simpleweather.feature.city.list.presentation.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.simpleweather.feature.city.list.presentation.utils.DragListener
import com.dropdrage.simpleweather.feature.city.list.presentation.utils.ItemsMovable

private const val NO_SWIPE_DIRECTION = 0

internal class CityCurrentWeatherDragCallback(private val onDragEnd: () -> Unit) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
    NO_SWIPE_DIRECTION
) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        val adapter = recyclerView.adapter as ItemsMovable
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        adapter.moveItem(from, to)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (viewHolder is DragListener) {
                viewHolder.onDragStart()
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder is DragListener) {
            viewHolder.onDragEnd()
        }
        onDragEnd()
    }

    override fun isLongPressDragEnabled(): Boolean = false

}
