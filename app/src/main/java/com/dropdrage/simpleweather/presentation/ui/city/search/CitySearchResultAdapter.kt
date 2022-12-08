package com.dropdrage.simpleweather.presentation.ui.city.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.simpleweather.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.presentation.model.ViewCitySearchResult
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener
import com.dropdrage.simpleweather.presentation.util.adapter.differ.DifferRecyclerAdapter

class CitySearchResultAdapter(private val onItemClickListener: OnItemClickListener<ViewCitySearchResult>) :
    DifferRecyclerAdapter<ViewCitySearchResult, CitySearchResultViewHolder>() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CitySearchResultViewHolder(
        ItemSearchCityBinding.inflate(inflater, parent, false),
        onItemClickListener,
    )
}