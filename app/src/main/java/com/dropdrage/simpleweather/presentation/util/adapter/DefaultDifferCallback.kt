package com.dropdrage.simpleweather.presentation.util.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class DefaultDifferCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem === newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}