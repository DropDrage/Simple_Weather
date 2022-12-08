package com.dropdrage.simpleweather.presentation.util.adapter.simple

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class SimpleViewHolder<T, VB : ViewBinding>(protected val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {
    @CallSuper
    open fun bind(value: T) {
        bindData(value)
    }

    abstract fun bindData(value: T)
}