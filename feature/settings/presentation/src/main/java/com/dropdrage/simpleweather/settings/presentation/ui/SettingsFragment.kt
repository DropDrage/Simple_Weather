package com.dropdrage.simpleweather.settings.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.common.presentation.util.extension.setLinearLayoutManager
import com.dropdrage.common.presentation.utils.collectWithViewLifecycle
import com.dropdrage.simpleweather.settings.presentation.change_dialog.SettingChangeDialog
import com.dropdrage.simpleweather.settings.presentation.change_dialog.SettingChangeViewModel
import com.dropdrage.simpleweather.settings.presentation.databinding.FragmentSettingsBinding
import com.dropdrage.simpleweather.settings.presentation.model.AnySetting
import com.dropdrage.simpleweather.settings.presentation.model.ViewSetting
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsViewModel by viewModels()
    private val dialogViewModel: SettingChangeViewModel by viewModels()

    private lateinit var settingsAdapter: SettingsAdapter
    private lateinit var settingsChangeDialog: SettingChangeDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSettingChangeDialog()
        initSettingsList()
        observeViewModel()
    }

    private fun initSettingChangeDialog() {
        settingsChangeDialog = SettingChangeDialog(::onSettingChanged)
    }

    private fun onSettingChanged(setting: AnySetting) {
        viewModel.changeSetting(setting)
        dialogViewModel.setSelectedSetting(setting)
        settingsChangeDialog.dismiss()
    }

    private fun initSettingsList() = binding.settingsList.apply {
        setLinearLayoutManager()
        setHasFixedSize(true)

        adapter = SettingsAdapter(viewModel.settings, ::openSettingChangeDialog).also { settingsAdapter = it }
    }

    private fun openSettingChangeDialog(setting: ViewSetting) {
        val currentValue = viewModel.getCurrentValue(setting.values[0])
        dialogViewModel.setSelectedSetting(currentValue)
        dialogViewModel.changeTargetSetting(setting.label, setting.values)
        settingsChangeDialog.show(childFragmentManager, null)
    }

    private fun observeViewModel() = viewModel.apply {
        collectWithViewLifecycle(settingChanged, settingsAdapter::changeSetting)
    }

}