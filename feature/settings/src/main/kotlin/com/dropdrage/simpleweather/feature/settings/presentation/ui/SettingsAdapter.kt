package com.dropdrage.simpleweather.feature.settings.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.adapters.simple.SimpleRecyclerAdapter
import com.dropdrage.simpleweather.feature.settings.databinding.ItemSettingBinding
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewSetting

internal class SettingsAdapter(
    settings: List<ViewSetting>,
    private val openSettingChangeDialog: OnItemClickListener<ViewSetting>,
) : SimpleRecyclerAdapter<ViewSetting, SettingsViewHolder>() {

    init {
        values = settings
    }


    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = SettingsViewHolder(
        ItemSettingBinding.inflate(inflater, parent, false),
        openSettingChangeDialog,
    )


    fun changeSetting(setting: ViewSetting) {
        notifyItemChanged(values.indexOf(setting))
    }

}
