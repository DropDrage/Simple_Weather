package com.dropdrage.simpleweather.feature.settings.presentation.ui

import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.feature.settings.databinding.ItemSettingBinding
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewSetting
import com.dropdrage.util.extension.implicitAccess

internal class SettingsViewHolder(
    binding: ItemSettingBinding,
    onClickListener: OnItemClickListener<ViewSetting>,
) : ClickableViewHolder<ViewSetting, ItemSettingBinding>(binding, onClickListener) {
    override fun bindData(value: ViewSetting) = binding.implicitAccess {
        label.text = value.label
        this.value.text = value.currentValue
    }
}
