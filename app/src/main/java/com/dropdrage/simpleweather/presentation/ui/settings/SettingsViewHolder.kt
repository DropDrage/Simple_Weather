package com.dropdrage.simpleweather.presentation.ui.settings

import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.databinding.ItemSettingBinding
import com.dropdrage.simpleweather.presentation.model.ViewSetting

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