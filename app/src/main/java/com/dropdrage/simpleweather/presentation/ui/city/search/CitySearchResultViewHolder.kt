package com.dropdrage.simpleweather.presentation.ui.city.search

import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.presentation.model.ViewCitySearchResult

class CitySearchResultViewHolder(
    binding: ItemSearchCityBinding,
    onClickListener: OnItemClickListener<ViewCitySearchResult>,
) : ClickableViewHolder<ViewCitySearchResult, ItemSearchCityBinding>(binding, onClickListener) {
    override fun bindData(value: ViewCitySearchResult) {
        binding.apply {
            city.text = value.name
            country.text = value.countryName
        }
    }
}