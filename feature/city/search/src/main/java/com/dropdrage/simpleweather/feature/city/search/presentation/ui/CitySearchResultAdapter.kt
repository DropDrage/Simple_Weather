package com.dropdrage.simpleweather.feature.city.search.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.adapters.differ.DifferRecyclerAdapter
import com.dropdrage.simpleweather.feature.city.search.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.feature.city.search.presentation.model.ViewCitySearchResult

internal class CitySearchResultAdapter(private val onItemClickListener: OnItemClickListener<ViewCitySearchResult>) :
    DifferRecyclerAdapter<ViewCitySearchResult, CitySearchResultViewHolder>() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CitySearchResultViewHolder(
        ItemSearchCityBinding.inflate(inflater, parent, false),
        onItemClickListener,
    )
}
