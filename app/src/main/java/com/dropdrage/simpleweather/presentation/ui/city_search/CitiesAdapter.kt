package com.dropdrage.simpleweather.presentation.ui.city_search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.simpleweather.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.domain.search.City
import com.dropdrage.simpleweather.presentation.util.simple_adapter.ItemClickableRecyclerListAdapter
import com.dropdrage.simpleweather.presentation.util.simple_adapter.OnItemClickListener

class CitiesAdapter(private val onItemClickListener: OnItemClickListener<City>) :
    ItemClickableRecyclerListAdapter<City, CityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder = CityViewHolder(
        ItemSearchCityBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onItemClickListener,
    )
}