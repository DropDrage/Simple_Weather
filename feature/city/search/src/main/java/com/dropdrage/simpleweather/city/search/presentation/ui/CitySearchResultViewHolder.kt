package com.dropdrage.simpleweather.city.search.presentation.ui

import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.city.search.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.city.search.presentation.model.ViewCitySearchResult

internal class CitySearchResultViewHolder(
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
