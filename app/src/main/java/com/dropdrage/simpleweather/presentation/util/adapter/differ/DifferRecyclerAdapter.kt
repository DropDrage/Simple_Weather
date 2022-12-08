package com.dropdrage.simpleweather.presentation.util.adapter.differ

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.dropdrage.simpleweather.presentation.util.adapter.BaseSimpleRecyclerListAdapter
import com.dropdrage.simpleweather.presentation.util.adapter.simple.SimpleViewHolder

abstract class DifferRecyclerAdapter<T : SameEquatable<T>, VH : SimpleViewHolder<T, *>>(
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