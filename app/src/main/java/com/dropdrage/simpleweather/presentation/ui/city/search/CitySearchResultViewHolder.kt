package com.dropdrage.simpleweather.presentation.ui.city.search

import com.dropdrage.simpleweather.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.presentation.model.ViewCitySearchResult
import com.dropdrage.simpleweather.presentation.util.adapter.ClickableViewHolder
import com.dropdrage.simpleweather.presentation.util.adapter.OnItemClickListener

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