package com.dropdrage.simpleweather.presentation.ui.settings.change_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.simpleweather.databinding.ItemSettingChangeBinding
import com.dropdrage.simpleweather.presentation.model.AnySetting
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener
import com.dropdrage.simpleweather.presentation.util.adapter.simple.SimpleRecyclerListAdapter
import kotlin.properties.Delegates

class SettingChangeAdapter(private val onSettingClickListener: OnItemClickListener<AnySetting>) :
    SimpleRecyclerListAdapter<AnySetting, SettingChangeViewHolder>() {

    var selectedSetting: AnySetting? by Delegates.observable(null) { _, _, _ ->
        values = values.toList()
    }


    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = SettingChangeViewHolder(
        ItemSettingChangeBinding.inflate(inflater, parent, false),
        onSettingClickListener,
    )

    override fun onBindViewHolder(holder: SettingChangeViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.isSelected = selectedSetting == values[position]
    }
}