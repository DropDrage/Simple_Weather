package com.dropdrage.simpleweather.feature.settings.presentation.ui.change_dialog

import androidx.core.view.isVisible
import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.feature.settings.databinding.ItemSettingChangeBinding
import com.dropdrage.simpleweather.feature.settings.presentation.model.AnySetting
import com.dropdrage.util.extension.implicitAccess

internal class SettingChangeViewHolder(
    binding: ItemSettingChangeBinding,
    onClickListener: OnItemClickListener<AnySetting>,
) : ClickableViewHolder<AnySetting, ItemSettingChangeBinding>(binding, onClickListener) {

    var isSelected: Boolean = false
        set(value) {
            binding.check.isVisible = value
            field = value
        }


    override fun bindData(value: AnySetting) = binding.implicitAccess {
        this.value.text = root.context.getString(value.unitResId, "")
    }

}
