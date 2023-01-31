package com.dropdrage.simpleweather.city_search.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropdrage.common.domain.Resource
import com.dropdrage.simpleweather.city.domain.CityRepository
import com.dropdrage.simpleweather.city_search.domain.CitySearchRepository
import com.dropdrage.simpleweather.city_search.presentation.model.ViewCitySearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val QUERY_DEBOUNCE = 500L

private const val TAG = "CitySearch"

@OptIn(FlowPreview::class)
@HiltViewModel
internal class CitySearchViewModel @Inject constructor(
    private val searchRepository: CitySearchRepository,
    private val cityRepository: CityRepository,
) : ViewModel() {

    private val _searchResults = MutableSharedFlow<List<ViewCitySearchResult>>()
    val searchResults: Flow<List<ViewCitySearchResult>> = _searchResults.asSharedFlow()

    private val _query = MutableStateFlow("")
    val query: Flow<String> = _query.asStateFlow()

    private val _cityAddedEvent = MutableSharedFlow<Unit>()
    val cityAddedEvent: Flow<Unit> = _cityAddedEvent.asSharedFlow()


    init {
        viewModelScope.launch {
            query.debounce(QUERY_DEBOUNCE).collect(::loadCities)
        }
    }


    fun updateQuery(newQuery: String) {
        viewModelScope.launch { _query.emit(newQuery) }
    }

    private suspend fun loadCities(query: String) {
        when (val result = searchRepository.searchCities(query)) {
            is Resource.Success -> _searchResults.emit(result.data.map {
                ViewCitySearchResult(it)
            })
            is Resource.Error -> Log.e(TAG, result.message, result.exception)
        }
    }

    fun addCity(cityResult: ViewCitySearchResult) {
        viewModelScope.launch {
            cityRepository.addCity(cityResult.city)
            _cityAddedEvent.emit(Unit)
        }
    }

}
