package com.dropdrage.simpleweather.presentation.util.adapter.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

abstract class SimpleRecyclerListAdapter<T, VH : SimpleViewHolder<T, *>> : RecyclerView.Adapter<VH>() {

    var values: List<T> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }


    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        createViewHolder(LayoutInflater.from(parent.context), parent)

    abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH

    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(values[position])
    }

    final override fun getItemCount(): Int = values.size
}