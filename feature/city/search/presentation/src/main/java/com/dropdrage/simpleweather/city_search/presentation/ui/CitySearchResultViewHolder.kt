package com.dropdrage.simpleweather.city_search.presentation.ui

import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.city_search.presentation.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.city_search.presentation.model.ViewCitySearchResult

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
