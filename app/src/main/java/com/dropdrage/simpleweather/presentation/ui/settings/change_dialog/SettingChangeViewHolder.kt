package com.dropdrage.simpleweather.presentation.ui.settings.change_dialog

import androidx.core.view.isVisible
import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.databinding.ItemSettingChangeBinding
import com.dropdrage.simpleweather.presentation.model.AnySetting

class SettingChangeViewHolder(
    binding: ItemSettingChangeBinding,
    onClickListener: OnItemClickListener<AnySetting>,
) : ClickableViewHolder<AnySetting, ItemSettingChangeBinding>(binding, onClickListener) {

    var isSelected: Boolean = false
        set(value) {
            binding.check.isVisible = value
            field = value
        }


    override fun bindData(value: AnySetting) {
        binding.apply {
            this.value.text = root.context.getString(value.unitResId, "")
        }
    }
}