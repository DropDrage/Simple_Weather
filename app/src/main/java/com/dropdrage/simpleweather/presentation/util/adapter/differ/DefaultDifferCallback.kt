package com.dropdrage.simpleweather.presentation.util.adapter.differ

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class DefaultDifferCallback<T : SameEquatable<T>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem.isSame(newItem)

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}