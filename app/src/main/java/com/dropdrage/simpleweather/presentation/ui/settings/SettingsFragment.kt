package com.dropdrage.simpleweather.presentation.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.databinding.FragmentSettingsBinding
import com.dropdrage.simpleweather.presentation.model.AnySetting
import com.dropdrage.simpleweather.presentation.model.ViewSetting
import com.dropdrage.simpleweather.presentation.ui.settings.change_dialog.SettingChangeDialog
import com.dropdrage.simpleweather.presentation.ui.settings.change_dialog.SettingChangeViewModel
import com.dropdrage.simpleweather.presentation.util.extension.setLinearLayoutManager
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
        settingChanged.observe(viewLifecycleOwner) {
            settingsAdapter.changeSetting(it)
        }
    }

}