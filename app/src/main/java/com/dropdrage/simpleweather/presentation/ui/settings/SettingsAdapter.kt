package com.dropdrage.simpleweather.presentation.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.adapters.simple.SimpleRecyclerListAdapter
import com.dropdrage.simpleweather.databinding.ItemSettingBinding
import com.dropdrage.simpleweather.presentation.model.ViewSetting

class SettingsAdapter(
    settings: List<ViewSetting>,
    private val openSettingChangeDialog: OnItemClickListener<ViewSetting>,
) : SimpleRecyclerListAdapter<ViewSetting, SettingsViewHolder>() {

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