package com.dropdrage.simpleweather.city_list.presentation.presentation.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.simpleweather.city_list.presentation.presentation.utils.DragListener
import com.dropdrage.simpleweather.city_list.presentation.presentation.utils.ItemsMovable

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
        val from = viewHolder.bindingAdapterPosition
        val to = target.bindingAdapterPosition
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
