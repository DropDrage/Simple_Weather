package com.dropdrage.simpleweather.presentation.util.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class DifferRecyclerAdapter<T : Any, VH : SimpleViewHolder<T, *>> : RecyclerView.Adapter<VH>() {
    private val differCallBack = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem === newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }
    protected val differ = AsyncListDiffer(this, differCallBack)


    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(differ.currentList[position])
    }

    final override fun getItemCount(): Int = differ.currentList.size


    fun submitValues(values: List<T>) {
        differ.submitList(values)
    }
}