package com.dropdrage.simpleweather.presentation.util.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

abstract class DifferRecyclerAdapter<T : Any, VH : SimpleViewHolder<T, *>>(
    differCallBack: DiffUtil.ItemCallback<T> = DefaultDifferCallback(),
) : BaseSimpleRecyclerListAdapter<VH>() {

    protected val differ = AsyncListDiffer(this, differCallBack)


    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(differ.currentList[position])
    }

    final override fun getItemCount(): Int = differ.currentList.size


    fun submitValues(values: List<T>) {
        differ.submitList(values)
    }
}