package com.dropdrage.simpleweather.settings.ui

import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.settings.databinding.ItemSettingBinding
import com.dropdrage.simpleweather.settings.model.ViewSetting

class SettingsViewHolder(
    binding: ItemSettingBinding,
    onClickListener: OnItemClickListener<ViewSetting>,
) : ClickableViewHolder<ViewSetting, ItemSettingBinding>(binding, onClickListener) {
    override fun bindData(value: ViewSetting) {
        binding.apply {
            label.text = value.label
            this.value.text = value.currentValue
        }
    }
}