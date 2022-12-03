package com.dropdrage.simpleweather.presentation.ui.city.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.simpleweather.presentation.util.adapter.ItemsMovable

private const val NO_SWIPE_DIRECTION = 0

private const val ON_DRAG_ITEM_ALPHA = 0.5f
private const val ON_DRAG_END_ITEM_ALPHA = 1f

class ItemDragCallback(private val onDragEnd: () -> Unit) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
    NO_SWIPE_DIRECTION) {

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
            viewHolder?.itemView?.alpha = ON_DRAG_ITEM_ALPHA
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        viewHolder.itemView.alpha = ON_DRAG_END_ITEM_ALPHA
        onDragEnd()
    }

}