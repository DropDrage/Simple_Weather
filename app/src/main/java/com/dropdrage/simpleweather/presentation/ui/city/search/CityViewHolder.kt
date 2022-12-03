package com.dropdrage.simpleweather.presentation.ui.city.search

import com.dropdrage.simpleweather.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.domain.city.search.City
import com.dropdrage.simpleweather.presentation.util.adapter.ClickableViewHolder
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener

class CityViewHolder(
    binding: ItemSearchCityBinding,
    onClickListener: OnItemClickListener<City>,
) : ClickableViewHolder<City, ItemSearchCityBinding>(binding, onClickListener) {
    override fun bindData(value: City) {
        binding.apply {
            city.text = value.name
            country.text = value.country.name
        }
    }
}