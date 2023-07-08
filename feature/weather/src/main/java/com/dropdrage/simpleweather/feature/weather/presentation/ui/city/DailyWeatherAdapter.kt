package com.dropdrage.simpleweather.feature.weather.presentation.ui.city

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.adapters.differ.DifferRecyclerAdapter
import com.dropdrage.simpleweather.feature.weather.databinding.ItemDayWeatherBinding
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewDayWeather

internal class DailyWeatherAdapter(private val onItemClick: OnItemClickListener<ViewDayWeather>) :
    DifferRecyclerAdapter<ViewDayWeather, DayWeatherViewHolder>() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = DayWeatherViewHolder(
        ItemDayWeatherBinding.inflate(inflater, parent, false),
        onItemClick
    )
}