package com.dropdrage.simpleweather.presentation.util.adapter

import androidx.viewbinding.ViewBinding

typealias OnItemClickListener<T> = (T) -> Unit

abstract class ClickableViewHolder<T, VB : ViewBinding>(
    binding: VB,
    private val onClickListener: OnItemClickListener<T>,
) : SimpleViewHolder<T, VB>(binding) {
    override fun bind(value: T) {
        super.bind(value)
        binding.root.setOnClickListener { onClickListener(value) }
    }
}