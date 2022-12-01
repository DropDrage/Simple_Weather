package com.dropdrage.simpleweather.presentation.util.simple_adapter

import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding

typealias OnItemClickListener<T> = (T) -> Unit

abstract class ItemClickableRecyclerListAdapter<T, VH : ClickableViewHolder<T, *>> : SimpleRecyclerListAdapter<T, VH>()

abstract class ClickableViewHolder<T, VB : ViewBinding>(
    binding: VB,
    private val onClickListener: OnItemClickListener<T>,
) : BindableViewHolder<T, VB>(binding) {
    @CallSuper
    override fun bind(value: T) {
        super.bind(value)
        binding.root.setOnClickListener { onClickListener(value) }
    }
}