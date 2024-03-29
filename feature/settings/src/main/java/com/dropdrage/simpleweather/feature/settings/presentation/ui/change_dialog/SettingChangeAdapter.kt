package com.dropdrage.simpleweather.feature.settings.presentation.ui.change_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.adapters.simple.SimpleRecyclerAdapter
import com.dropdrage.simpleweather.feature.settings.databinding.ItemSettingChangeBinding
import com.dropdrage.simpleweather.feature.settings.presentation.model.AnySetting
import kotlin.properties.Delegates

internal class SettingChangeAdapter(private val onSettingClickListener: OnItemClickListener<AnySetting>) :
    SimpleRecyclerAdapter<AnySetting, SettingChangeViewHolder>() {

    var selectedSetting: AnySetting? by Delegates.observable(null) { _, _, _ ->
        notifyDataSetChanged()
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
