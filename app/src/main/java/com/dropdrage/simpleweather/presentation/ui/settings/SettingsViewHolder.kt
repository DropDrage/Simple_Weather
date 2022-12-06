package com.dropdrage.simpleweather.presentation.ui.settings

import com.dropdrage.simpleweather.databinding.ItemSettingBinding
import com.dropdrage.simpleweather.presentation.model.ViewSetting
import com.dropdrage.simpleweather.presentation.util.adapter.ClickableViewHolder
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener

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