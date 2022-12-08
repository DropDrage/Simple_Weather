package com.dropdrage.simpleweather.presentation.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.simpleweather.databinding.ItemSettingBinding
import com.dropdrage.simpleweather.presentation.model.ViewSetting
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener
import com.dropdrage.simpleweather.presentation.util.adapter.SimpleRecyclerListAdapter

class SettingsAdapter(private val openSettingChangeDialog: OnItemClickListener<ViewSetting>) :
    SimpleRecyclerListAdapter<ViewSetting, SettingsViewHolder>() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = SettingsViewHolder(
        ItemSettingBinding.inflate(inflater, parent, false),
        openSettingChangeDialog,
    )
}