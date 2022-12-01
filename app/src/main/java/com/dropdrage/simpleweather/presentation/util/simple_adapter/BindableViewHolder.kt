package com.dropdrage.simpleweather.presentation.util.simple_adapter

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BindableViewHolder<T, VB : ViewBinding>(protected val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {
    @CallSuper
    open fun bind(value: T) {
        displayData(value)
    }

    abstract fun displayData(value: T)
}