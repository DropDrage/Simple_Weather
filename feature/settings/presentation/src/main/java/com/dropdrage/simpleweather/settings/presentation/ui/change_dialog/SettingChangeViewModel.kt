package com.dropdrage.simpleweather.settings.presentation.change_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.settings.presentation.model.AnySetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SettingChangeViewModel : ViewModel() {

    private val _selectedSetting = MutableSharedFlow<AnySetting>()
    val selectedSetting: Flow<AnySetting> = _selectedSetting.asSharedFlow()

    private val _title = MutableStateFlow("")
    val title: Flow<String> = _title.asStateFlow()

    private val _values = MutableStateFlow<List<AnySetting>>(emptyList())
    val values: Flow<List<AnySetting>> = _values.asStateFlow()


    fun setSelectedSetting(selectedSetting: AnySetting) {
        viewModelScope.launch {
            _selectedSetting.emit(selectedSetting)
        }
    }

    fun changeTargetSetting(title: String, values: List<AnySetting>) {
        viewModelScope.launch {
            _title.emit(title)
            _values.emit(values)
        }
    }

}
