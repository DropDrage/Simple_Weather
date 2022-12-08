package com.dropdrage.simpleweather.presentation.ui.city.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.simpleweather.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.domain.city.City
import com.dropdrage.simpleweather.presentation.util.adapter.DifferRecyclerAdapter
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener

class CitiesAdapter(private val onItemClickListener: OnItemClickListener<City>) :
    DifferRecyclerAdapter<City, CityViewHolder>() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CityViewHolder(
        ItemSearchCityBinding.inflate(inflater, parent, false),
        onItemClickListener,
    )
}