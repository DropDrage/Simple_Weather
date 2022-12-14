package com.dropdrage.simpleweather.presentation.util.adapter.differ

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dropdrage.simpleweather.presentation.util.adapter.simple.SimpleViewHolder

abstract class DifferRecyclerAdapter<T : SameEquatable<T>, VH : SimpleViewHolder<T, *>>(
    differCallBack: DiffUtil.ItemCallback<T> = DefaultDifferCallback(),
) : ListAdapter<T, VH>(differCallBack) {

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        createViewHolder(LayoutInflater.from(parent.context), parent)

    abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH


    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(currentList[position])
    }

}
