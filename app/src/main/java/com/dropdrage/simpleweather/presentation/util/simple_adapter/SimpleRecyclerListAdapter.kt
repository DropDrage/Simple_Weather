package com.dropdrage.simpleweather.presentation.util.simple_adapter

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView.Adapter
import kotlin.properties.Delegates

abstract class SimpleRecyclerListAdapter<T, VH : BindableViewHolder<T, *>> : Adapter<VH>() {
    var values: List<T> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }


    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(values[position])
    }

    final override fun getItemCount(): Int = values.size
}