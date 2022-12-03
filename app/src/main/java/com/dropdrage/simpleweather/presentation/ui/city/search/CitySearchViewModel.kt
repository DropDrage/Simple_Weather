package com.dropdrage.simpleweather.presentation.ui.city.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.city.search.City
import com.dropdrage.simpleweather.domain.city.search.CitySearchRepository
import com.dropdrage.simpleweather.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val QUERY_DEBOUNCE = 500L

private const val TAG = "CitySearch"

@OptIn(FlowPreview::class)
@HiltViewModel
class CitySearchViewModel @Inject constructor(
    private val searchRepository: CitySearchRepository,
    private val cityRepository: CityRepository,
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<City>>()
    val searchResults: LiveData<List<City>> = _searchResults

    private val query = MutableStateFlow("")

    private val _cityAddedEvent = MutableLiveData<Unit>()
    val cityAddedEvent: LiveData<Unit> = _cityAddedEvent


    init {
        viewModelScope.launch {
            query.debounce(QUERY_DEBOUNCE).collect(::loadCities)
        }
    }


    fun updateQuery(newQuery: String) {
        viewModelScope.launch { query.emit(newQuery) }
    }

    private suspend fun loadCities(query: String) {
        when (val result = searchRepository.searchCities(query)) {
            is Resource.Success -> _searchResults.value = result.data
            is Resource.Error -> Log.e(TAG, result.message, result.exception)
        }
    }

    fun addCity(city: City) {
        viewModelScope.launch {
            cityRepository.addCity(city)
            _cityAddedEvent.value = Unit
        }
    }
}