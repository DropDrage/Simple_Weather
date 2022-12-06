package com.dropdrage.simpleweather.presentation.ui.city.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.simpleweather.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.domain.city.City
import com.dropdrage.simpleweather.presentation.util.adapter.DifferRecyclerAdapter
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener

class CitiesAdapter(private val onItemClickListener: OnItemClickListener<City>) :
    DifferRecyclerAdapter<City, CityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder = CityViewHolder(
        ItemSearchCityBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onItemClickListener,
    )
}