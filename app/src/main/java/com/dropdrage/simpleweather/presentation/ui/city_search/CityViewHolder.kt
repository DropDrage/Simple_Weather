package com.dropdrage.simpleweather.presentation.ui.city_search

import com.dropdrage.simpleweather.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.domain.search.City
import com.dropdrage.simpleweather.presentation.util.simple_adapter.ClickableViewHolder
import com.dropdrage.simpleweather.presentation.util.simple_adapter.OnItemClickListener

class CityViewHolder(
    binding: ItemSearchCityBinding,
    onClickListener: OnItemClickListener<City>,
) : ClickableViewHolder<City, ItemSearchCityBinding>(binding, onClickListener) {
    override fun displayData(city: City) {
        binding.apply {
            this.city.text = city.name
            country.text = city.country
        }
    }
}