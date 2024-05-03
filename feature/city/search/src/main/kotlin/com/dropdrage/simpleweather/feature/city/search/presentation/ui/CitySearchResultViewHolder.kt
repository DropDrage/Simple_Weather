package com.dropdrage.simpleweather.feature.city.search.presentation.ui

import com.dropdrage.adapters.ClickableViewHolder
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.simpleweather.feature.city.search.databinding.ItemSearchCityBinding
import com.dropdrage.simpleweather.feature.city.search.presentation.model.ViewCitySearchResult
import com.dropdrage.util.extension.implicitAccess

internal class CitySearchResultViewHolder(
    binding: ItemSearchCityBinding,
    onClickListener: OnItemClickListener<ViewCitySearchResult>,
) : ClickableViewHolder<ViewCitySearchResult, ItemSearchCityBinding>(binding, onClickListener) {
    override fun bindData(value: ViewCitySearchResult) = binding.implicitAccess {
        city.text = value.name
        country.text = value.countryName
    }
}
