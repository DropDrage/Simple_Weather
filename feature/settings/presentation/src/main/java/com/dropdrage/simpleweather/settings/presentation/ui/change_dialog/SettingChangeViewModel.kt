package com.dropdrage.simpleweather.settings.presentation.change_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dropdrage.simpleweather.settings.presentation.model.AnySetting

internal class SettingChangeViewModel : ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _selectedSetting = MutableLiveData<AnySetting>()
    val selectedSetting: LiveData<AnySetting> = _selectedSetting

    private val _values = MutableLiveData<List<AnySetting>>()
    val values: LiveData<List<AnySetting>> = _values


    fun setSelectedSetting(selectedSetting: AnySetting) {
        _selectedSetting.value = selectedSetting
    }

    fun changeTargetSetting(title: String, values: List<AnySetting>) {
        _title.value = title
        _values.value = values
    }

}
