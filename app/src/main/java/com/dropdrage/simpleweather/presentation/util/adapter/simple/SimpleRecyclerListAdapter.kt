package com.dropdrage.simpleweather.presentation.util.adapter.simple

import androidx.annotation.CallSuper
import com.dropdrage.simpleweather.presentation.util.adapter.BaseSimpleRecyclerListAdapter
import kotlin.properties.Delegates

abstract class SimpleRecyclerListAdapter<T, VH : SimpleViewHolder<T, *>> : BaseSimpleRecyclerListAdapter<VH>() {

    var values: List<T> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }


    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(values[position])
    }

    final override fun getItemCount(): Int = values.size
}