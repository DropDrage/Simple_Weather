package com.dropdrage.simpleweather.feature.weather.presentation.ui.city

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dropdrage.adapters.OnItemClickListener
import com.dropdrage.adapters.differ.DifferRecyclerAdapter
import com.dropdrage.simpleweather.feature.weather.databinding.ItemHourWeatherBinding
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewHourWeather

internal class HourlyWeatherAdapter(private val onItemClick: OnItemClickListener<ViewHourWeather>) :
    DifferRecyclerAdapter<ViewHourWeather, HourWeatherViewHolder>() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) = HourWeatherViewHolder(
        ItemHourWeatherBinding.inflate(inflater, parent, false),
        onItemClick
    )
}