package com.dropdrage.simpleweather.presentation.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseSimpleRecyclerListAdapter<VH : SimpleViewHolder<*, *>> : RecyclerView.Adapter<VH>() {

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        createViewHolder(LayoutInflater.from(parent.context), parent)

    abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH

}