package com.dropdrage.simpleweather.settings.presentation.change_dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.common.presentation.util.extension.setLinearLayoutManager
import com.dropdrage.common.presentation.utils.collectWithViewLifecycle
import com.dropdrage.simpleweather.settings.presentation.R
import com.dropdrage.simpleweather.settings.presentation.databinding.DialogSettingChangeBinding
import com.dropdrage.simpleweather.settings.presentation.model.AnySetting

internal class SettingChangeDialog(
    private val onSettingChanged: (AnySetting) -> Unit,
) : DialogFragment(R.layout.dialog_setting_change) {

    private val binding by viewBinding(DialogSettingChangeBinding::bind)

    private lateinit var settingAdapter: SettingChangeAdapter
    private val viewModel: SettingChangeViewModel by viewModels(ownerProducer = { requireParentFragment() })


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyStyle()

        initSettingsList()
        setOnClickListeners()
        observeViewModel()
    }

    private fun applyStyle() {
        requireDialog().window!!.apply {
            setGravity(Gravity.BOTTOM)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawableResource(R.drawable.background_dialog)
        }
    }

    private fun setOnClickListeners() = binding.apply {
        cancel.setOnClickListener { dismiss() }
    }

    private fun initSettingsList() = binding.settings.apply {
        setLinearLayoutManager()
        adapter = SettingChangeAdapter(onSettingChanged).also { settingAdapter = it }
    }

    private fun observeViewModel() = viewModel.apply {
        collectWithViewLifecycle(title) {
            binding.title.text = it
        }
        collectWithViewLifecycle(selectedSetting) {
            settingAdapter.selectedSetting = it
        }
        collectWithViewLifecycle(values) {
            settingAdapter.values = it
        }
    }
}
