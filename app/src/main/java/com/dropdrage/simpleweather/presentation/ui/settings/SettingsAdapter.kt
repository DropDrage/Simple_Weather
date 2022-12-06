package com.dropdrage.simpleweather.presentation.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.simpleweather.databinding.ItemSettingBinding
import com.dropdrage.simpleweather.presentation.model.ViewSetting
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener
import com.dropdrage.simpleweather.presentation.util.adapter.SimpleRecyclerListAdapter

class SettingsAdapter(private val openSettingChangeDialog: OnItemClickListener<ViewSetting>) :
    SimpleRecyclerListAdapter<ViewSetting, SettingsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SettingsViewHolder(
        ItemSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        openSettingChangeDialog,
    )
}